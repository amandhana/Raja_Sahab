package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

public class QuizReadyForTestActivity extends AppCompatActivity {

    Activity mActivity;
    String testId = "";
    String testDuration = "";
    String fromWhereStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_quiz_ready_for_test);
        init();
    }

    @Override
    public void onBackPressed() {

    }

    private void init() {
        mActivity = this;
        getIntentData();
        if (fromWhereStr.equalsIgnoreCase(""))
            getBundleData();
        setCountDownTimer();
    }

    private void getIntentData() {
        if (getIntent().getStringExtra(Constants.Params.TEST_ID) != null) {
            fromWhereStr = getIntent().getStringExtra(Constants.Quiz.FROM_WHERE);
            testId = getIntent().getStringExtra(Constants.Params.TEST_ID);
            testDuration = getIntent().getStringExtra(Constants.Params.TEST_DURATION);
        }
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            testId = bundle.getString(Constants.Params.TEST_ID);
            testDuration = bundle.getString(Constants.Params.TEST_DURATION);
        }
    }

    private void setCountDownTimer() {
        TextView tvTimer = findViewById(R.id.tv_timer);
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText(getString(R.string.quiz_ready_timer, millisUntilFinished / 1000, " Sec"));
            }

            public void onFinish() {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Params.TEST_ID, testId);
                bundle.putString(Constants.Params.TEST_DURATION, testDuration);
                bundle.putString(Constants.Quiz.FROM_WHERE, fromWhereStr == null ? "" : fromWhereStr);
                Utils.startActivityBundleFinish(mActivity, QuizInstructionsActivity.class, bundle);
            }

        }.start();
    }

}