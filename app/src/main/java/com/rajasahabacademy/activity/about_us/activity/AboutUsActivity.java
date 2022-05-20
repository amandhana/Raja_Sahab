package com.rajasahabacademy.activity.about_us.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.about_us.model.ContentResponse;
import com.rajasahabacademy.support.Utils;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_about_us);
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
        clickListener();
        getAboutUs();
    }

    private void clickListener() {
        shimmerFrameLayout = findViewById(R.id.about_us_shimmer);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void getAboutUs() {
        TextView tvAboutUs = findViewById(R.id.tv_about_us);
        if (Constants.AppSaveData.contentResponse == null) {
            if (Utils.isNetworkAvailable(mActivity)) {
                shimmerFrameLayout.startShimmer();
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
                communicator.post(101, mActivity, Constants.Apis.GET_CONTENT, params, new CustomResponseListener() {
                    @Override
                    public void onResponse(int requestCode, String response) {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        try {
                            if (response != null && !response.equals("")) {
                                ContentResponse modelResponse = (ContentResponse) Utils.getObject(response, ContentResponse.class);
                                if (modelResponse != null && modelResponse.getMessage() != null && modelResponse.getMessage().equalsIgnoreCase("ok")) {
                                    Constants.AppSaveData.contentResponse = modelResponse;
                                    if (modelResponse.getAboutUs() != null) {
                                        ((WebView)findViewById(R.id.webview)).loadData(modelResponse.getAboutUs(), "text/html", "utf8");
                                        Utils.setHtmlText(modelResponse.getAboutUs(), tvAboutUs);
                                    }
                                    else
                                        Utils.setHtmlText(getString(R.string.dummy_about_us), tvAboutUs);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error) {
                        shimmerFrameLayout.setVisibility(View.GONE);
                        Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                    }
                });
            } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            if (Constants.AppSaveData.contentResponse.getAboutUs() != null) {
                ((WebView)findViewById(R.id.webview)).loadData(Constants.AppSaveData.contentResponse.getAboutUs(), "text/html", "utf8");
                Utils.setHtmlText(Constants.AppSaveData.contentResponse.getAboutUs(), tvAboutUs);
            }
            else Utils.setHtmlText(getString(R.string.dummy_about_us), tvAboutUs);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }

}