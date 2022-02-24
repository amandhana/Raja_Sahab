package com.rajasahabacademy.activity.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.quiz.adapter.AttemptedQuizAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.quiz.model.attempt_quiz.AttemptedQuizResponse;
import com.rajasahabacademy.support.Utils;

public class QuizAttemptedActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    ShimmerFrameLayout attemptedQuizShimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_attempted);
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
        getAttemptedQuizList();
    }

    private void setClickListener() {
        attemptedQuizShimmer = findViewById(R.id.attempted_quiz_shimmer);
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
    }

    private void getAttemptedQuizList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)) {
            attemptedQuizShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.GET_ATTEMPTED_QUIZ, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    attemptedQuizShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            AttemptedQuizResponse modelResponse = (AttemptedQuizResponse) Utils.getObject(response, AttemptedQuizResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getMessage() != null) {
                                    if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        findViewById(R.id.tv_no_quiz_test).setVisibility(View.GONE);
                                        recyclerView.setAdapter(new AttemptedQuizAdapter(mActivity, modelResponse.getResults()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    attemptedQuizShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back)
            onBackPressed();
    }
}