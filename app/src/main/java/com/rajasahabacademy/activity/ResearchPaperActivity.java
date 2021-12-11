package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CourseDetailPdfAdapter;
import com.rajasahabacademy.adapter.CourseDetailVideoAdapter;
import com.rajasahabacademy.adapter.ResearchPaperAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.course.course_subject.CourseSubjectResponse;
import com.rajasahabacademy.model.research_paper.ResearchPaperResponse;
import com.rajasahabacademy.support.Utils;

public class ResearchPaperActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_paper);
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
        setResearchPaperView();
    }

    private void setClickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        ImageView ivCart = findViewById(R.id.iv_research_cart);
        ivCart.setOnClickListener(this);
    }


    private void setResearchPaperView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_research_paper);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.RESEARCH_PAPER_LIST, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ResearchPaperResponse modelResponse = (ResearchPaperResponse) Utils.getObject(response, ResearchPaperResponse.class);
                            if (modelResponse != null && modelResponse.getResults().size() > 0){
                                recyclerView.setVisibility(View.VISIBLE);
                                findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                recyclerView.setAdapter(new ResearchPaperAdapter(mActivity,modelResponse.getResults()));
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back)
            onBackPressed();
        else if (view.getId() == R.id.iv_research_cart) {
            Utils.startActivity(mActivity, ResearchCartActivity.class);
        }
    }
}