package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CartAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.cart.GetCartDataResponse;
import com.rajasahabacademy.model.cart.Result;
import com.rajasahabacademy.support.Utils;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    Activity mActivity;
    TextView tvBuyAll;
    List<Result> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_cart);
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
        setUpCartList();
    }

    private void clickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        tvBuyAll = findViewById(R.id.tv_buy_all);
        tvBuyAll.setOnClickListener(this);
    }

    private void setUpCartList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
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
            communicator.post(101, mActivity, Constants.Apis.GET_NOTE_CART, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            GetCartDataResponse modelResponse = (GetCartDataResponse) Utils.getObject(response, GetCartDataResponse.class);
                            if (modelResponse != null && modelResponse.getNotification().equalsIgnoreCase("Success")) {
                                if (modelResponse.getResults().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    tvBuyAll.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                    list = modelResponse.getResults();
                                    recyclerView.setAdapter(new CartAdapter(mActivity, list));
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    tvBuyAll.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                tvBuyAll.setVisibility(View.GONE);
                                findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        tvBuyAll.setVisibility(View.GONE);
                        findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, getString(R.string.quiz_list_failure));
                    recyclerView.setVisibility(View.GONE);
                    tvBuyAll.setVisibility(View.GONE);
                    findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private int getPaybleAmount() {
        double amount = 0;
        if (list != null) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    amount = amount + Double.parseDouble(list.get(i).getPrice());
                }
            }
        }
        return (int) amount;
    }

    private StringBuilder getOrderIds() {
        StringBuilder id = new StringBuilder();
        if (list != null) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (id.toString().equals(""))
                        id = new StringBuilder(list.get(i).getId());
                    else
                        id.append(",").append(list.get(i).getId());
                }
            }
        }
        return id;
    }

    public void removeCart(String noteId) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.NOTE_ID, noteId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REMOVE_NOTE_CART, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success"))
                                setUpCartList();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, getString(R.string.quiz_list_failure));
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void buyAllApi() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.NOTES, getOrderIds());
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                params.put(Constants.Params.PRICE, getPaybleAmount());
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ADD_NOTE_ORDER, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("notification").equalsIgnoreCase("Success")) {
                                Toast.makeText(mActivity, "Successfully buy all notes", Toast.LENGTH_SHORT).show();
                                setUpCartList();
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
    public void onPaymentSuccess(String s) {
        buyAllApi();
    }

    @Override
    public void onPaymentError(int i, String s) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        } else if (view.getId() == R.id.tv_buy_all)
            Utils.startPayment(mActivity, getPaybleAmount());
    }
}