package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CourseVideoAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.course.course_video.CourseVideoResponse;
import com.rajasahabacademy.support.Utils;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class CourseTopicVideoActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    LinearLayout bottomAmountLay;
    TextView tvAmount;
    ShimmerFrameLayout courseShimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_course_topic_video);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;
        setClickListener();
        getCourseVideo();
        setUpPrice();
    }

    private void setClickListener() {
        bottomAmountLay = findViewById(R.id.bottom_course_video_amount_lay);
        courseShimmer = findViewById(R.id.course_video_shimmer);
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
        if (getIntent().getStringExtra(Constants.Course.COURSE_BUY_STATUS).equals("0"))
            bottomAmountLay.setVisibility(View.GONE);
        else bottomAmountLay.setVisibility(View.GONE);
    }

    private void setUpPrice() {
        TextView tvSubjectName = findViewById(R.id.tv_course_video_subject);
        TextView tvTotalAmount = findViewById(R.id.tv_course_detail_total_amount_1);
        tvAmount = findViewById(R.id.tv_course_detail_amount_1);
        tvSubjectName.setText(getIntent().getStringExtra(Constants.Course.SUBJECT));
        tvAmount.setText(getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT));
        tvTotalAmount.setText(getIntent().getStringExtra(Constants.Course.EXPIRE_AMOUNT));
        tvTotalAmount.setPaintFlags(tvTotalAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void getCourseVideo() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)) {
            courseShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.COURSE_ID, getIntent().getStringExtra(Constants.Course.COURSE_ID));
                params.put(Constants.Params.SUBJECT_ID, getIntent().getStringExtra(Constants.Course.SUBJECT_ID));
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.COURSE_VIDEO, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    courseShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            CourseVideoResponse modelResponse = (CourseVideoResponse) Utils.getObject(response, CourseVideoResponse.class);
                            if (modelResponse != null && modelResponse.getResults() != null) {
                                if (modelResponse.getResults().getData() != null && modelResponse.getResults().getData().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_course_video).setVisibility(View.GONE);
                                    recyclerView.setAdapter(new CourseVideoAdapter(mActivity, modelResponse.getResults().getData(),
                                            getIntent().getStringExtra(Constants.Course.COURSE_BUY_STATUS)));
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_course_video).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_course_video).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_course_video).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    courseShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_course_video).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private int getPaybleAmount() {
        String amount = tvAmount.getText().toString();
        double amountD = Double.parseDouble(amount);
        return (int) amountD;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }
}