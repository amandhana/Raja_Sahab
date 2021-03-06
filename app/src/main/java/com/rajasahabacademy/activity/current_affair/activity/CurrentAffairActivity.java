package com.rajasahabacademy.activity.current_affair.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.current_affair.adapter.CurrentAffairAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.current_affair.model.CurrentAffairResponse;
import com.rajasahabacademy.support.Utils;

public class CurrentAffairActivity extends AppCompatActivity implements View.OnClickListener{
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_affair);
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
        CurrentAffair();

    }

    private void setClickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void CurrentAffair() {
        RecyclerView recyclerView = findViewById(R.id.recycler_current_affairs);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)){
            Utils.hideKeyboard(mActivity);
            Utils.showProgressBar(mActivity);
            RequestParams params = new RequestParams();
            try{
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("Params", params.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101,mActivity, Constants.Apis.CURRENT_AFFAIR,params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try{
                        if (response != null){
                            CurrentAffairResponse modelResponse = (CurrentAffairResponse) Utils.getObject(response, CurrentAffairResponse.class);
                            if (modelResponse != null){
                                if (modelResponse.getNotification().equalsIgnoreCase("success")){
                                    if (modelResponse.getResults().size() > 0){
                                        recyclerView.setVisibility(View.VISIBLE);
                                        findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                        recyclerView.setAdapter(new CurrentAffairAdapter(mActivity,modelResponse.getResults()));
                                    }else{
                                        recyclerView.setVisibility(View.GONE);
                                        findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    recyclerView.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                }
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    }catch (Exception e){
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
        }else Utils.showToastPopup(mActivity,getString(R.string.internet_error));
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back)
            onBackPressed();
    }
}