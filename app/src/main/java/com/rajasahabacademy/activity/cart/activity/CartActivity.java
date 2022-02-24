package com.rajasahabacademy.activity.cart.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.payment.activity.PaymentActivity;
import com.rajasahabacademy.activity.cart.adapter.CartAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.cart.model.GetCartDataResponse;
import com.rajasahabacademy.activity.cart.model.Result;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    TextView tvBuyAll;
    List<Result> list;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

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
        handleOnActivityResult();
    }

    private void clickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        tvBuyAll = findViewById(R.id.tv_buy_all);
        tvBuyAll.setOnClickListener(this);
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
                        buyAllApi(Integer.parseInt(paybleAmount),Integer.parseInt(remainWallet),type);
                    }
                });
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
                                    Constants.AppSaveData.homeCartCount = String.valueOf(list.size());
                                    recyclerView.setAdapter(new CartAdapter(mActivity, list));
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

    private void buyAllApi(int paybleAmount,int walletAmount,String paymentType) {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.NOTES, getOrderIds());
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
            communicator.post(101, mActivity, Constants.Apis.ADD_NOTE_ORDER, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("notification").equalsIgnoreCase("Success")) {
                                Toast.makeText(mActivity, "Successfully buy all notes", Toast.LENGTH_SHORT).show();
                                Utils.getSaveLoginUser(mActivity).getResults().setWallet(String.valueOf(walletAmount));
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

    private void startPayment() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(Constants.Course.TOTAL_AMOUNT, String.valueOf(getPaybleAmount()));
        intent.putExtra("from_where", "cart");
        intent.putExtra(Constants.Course.COURSE_ID, "");
        someActivityResultLauncher.launch(intent);
        /*try {
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
                    buyAllApi(getPaybleAmount(),remainWalletAmount,"wallet");
                }
                else{
                    int remainWalletAmount = walletAmount - getPaybleAmount();
                    buyAllApi(getPaybleAmount(),remainWalletAmount,"wallet");
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
        }*/

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        } else if (view.getId() == R.id.tv_buy_all)
            startPayment();
    }
}