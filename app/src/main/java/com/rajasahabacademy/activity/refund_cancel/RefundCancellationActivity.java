package com.rajasahabacademy.activity.refund_cancel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.about_us.model.ContentResponse;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.support.Utils;

public class RefundCancellationActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_refund_cancellation);
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
        clickListener();
        getRefundCancellation();
    }

    private void clickListener() {
        shimmerFrameLayout = findViewById(R.id.about_us_shimmer);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void getRefundCancellation() {
        TextView tvName = findViewById(R.id.tv_refund_cancellation);
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
                                    if (modelResponse.getRefund() != null) {
                                        ((WebView)findViewById(R.id.webview)).loadData(modelResponse.getRefund(), "text/html", "utf8");
                                        Utils.setHtmlText(modelResponse.getRefund(), tvName);
                                    }
                                    else
                                        Utils.setHtmlText(getString(R.string.dummy_refund_policy), tvName);
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
            if (Constants.AppSaveData.contentResponse.getRefund() != null) {
                ((WebView)findViewById(R.id.webview)).loadData(Constants.AppSaveData.contentResponse.getRefund(), "text/html", "utf8");
                Utils.setHtmlText(Constants.AppSaveData.contentResponse.getRefund(), tvName);
            }
            else Utils.setHtmlText(getString(R.string.dummy_refund_policy), tvName);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }

}