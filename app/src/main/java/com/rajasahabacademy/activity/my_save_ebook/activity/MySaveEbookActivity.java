package com.rajasahabacademy.activity.my_save_ebook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.my_save_ebook.adapter.EbookOfflineAdapter;
import com.rajasahabacademy.support.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MySaveEbookActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_save_ebook);
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        if (getFiles().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_no_video).setVisibility(View.GONE);
            recyclerView.setAdapter(new EbookOfflineAdapter(mActivity, getFiles()));
        } else {
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.tv_no_video).setVisibility(View.VISIBLE);
        }
    }
    private List<File> getFiles() {
        try {
            String path = Environment.getExternalStorageDirectory() + "/Ebook";
            File directory = new File(path);
            if (directory.exists()) {
                File[] files = directory.listFiles();
                return new ArrayList<>(Arrays.asList(Objects.requireNonNull(files)).subList(0, Objects.requireNonNull(files).length));
            }else return new ArrayList<>();
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private void clickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back)
            onBackPressed();
    }

}