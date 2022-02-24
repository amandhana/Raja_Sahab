package com.rajasahabacademy.activity.research_cart.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.pdf_view.PdfViewActivity;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

public class ResearchPaperDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    TextView tvBuyNow;
    TextView tvAddCart;
    String researchId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_paper_detail);
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
        ImageView ivImage = findViewById(R.id.iv_image);
        tvBuyNow = findViewById(R.id.tv_buy_now);
        tvAddCart = findViewById(R.id.tv_add_cart);
        researchId = getIntent().getStringExtra(Constants.ResearchPaper.RESEARCH_ID);
        Utils.setImageUsingGlide(mActivity, getIntent().getStringExtra(Constants.ResearchPaper.THUMBNAIL), ivImage);
        TextView tvDescription = findViewById(R.id.tv_research_paper_description);
        Utils.setHtmlText(getIntent().getStringExtra(Constants.ResearchPaper.DESCRIPTION), tvDescription);

        if (getIntent().getStringExtra(Constants.ResearchPaper.STATUS).equals("0")) {
            tvBuyNow.setText("Buy Now");
        } else {
            tvBuyNow.setText("View");
        }
        if (getIntent().getStringExtra(Constants.ResearchPaper.IS_CART).equals("1"))
            tvAddCart.setText("Remove Cart");
        else tvAddCart.setText("Add Cart");
        tvBuyNow.setOnClickListener(this);
        tvAddCart.setOnClickListener(this);
    }

    private void setClickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void buyNow() {
        if (tvBuyNow.getText().toString().equals("View")) {
            if (!getIntent().getStringExtra(Constants.ResearchPaper.PATH).equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Course.EBOOK_PATH, getIntent().getStringExtra(Constants.ResearchPaper.PATH));
                bundle.putString(Constants.Course.EBOOK_NAME, getIntent().getStringExtra(Constants.ResearchPaper.TITLE));
                bundle.putString(Constants.Course.FROM_WHERE, "");
                Utils.startActivityBundle(mActivity, PdfViewActivity.class, bundle);
            } else
                Utils.showToastPopup(mActivity, mActivity.getResources().getString(R.string.ebook_path_valid));
        }
    }

    public void addToCart() {
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
            communicator.post(101, mActivity, Constants.Apis.ADD_RESEARCH_CART, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("notification").equalsIgnoreCase("Success")) {
                                Toast.makeText(mActivity, "Add into cart successfully", Toast.LENGTH_SHORT).show();
                                tvAddCart.setText("Remove Cart");
                            }
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

    public void removeCart() {
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
                            if (jsonObject.optBoolean("success")) {
                                Toast.makeText(mActivity, "Remove from cart successfully", Toast.LENGTH_SHORT).show();
                                tvAddCart.setText("Add Cart");
                            }
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_buy_now)
            buyNow();
        else if (id == R.id.cv_back)
            onBackPressed();
        else if (id == R.id.tv_add_cart){
            if (tvAddCart.getText().toString().equals("Add Cart"))
                addToCart();
            else removeCart();
        }
    }
}