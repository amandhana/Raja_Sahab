package com.rajasahabacademy.activity.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

public class QuizInstructionsActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    String testId = "";
    String testDuration = "";
    String fromWhereStr = "";
    ShimmerFrameLayout quizInstructionShimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_instructions);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fromWhereStr.equalsIgnoreCase(Constants.Quiz.FROM_WHERE_VALUE)) {
            finishAffinity();
            Utils.startActivityFinish(mActivity, HomeActivity.class);
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void init() {
        mActivity = this;
        getBundleData();
        setClickListener();
        getQuizInstruction();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            testId = bundle.getString(Constants.Params.TEST_ID);
            testDuration = bundle.getString(Constants.Params.TEST_DURATION);
            fromWhereStr = bundle.getString(Constants.Quiz.FROM_WHERE);
        }
    }

    private void setClickListener() {
        quizInstructionShimmer = findViewById(R.id.quiz_instruction_shimmer);
        RelativeLayout startTestLay = findViewById(R.id.start_test_lay);
        startTestLay.setOnClickListener(this);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void getQuizInstruction() {
        if (Utils.isNetworkAvailable(mActivity)) {
            quizInstructionShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.TEST_ID, testId);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.QUIZ_INSTRUCTION, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    quizInstructionShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject object = new JSONObject(response);
                            if (object.optString("message").equalsIgnoreCase("ok")) {
                                findViewById(R.id.data_lay).setVisibility(View.VISIBLE);
                                TextView tvQuizName = findViewById(R.id.tv_quiz_name);
                                WebView webView = findViewById(R.id.webview_instruction);
                                tvQuizName.setText(object.optString("title"));
                                String option1 = "<font color='black'>" + "<b>" + object.optString("description") + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
                                webView.setBackgroundColor(Color.TRANSPARENT);
                                webView.loadData(option1, "text/html", "utf8");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    quizInstructionShimmer.setVisibility(View.GONE);
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void goToQuestionAnsScreen() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Params.TEST_ID, testId);
        bundle.putString(Constants.Params.TEST_DURATION, testDuration);
        bundle.putString(Constants.Quiz.QUIZ_NAME, ((TextView) findViewById(R.id.tv_quiz_name)).getText().toString());
        bundle.putString(Constants.Quiz.FROM_WHERE, fromWhereStr == null ? "" : fromWhereStr);
        Utils.startActivityBundleFinish(mActivity, QuizQuestionAnsActivity.class, bundle);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start_test_lay)
            goToQuestionAnsScreen();
        else if (id == R.id.cv_back) {
            onBackPressed();
        }
    }
}