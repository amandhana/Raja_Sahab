package com.rajasahabacademy.activity.course_detail.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.payment.activity.PaymentActivity;
import com.rajasahabacademy.activity.course_detail.adapter.CourseDetailVideoAdapter;
import com.rajasahabacademy.activity.course_detail.adapter.CourseDetailPdfAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.course_detail.model.course_subject.CourseSubjectResponse;
import com.rajasahabacademy.activity.home.model.latest_course.Course;
import com.rajasahabacademy.support.Utils;
import com.rajasahabacademy.support.ViewUtils;

import org.json.JSONObject;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    ImageView ivImage;
    ShimmerFrameLayout courseDetailVideoShimmer;
    ShimmerFrameLayout courseDetailPdfShimmer;
    ImageView ivVideoDropDown;
    ImageView ivPdfDropDown;
    RecyclerView recyclerViewVideo;
    RecyclerView recyclerViewPdf;
    TextView tvAmount;
    LinearLayout bottomAmountLay;
    String courseStatus = "free";
    CourseDetailVideoAdapter courseDetailVideoAdapter;
    CourseDetailPdfAdapter courseDetailPdfAdapter;
    String fromWhereStr = "";
    String courseId = "";
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_course_detail);
        init();
    }

    @Override
    public void onBackPressed() {
        if (fromWhereStr.equalsIgnoreCase("")) {
            super.onBackPressed();
        } else if (fromWhereStr.equalsIgnoreCase(Constants.Course.FROM_WHERE_VALUE_SLIDER))
            super.onBackPressed();
        else {
            finishAffinity();
            Utils.startActivityFinish(mActivity, HomeActivity.class);
        }
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;
        getIntentData();
        setClickListener();
        if (fromWhereStr.equalsIgnoreCase("")) {
            Utils.setImageUsingGlide(mActivity, getIntent().getStringExtra(Constants.Preference.COURSE_IMAGE), ivImage);
            setUpPrice();
        }
        getCourseSubject();
        handleOnActivityResult();
    }

    private void getIntentData() {
        if (getIntent().getStringExtra(Constants.Course.FROM_WHERE) != null)
            fromWhereStr = getIntent().getStringExtra(Constants.Course.FROM_WHERE);
        if (getIntent().getStringExtra(Constants.Course.COURSE_ID) != null)
            courseId = getIntent().getStringExtra(Constants.Course.COURSE_ID);
    }

    private void setClickListener() {
        bottomAmountLay = findViewById(R.id.bottom_amount_lay);
        recyclerViewVideo = findViewById(R.id.recycler_view_video_tops);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));

        recyclerViewPdf = findViewById(R.id.recycler_view_pdf_notes);
        recyclerViewPdf.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));

        courseDetailVideoShimmer = findViewById(R.id.course_detail_video_shimmer);
        courseDetailPdfShimmer = findViewById(R.id.course_detail_pdf_shimmer);
        ivImage = findViewById(R.id.iv_image);
        CardView cvmenu = findViewById(R.id.cv_back);
        RelativeLayout videoDropDownLay = findViewById(R.id.all_video_tops_lay);
        RelativeLayout pdfLay = findViewById(R.id.pdf_lay);
        ivVideoDropDown = findViewById(R.id.iv_video_drop_down);
        ivPdfDropDown = findViewById(R.id.iv_pdf_drop_down);
        LinearLayout buyNowLay = findViewById(R.id.buy_now_lay);
        LinearLayout bottomAmountLay = findViewById(R.id.bottom_amount_lay);
        bottomAmountLay.setOnClickListener(this);

        cvmenu.setOnClickListener(this);
        videoDropDownLay.setOnClickListener(this);
        pdfLay.setOnClickListener(this);
    }

    private void getCourseSubject() {
        if (Utils.isNetworkAvailable(mActivity)) {
            courseDetailVideoShimmer.startShimmer();
            courseDetailPdfShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Course.COURSE_ID, courseId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.COURSE_SUBJECT, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    courseDetailVideoShimmer.setVisibility(View.GONE);
                    courseDetailPdfShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            CourseSubjectResponse modelResponse = (CourseSubjectResponse) Utils.getObject(response, CourseSubjectResponse.class);
                            if (modelResponse != null && modelResponse.getSuccess() != null && modelResponse.getSuccess()) {
                                if (modelResponse.getVideosCategory() != null && modelResponse.getVideosCategory().size() > 0) {
                                    recyclerViewVideo.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_subject_video).setVisibility(View.GONE);
                                    courseDetailVideoAdapter = new CourseDetailVideoAdapter(mActivity, modelResponse.getVideosCategory(),
                                            courseId,
                                            getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT),
                                            getIntent().getStringExtra(Constants.Course.EXPIRE_AMOUNT),
                                            getCourseBuyStatus());
                                    recyclerViewVideo.setAdapter(courseDetailVideoAdapter);
                                } else {
                                    recyclerViewVideo.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_subject_video).setVisibility(View.VISIBLE);
                                }
                                if (modelResponse.getEbookCategory() != null && modelResponse.getEbookCategory().size() > 0) {
                                    recyclerViewPdf.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_subject_pdf).setVisibility(View.GONE);
                                    courseDetailPdfAdapter = new CourseDetailPdfAdapter(mActivity, modelResponse.getEbookCategory(),
                                            courseId
                                            , getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT),
                                            getIntent().getStringExtra(Constants.Course.EXPIRE_AMOUNT),
                                            getCourseBuyStatus());
                                    recyclerViewPdf.setAdapter(courseDetailPdfAdapter);
                                } else {
                                    recyclerViewPdf.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_subject_pdf).setVisibility(View.VISIBLE);
                                }
                                /*if (fromWhereStr.equalsIgnoreCase(Constants.Course.FROM_WHERE_VALUE) ||
                                        fromWhereStr.equalsIgnoreCase(Constants.Course.FROM_WHERE_VALUE_SLIDER)) {
                                    if (modelResponse.getCourse() != null)
                                        setUpDataFromNotification(modelResponse.getCourse());
                                }*/
                            } else {
                                recyclerViewVideo.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_subject_video).setVisibility(View.VISIBLE);
                                recyclerViewPdf.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_subject_pdf).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerViewVideo.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_subject_video).setVisibility(View.VISIBLE);
                        recyclerViewPdf.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_subject_pdf).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    courseDetailVideoShimmer.setVisibility(View.GONE);
                    courseDetailPdfShimmer.setVisibility(View.GONE);
                    recyclerViewVideo.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_subject_video).setVisibility(View.VISIBLE);
                    recyclerViewPdf.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_subject_pdf).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void setUpDataFromNotification(Course model) {
        Utils.setImageUsingGlide(mActivity, model.getThumbnail(), ivImage);
        TextView tvCourseTitle = findViewById(R.id.tv_course_title);
        tvAmount = findViewById(R.id.tv_course_detail_amount_1);
        tvCourseTitle.setText(model.getTitle());
        tvAmount.setText(model.getPrice());
        if (model.getCourseBuyStatus().equals("0") &&
                !model.getPrice().equals("0")) {
            bottomAmountLay.setVisibility(View.VISIBLE);
            courseStatus = "paid";
        } else {
            bottomAmountLay.setVisibility(View.GONE);
            courseStatus = "free";
        }
    }

    private String getCourseBuyStatus() {
        if (!courseStatus.equals("free"))
            return getIntent().getStringExtra(Constants.Course.COURSE_BUY_STATUS);
        else return "1";
    }

    private void showHideVideoSubjectList() {
        if (recyclerViewVideo.getVisibility() == View.VISIBLE) {
            ViewUtils.collapse(recyclerViewVideo);
            ViewUtils.collapse(findViewById(R.id.video_recycler_lay));
            ivVideoDropDown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
        } else {
            ivVideoDropDown.setBackgroundResource(R.drawable.ic_arrow_drop_up);
            ViewUtils.expand(recyclerViewVideo);
            ViewUtils.expand(findViewById(R.id.video_recycler_lay));
        }
    }

    private void showHidePdfSubjectList() {
        if (recyclerViewPdf.getVisibility() == View.VISIBLE) {
            ViewUtils.collapse(recyclerViewPdf);
            ViewUtils.collapse(findViewById(R.id.pdf_recycler_lay));
            ivPdfDropDown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
        } else {
            ivPdfDropDown.setBackgroundResource(R.drawable.ic_arrow_drop_up);
            ViewUtils.expand(recyclerViewPdf);
            ViewUtils.expand(findViewById(R.id.pdf_recycler_lay));
        }
    }

    private void setUpPrice() {
        TextView tvCourseTitle = findViewById(R.id.tv_course_title);
        TextView tvTotalAmount = findViewById(R.id.tv_course_detail_total_amount_1);
        tvAmount = findViewById(R.id.tv_course_detail_amount_1);
        tvCourseTitle.setText(getIntent().getStringExtra(Constants.Course.COURSE_TITLE));
        tvAmount.setText(getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT));
        tvTotalAmount.setText(getIntent().getStringExtra(Constants.Course.EXPIRE_AMOUNT));
        tvTotalAmount.setPaintFlags(tvTotalAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (getIntent().getStringExtra(Constants.Course.COURSE_BUY_STATUS).equals("0") &&
                !getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT).equals("0")) {
            bottomAmountLay.setVisibility(View.VISIBLE);
            courseStatus = "paid";
        } else {
            bottomAmountLay.setVisibility(View.GONE);
            courseStatus = "free";
        }
    }

    private int getPaybleAmount() {
        String amount = tvAmount.getText().toString();
        double amountD = Double.parseDouble(amount);
        return (int) amountD;
    }

    private void buyNowCourse(int paybleAmount, int remainWalletAmount, String paymentType) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.COURSE_ID, courseId);
                params.put(Constants.Params.PRICE, tvAmount.getText().toString());
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.BUY_COURSE, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject object = new JSONObject(response);
                            if (object.optString("message").equalsIgnoreCase("ok")) {
                                courseStatus = "free";
                                if (courseDetailVideoAdapter != null)
                                    courseDetailVideoAdapter.updateStatus(getCourseBuyStatus());
                                if (courseDetailPdfAdapter != null)
                                    courseDetailPdfAdapter.updateStatus(getCourseBuyStatus());
                                bottomAmountLay.setVisibility(View.GONE);
                                Utils.showToastPopup(mActivity, getString(R.string.course_buy_success));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void handleOnActivityResult() {
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        courseStatus = "free";
                        if (courseDetailVideoAdapter != null)
                            courseDetailVideoAdapter.updateStatus(getCourseBuyStatus());
                        if (courseDetailPdfAdapter != null)
                            courseDetailPdfAdapter.updateStatus(getCourseBuyStatus());
                        bottomAmountLay.setVisibility(View.GONE);
                    }
                });
    }

    private void startPayment() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(Constants.Course.TOTAL_AMOUNT, String.valueOf(getPaybleAmount()));
        intent.putExtra(Constants.Course.COURSE_ID, courseId);
        intent.putExtra("from_where", "course_detail");
        someActivityResultLauncher.launch(intent);

        /*try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_wallet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            RelativeLayout walletLay = dialog.findViewById(R.id.wallet_lay);
            TextView tvUpi = dialog.findViewById(R.id.tv_upi);
            TextView tvWallet = dialog.findViewById(R.id.tv_wallet);
            tvWallet.setText(Utils.getSaveLoginUser(mActivity).getResults().getWallet());

            int walletAmount = (int) Double.parseDouble(tvWallet.getText().toString());


            walletLay.setOnClickListener(v -> {
                dialog.dismiss();
                if (walletAmount == 0 || walletAmount < 0)
                    Toast.makeText(mActivity, "Wallet amount is 0", Toast.LENGTH_SHORT).show();
                else if (walletAmount < getPaybleAmount())
                    Toast.makeText(mActivity, "Wallet amount is less than payble amount", Toast.LENGTH_SHORT).show();
                else if (walletAmount == getPaybleAmount()) {
                    int remainWalletAmount = walletAmount - getPaybleAmount();
                    buyNowCourse(getPaybleAmount(),remainWalletAmount,"wallet");
                }
                else{
                    int remainWalletAmount = walletAmount - getPaybleAmount();
                    buyNowCourse(getPaybleAmount(),remainWalletAmount,"wallet");
                }
            });

            tvUpi.setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(Constants.Course.TOTAL_AMOUNT, String.valueOf(getPaybleAmount()));
                intent.putExtra(Constants.Course.WALLET_AMOUNT, String.valueOf(walletAmount));
                intent.putExtra(Constants.Course.COURSE_ID, courseId);
                someActivityResultLauncher.launch(intent);
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.all_video_tops_lay) {
            showHideVideoSubjectList();
        } else if (id == R.id.pdf_lay) {
            showHidePdfSubjectList();
        } else if (id == R.id.bottom_amount_lay) {
            startPayment();
        }
    }
}