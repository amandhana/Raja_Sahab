package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CourseBookmarkVideoAdapter;
import com.rajasahabacademy.adapter.PaymenrOffersAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.bookmark.video.BookmarkVideoResponse;
import com.rajasahabacademy.model.offer.OfferResponse;
import com.rajasahabacademy.support.Utils;
import com.razorpay.PaymentResultListener;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    Activity mActivity;
    TextView tvTotalAmount;
    TextView tvPaybleAmount;
    String walletAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
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
        getIntentData();
        getOffers();
    }

    private void clickListener() {
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        tvPaybleAmount = findViewById(R.id.tv_payble_amount);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        LinearLayout buyNowLay = findViewById(R.id.buy_now_lay);
        buyNowLay.setOnClickListener(this);
    }

    private void getIntentData() {
        String totalAmountStr = getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT);
        if (getIntent().getStringExtra(Constants.Course.WALLET_AMOUNT) != null)
            walletAmount = getIntent().getStringExtra(Constants.Course.WALLET_AMOUNT);
        tvTotalAmount.setText(totalAmountStr);
        tvPaybleAmount.setText(totalAmountStr);
    }

    private void getOffers() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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
            communicator.post(101, mActivity, Constants.Apis.GET_COUPONS, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            OfferResponse modelResponse = (OfferResponse) Utils.getObject(response, OfferResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getSuccess()) {
                                    if (modelResponse.getData().size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        findViewById(R.id.tv_no_offer).setVisibility(View.GONE);
                                        recyclerView.setAdapter(new PaymenrOffersAdapter(mActivity, modelResponse.getData()));
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        findViewById(R.id.tv_no_offer).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_offer).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_offer).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_offer).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    recyclerView.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_offer).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    public void applyOffer(String amount) {
        double amountD = Double.parseDouble(amount);
        int offerAmount = (int) amountD;
        double totalAmountD = Double.parseDouble(tvTotalAmount.getText().toString());
        int totalAmount = (int) totalAmountD;
        int paybleAmount = totalAmount - offerAmount;
        tvPaybleAmount.setText(String.valueOf(paybleAmount));
    }

    public void removeOffer(){
        tvPaybleAmount.setText(tvTotalAmount.getText().toString());
    }

    private int getPaybleAmount() {
        String amount = tvPaybleAmount.getText().toString();
        double amountD = Double.parseDouble(amount);
        return (int) amountD;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back)
            onBackPressed();
        else if (id == R.id.buy_now_lay)
            Utils.startPayment(mActivity, getPaybleAmount());
    }

    @Override
    public void onPaymentSuccess(String s) {
        Intent intent = new Intent();
        intent.putExtra(Constants.Course.WALLET_AMOUNT,walletAmount);
        intent.putExtra(Constants.Course.TOTAL_AMOUNT,tvTotalAmount.getText().toString());
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}