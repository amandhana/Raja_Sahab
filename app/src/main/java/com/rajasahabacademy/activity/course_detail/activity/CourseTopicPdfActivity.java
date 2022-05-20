package com.rajasahabacademy.activity.course_detail.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.adapter.CourseTopicPdfAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.course_detail.model.course_pdf.CoursePdfResponse;
import com.rajasahabacademy.support.Utils;

public class CourseTopicPdfActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    String courseId = "";
    String subjectId = "";
    String courseByStatus = "";
    ShimmerFrameLayout coursePdfShimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_course_topic_pdf);
        init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
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
        setUpBundleData();
        getCourseEbook();
    }

    private void setClickListener() {
        coursePdfShimmer = findViewById(R.id.course_pdf_shimmer);
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
    }

    private void setUpBundleData() {
        TextView tvSubjectName = findViewById(R.id.tv_pdf_subject_name);
        TextView tvTotalAmount = findViewById(R.id.tv_pdf_total_amount_2);
        TextView tvAmount = findViewById(R.id.tv_pdf_amount_2);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            courseId = bundle.getString(Constants.Course.COURSE_ID);
            subjectId = bundle.getString(Constants.Course.SUBJECT_ID);
            courseByStatus = bundle.getString(Constants.Course.COURSE_BUY_STATUS);
            tvSubjectName.setText(bundle.getString(Constants.Course.SUBJECT));
            tvAmount.setText(getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT));
            tvTotalAmount.setText(getIntent().getStringExtra(Constants.Course.EXPIRE_AMOUNT));
            tvTotalAmount.setPaintFlags(tvTotalAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void getCourseEbook() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)) {
            coursePdfShimmer.startShimmer();
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
            communicator.post(101, mActivity, Constants.Apis.COURSE_EBOOK, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    coursePdfShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            CoursePdfResponse modelResponse = (CoursePdfResponse) Utils.getObject(response, CoursePdfResponse.class);
                            if (modelResponse != null && modelResponse.getResults() != null) {
                                if (modelResponse.getResults().getEbooks() != null && modelResponse.getResults().getEbooks().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_course_pdf).setVisibility(View.GONE);
                                    recyclerView.setAdapter(new CourseTopicPdfAdapter(mActivity, modelResponse.getResults().getEbooks(),
                                            courseByStatus));
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    coursePdfShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_course_pdf).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }
}