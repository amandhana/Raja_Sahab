package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;

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

    private void init() {
        mActivity = this;
        ImageView ivPdfLock = findViewById(R.id.iv_pdf_lock);
        ImageView ivImage = findViewById(R.id.iv_image);
        Utils.setImageUsingGlide(mActivity, getIntent().getStringExtra(Constants.ResearchPaper.THUMBNAIL), ivImage);
        tvBuyNow = findViewById(R.id.tv_buy_now);
        TextView tvDescription = findViewById(R.id.tv_research_paper_description);
        Utils.setHtmlText(getIntent().getStringExtra(Constants.ResearchPaper.DESCRIPTION), tvDescription);

        if (getIntent().getStringExtra(Constants.ResearchPaper.STATUS).equals("0")) {
            ivPdfLock.setVisibility(View.VISIBLE);
            tvBuyNow.setText("Buy Now");
        } else {
            ivPdfLock.setVisibility(View.GONE);
            tvBuyNow.setText("View");
        }
        tvBuyNow.setOnClickListener(this);
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
        if (id == R.id.tv_buy_now)
            buyNow();
    }
}