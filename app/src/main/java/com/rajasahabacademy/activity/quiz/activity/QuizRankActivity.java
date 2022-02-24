package com.rajasahabacademy.activity.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.view_solution.ViewSolutionActivity;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.quiz.adapter.QuizRankAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.quiz.model.rank.RankResponse;
import com.rajasahabacademy.activity.quiz.model.rank.UserResult;
import com.rajasahabacademy.support.Utils;

public class QuizRankActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    String testId;
    String testDuration;
    String quizNameStr;
    String fromWhereStr;
    RankResponse modelResponse;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_quiz_rank);
        init();
    }

    @Override
    public void onBackPressed() {
        if (fromWhereStr.equalsIgnoreCase(Constants.Quiz.FROM_WHERE_VALUE)) {
            finishAffinity();
            Utils.startActivityFinish(mActivity, HomeActivity.class);
        } else {
            super.onBackPressed();
            Utils.hideKeyboard(mActivity);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    private void init() {
        mActivity = this;
        setClickListener();
        getBundleData();
        setUpRank();
    }

    private void setClickListener() {
        shimmerFrameLayout = findViewById(R.id.quiz_rank_shimmer);
        TextView tvExit = findViewById(R.id.tv_exit);
        tvExit.setOnClickListener(this);
        TextView tvReAttempt = findViewById(R.id.tv_reattempt);
        tvReAttempt.setOnClickListener(this);
        TextView tvViewSolution = findViewById(R.id.tv_view_solution);
        tvViewSolution.setOnClickListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            testId = bundle.getString(Constants.Params.TEST_ID);
            testDuration = bundle.getString(Constants.Params.TEST_DURATION);
            quizNameStr = bundle.getString(Constants.Quiz.QUIZ_NAME);
            fromWhereStr = bundle.getString(Constants.Quiz.FROM_WHERE);
        }
    }

    private void setUpRank() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)) {
            shimmerFrameLayout.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.TEST_ID, testId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.QUIZ_RANK, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            modelResponse = (RankResponse) Utils.getObject(response, RankResponse.class);
                            if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                setUpData(modelResponse.getUserResult());
                                if (modelResponse.getRanking() != null && modelResponse.getRanking().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_rank).setVisibility(View.GONE);
                                    recyclerView.setAdapter(new QuizRankAdapter(mActivity, modelResponse.getRanking()));
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_rank).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_rank).setVisibility(View.VISIBLE);
                            }
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            findViewById(R.id.tv_no_rank).setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_rank).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void setUpData(UserResult userResult) {
        findViewById(R.id.nested_scroll_view).setVisibility(View.VISIBLE);
        TextView tvMyRank = findViewById(R.id.tv_my_rank);
        TextView tvSubmitQuestion = findViewById(R.id.tv_submitted);
        TextView tvSkipQuestion = findViewById(R.id.tv_skip);
        TextView tvWrongQuestion = findViewById(R.id.tv_wrong);
        TextView tvPercentage = findViewById(R.id.tv_percent);
        ImageView ivQuizRank = findViewById(R.id.iv_rank_image);
        if (userResult != null) {
            tvMyRank.setText(getString(R.string.rank_format, "#", userResult.getRanking()));
            tvSubmitQuestion.setText(userResult.getCorrect());
            tvSkipQuestion.setText(userResult.getSkipped());
            tvWrongQuestion.setText(userResult.getWrong());
            tvPercentage.setText(getString(R.string.rank_format, userResult.getPercentage(), "%"));
            int percentage = (int) Double.parseDouble(userResult.getPercentage());
            if (percentage < 50)
                ivQuizRank.setBackgroundResource(R.drawable.rank_fail);
            else if (percentage < 80)
                ivQuizRank.setBackgroundResource(R.drawable.rank_middle);
            else ivQuizRank.setBackgroundResource(R.drawable.rank_middle);
        }
    }

    public void exitQuizPopup() {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            tvMessage.setText(getString(R.string.exit_quiz));
            CardView cvCancel = dialog.findViewById(R.id.cv_cancel);
            CardView cvOk = dialog.findViewById(R.id.cv_ok);


            cvCancel.setOnClickListener(v -> dialog.dismiss());
            cvOk.setOnClickListener(v -> {
                onBackPressed();
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToReAttempt() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Params.TEST_ID, testId);
        bundle.putString(Constants.Params.TEST_DURATION, testDuration);
        bundle.putString(Constants.Quiz.QUIZ_NAME, quizNameStr);
        bundle.putString(Constants.Quiz.FROM_WHERE, fromWhereStr == null ? "" : fromWhereStr);
        Utils.startActivityBundleFinish(mActivity, QuizQuestionAnsActivity.class, bundle);
    }

    private void goToViewSolution() {
        try {
            if (modelResponse.getUserResult() != null) {
                if (modelResponse.getUserResult().getQuestions() != null) {
                    Constants.AppSaveData.viewSolutionList = modelResponse.getUserResult().getQuestions();
                    Utils.startActivity(mActivity, ViewSolutionActivity.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_exit) {
            exitQuizPopup();
        } else if (view.getId() == R.id.tv_reattempt)
            goToReAttempt();
        else if (view.getId() == R.id.tv_view_solution)
            goToViewSolution();
    }
}