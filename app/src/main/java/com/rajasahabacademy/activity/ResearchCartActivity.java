package com.rajasahabacademy.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CartAdapter;
import com.rajasahabacademy.adapter.CartResearchPaperAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.cart.GetCartDataResponse;
import com.rajasahabacademy.model.research_paper.cart.ResearchCartResponse;
import com.rajasahabacademy.model.research_paper.cart.Result;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class ResearchCartActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    TextView tvBuyAll;
    List<Result> list;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_cart);
        init();
    }

    private void init() {
        mActivity = this;
        setClickListener();
        setResearchPaperCart();
        handleOnActivityResult();
    }

    private void setClickListener() {
        tvBuyAll = findViewById(R.id.tv_buy_all);
        tvBuyAll.setOnClickListener(this);
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void handleOnActivityResult() {
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String remainWallet = Objects.requireNonNull(data).getStringExtra("remain_wallet");
                        String paybleAmount = data.getStringExtra("payble_amount");
                        String type = data.getStringExtra("type");
                        buyAllApi(Integer.parseInt(paybleAmount), Integer.parseInt(remainWallet), type);
                    }
                });
    }


    private void setResearchPaperCart() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cart_research_paper);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
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
            communicator.post(101, mActivity, Constants.Apis.RESEARCH_CART_LIST, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ResearchCartResponse modelResponse = (ResearchCartResponse) Utils.getObject(response, ResearchCartResponse.class);
                            if (modelResponse != null && modelResponse.getNotification().equalsIgnoreCase("Success")) {
                                if (modelResponse.getResults().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    tvBuyAll.setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_no_data).setVisibility(View.GONE);
                                    list = modelResponse.getResults();
                                    recyclerView.setAdapter(new CartResearchPaperAdapter(mActivity, list));
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    tvBuyAll.setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
                                    Constants.AppSaveData.homeCartCount = "0";
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

    public void removeCart(String researchId) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.RESEARCH_ID, researchId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REMOVE_RESEARCH_CART, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success"))
                                setResearchPaperCart();
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

    private void startPayment() {
/*
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_wallet);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            RelativeLayout walletLay = dialog.findViewById(R.id.wallet_lay);
            TextView tvUpi = dialog.findViewById(R.id.tv_upi);
            TextView tvWallet = dialog.findViewById(R.id.tv_wallet);
            tvWallet.setText(Utils.getSaveLoginUser(mActivity).getResults().getWallet());

            int walletAmount = (int) Double.parseDouble(tvWallet.getText().toString());


            walletLay.setOnClickListener(v -> {
                dialog.dismiss();
                if (walletAmount == 0 || walletAmount < 0)
                    Toast.makeText(mActivity, "Wallet amount is 0", Toast.LENGTH_SHORT).show();
                else if (walletAmount < getPaybleAmount())
                    Toast.makeText(mActivity, "Wallet amount is less than payble amount", Toast.LENGTH_SHORT).show();
                else if (walletAmount == getPaybleAmount()) {
                    int remainWalletAmount = walletAmount - getPaybleAmount();
                    buyAllApi(getPaybleAmount(), remainWalletAmount, "wallet");
                } else {
                    int remainWalletAmount = walletAmount - getPaybleAmount();
                    buyAllApi(getPaybleAmount(), remainWalletAmount, "wallet");
                }
            });

            tvUpi.setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(Constants.Course.TOTAL_AMOUNT, String.valueOf(getPaybleAmount()));
                intent.putExtra(Constants.Course.WALLET_AMOUNT, String.valueOf(walletAmount));
                someActivityResultLauncher.launch(intent);
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

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

    private void buyAllApi(int paybleAmount, int walletAmount, String paymentType) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.RESEARCHS, getOrderIds());
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                params.put(Constants.Params.PRICE, paybleAmount);
                params.put(Constants.Params.TYPE, paymentType);
                params.put(Constants.Params.WALLET_AMOUNT, walletAmount);
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ADD_RESEARCH_ORDER, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("notification").equalsIgnoreCase("Success")) {
                                Toast.makeText(mActivity, "Successfully buy all notes", Toast.LENGTH_SHORT).show();
                                setResearchPaperCart();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back)
            onBackPressed();
        else if (view.getId() == R.id.tv_buy_all) {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(Constants.Course.TOTAL_AMOUNT, String.valueOf(getPaybleAmount()));
            intent.putExtra(Constants.Course.COURSE_ID, "");
            intent.putExtra("from_where", "research_cart");
            someActivityResultLauncher.launch(intent);
        }
    }
}