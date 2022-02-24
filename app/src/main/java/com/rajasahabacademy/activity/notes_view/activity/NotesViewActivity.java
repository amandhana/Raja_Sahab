package com.rajasahabacademy.activity.notes_view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rajasahabacademy.R;
import com.rajasahabacademy.support.Utils;

public class NotesViewActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_notes_view);
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
    }

    private void setClickListener() {
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }

}