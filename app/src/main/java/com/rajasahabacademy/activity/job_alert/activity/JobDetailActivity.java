package com.rajasahabacademy.activity.job_alert.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

public class JobDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
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
        setUpData();
    }


    private void setClickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }


    private void setUpData(){
        ImageView ivJobDetail = findViewById(R.id.iv_job_detail_image);
        TextView tvJobTitle = findViewById(R.id.tv_job_title);
        TextView tvJobDescription = findViewById(R.id.tv_job_description);
        Utils.setImageUsingGlide(mActivity, getIntent().getStringExtra(Constants.JobAlert.THUMBNAIL), ivJobDetail);
        tvJobTitle.setText(getIntent().getStringExtra(Constants.JobAlert.TITLE));
        Utils.setHtmlText(getIntent().getStringExtra(Constants.ResearchPaper.DESCRIPTION), tvJobDescription);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back)
            onBackPressed();
    }
}