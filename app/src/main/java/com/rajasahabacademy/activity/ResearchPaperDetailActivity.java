package com.rajasahabacademy.activity;

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

public class ResearchPaperDetailActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    TextView tvBuyNow;

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
        Utils.setImageUsingGlide(mActivity, getIntent().getStringExtra(Constants.ResearchPaper.THUMBNAIL), ivImage);
        tvBuyNow = findViewById(R.id.tv_buy_now);
        TextView tvDescription = findViewById(R.id.tv_research_paper_description);
        Utils.setHtmlText(getIntent().getStringExtra(Constants.ResearchPaper.DESCRIPTION), tvDescription);

        if (getIntent().getStringExtra(Constants.ResearchPaper.STATUS).equals("0")) {
            tvBuyNow.setText("Buy Now");
        } else {
            tvBuyNow.setText("View");
        }
        tvBuyNow.setOnClickListener(this);
    }

    private void setClickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        ImageView ivCart = findViewById(R.id.iv_research_cart);
        ivCart.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_buy_now) {
            buyNow();
        } else if (id == R.id.cv_back) {
            onBackPressed();
        }else if (id == R.id.iv_research_cart) {
            Utils.startActivity(mActivity, ResearchCartActivity.class);
        }
    }
}