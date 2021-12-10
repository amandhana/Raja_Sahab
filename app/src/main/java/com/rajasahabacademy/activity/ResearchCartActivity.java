package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.CartResearchPaperAdapter;

public class ResearchCartActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;

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
    }

    private void setClickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }

    private void setResearchPaperCart() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_cart_research_paper);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CartResearchPaperAdapter(mActivity));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back)
            onBackPressed();
    }
}