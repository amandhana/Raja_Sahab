package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    Activity mActivity;
    TextView tvTotalAmount;
    TextView tvPaybleAmount;
    TextView tvOfferAmount;
    RelativeLayout couponAmountLay;
    TextView tvWallet;
    String courseId = "";
    String fromWhere = "";
    int paybleAmount;

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
        setUpData();
    }

    private void clickListener() {
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        tvPaybleAmount = findViewById(R.id.tv_payble_amount);
        tvOfferAmount = findViewById(R.id.tv_coupon_amount);
        couponAmountLay = findViewById(R.id.coupon_amount_lay);
        tvWallet = findViewById(R.id.tv_wallet);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        LinearLayout buyNowLay = findViewById(R.id.buy_now_lay);
        buyNowLay.setOnClickListener(this);
    }

    private void getIntentData() {
        courseId = getIntent().getStringExtra(Constants.Course.COURSE_ID);
        String totalAmountStr = getIntent().getStringExtra(Constants.Course.TOTAL_AMOUNT);
        fromWhere = getIntent().getStringExtra("from_where");
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

    private void setUpData() {
        tvWallet.setText(Utils.getSaveLoginUser(mActivity).getResults().getWallet());
    }

    public void applyOffer(String amount) {
        couponAmountLay.setVisibility(View.VISIBLE);
        double amountD = Double.parseDouble(amount);
        int offerAmount = (int) amountD;
        double totalAmountD = Double.parseDouble(tvTotalAmount.getText().toString());
        int totalAmount = (int) totalAmountD;
        int paybleAmount = totalAmount - offerAmount;
        tvOfferAmount.setText(String.valueOf(offerAmount));
        tvPaybleAmount.setText(String.valueOf(paybleAmount));
    }

    public void removeOffer() {
        tvPaybleAmount.setText(tvTotalAmount.getText().toString());
        couponAmountLay.setVisibility(View.GONE);
    }

    private int getPaybleAmount() {
        String amount = tvPaybleAmount.getText().toString();
        double amountD = Double.parseDouble(amount);

        return (int) amountD;
    }

    private void buyNowCourse(int paybleAmount, int remainWallet, String paymentType) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.COURSE_ID, courseId);
                params.put(Constants.Params.PRICE, paybleAmount);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                params.put(Constants.Params.TYPE, paymentType);
                params.put(Constants.Params.WALLET_AMOUNT, remainWallet);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.BUY_COURSE, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject object = new JSONObject(response);
                            if (object.optString("message").equalsIgnoreCase("ok")) {
                                Utils.getSaveLoginUser(mActivity).getResults().setWallet(String.valueOf(remainWallet));
                                Utils.showToastPopup(mActivity, getString(R.string.course_buy_success));
                                Intent intent = new Intent();
                                intent.putExtra(Constants.Course.WALLET_AMOUNT, tvWallet.getText().toString());
                                intent.putExtra(Constants.Course.TOTAL_AMOUNT, tvTotalAmount.getText().toString());
                                setResult(RESULT_OK, intent);
                                onBackPressed();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back)
            onBackPressed();
        else if (id == R.id.buy_now_lay) {
            int walletAmount = (int) Double.parseDouble(tvWallet.getText().toString());
            if (walletAmount == 0 || walletAmount < 0) {
                paybleAmount = getPaybleAmount();
                Utils.startPayment(mActivity, paybleAmount);
            } else if (walletAmount < getPaybleAmount()) {
                paybleAmount = getPaybleAmount() - walletAmount;
                Utils.startPayment(mActivity, paybleAmount);
            } else if (walletAmount == getPaybleAmount()) {
                int remainWalletAmount = walletAmount - getPaybleAmount();
                switch (fromWhere) {
                    case "course_detail":
                        buyNowCourse(getPaybleAmount(), remainWalletAmount, "wallet");
                        break;
                    case "cart":
                        Intent intent = new Intent();
                        intent.putExtra("remain_wallet", String.valueOf(remainWalletAmount));
                        intent.putExtra("payble_amount", String.valueOf(paybleAmount));
                        intent.putExtra("type", "wallet");
                        intent.putExtra("wallet_amount", String.valueOf(remainWalletAmount));
                        setResult(RESULT_OK, intent);
                        onBackPressed();
                        break;
                    case "research_cart":
                        intent = new Intent();
                        intent.putExtra("remain_wallet", String.valueOf(remainWalletAmount));
                        intent.putExtra("payble_amount", String.valueOf(paybleAmount));
                        intent.putExtra("type", "wallet");
                        intent.putExtra("wallet_amount", String.valueOf(remainWalletAmount));
                        setResult(RESULT_OK, intent);
                        onBackPressed();
                        break;
                }

            } else {
                int remainWalletAmount = walletAmount - getPaybleAmount();
                switch (fromWhere) {
                    case "course_detail":
                        buyNowCourse(getPaybleAmount(), remainWalletAmount, "wallet");
                        break;
                    case "cart":
                        Intent intent = new Intent();
                        intent.putExtra("remain_wallet", String.valueOf(remainWalletAmount));
                        intent.putExtra("payble_amount", String.valueOf(getPaybleAmount()));
                        intent.putExtra("type", "wallet");
                        setResult(RESULT_OK, intent);
                        onBackPressed();
                        break;
                    case "research_cart":
                        intent = new Intent();
                        intent.putExtra("remain_wallet", String.valueOf(remainWalletAmount));
                        intent.putExtra("payble_amount", String.valueOf(getPaybleAmount()));
                        intent.putExtra("type", "wallet");
                        setResult(RESULT_OK, intent);
                        onBackPressed();
                        break;
                }
            }

        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        switch (fromWhere) {
            case "course_detail":
                Log.e("success","course_detail");
                buyNowCourse(getPaybleAmount(), 0, "upi");
                break;
            case "cart":
                Log.e("success","cart");
                Intent intent = new Intent();
                intent.putExtra("remain_wallet", "0");
                intent.putExtra("payble_amount", String.valueOf(getPaybleAmount()));
                intent.putExtra("type", "upi");
                setResult(RESULT_OK, intent);
                onBackPressed();
                break;
            case "research_cart":
                Log.e("success","researc_cart");
                intent = new Intent();
                intent.putExtra("remain_wallet", "0");
                intent.putExtra("payble_amount", String.valueOf(getPaybleAmount()));
                intent.putExtra("type", "upi");
                setResult(RESULT_OK, intent);
                onBackPressed();
                break;
        }

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("error",s);
    }

}