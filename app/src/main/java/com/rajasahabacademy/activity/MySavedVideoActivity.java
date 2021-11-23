package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.YoutubeOfflineAdapter;
import com.rajasahabacademy.support.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MySavedVideoActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saved_video);
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
            recyclerView.setAdapter(new YoutubeOfflineAdapter(mActivity, getFiles()));
        } else {
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.tv_no_video).setVisibility(View.VISIBLE);
        }
    }

    private List<File> getFiles() {
        File cDir = getApplication().getExternalFilesDir(null);
        String path = cDir.getPath() + "/";
        File directory = new File(path);
        File[] files = directory.listFiles();
        assert files != null;
        return new ArrayList<>(Arrays.asList(files).subList(0, Objects.requireNonNull(files).length));
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