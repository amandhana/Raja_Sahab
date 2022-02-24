package com.rajasahabacademy.activity.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rajasahabacademy.R;
import com.rajasahabacademy.support.Utils;

public class QuizAccToSubjectListActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_live_quiz_acc_to_subject_list);
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
    }

    private void clickListener() {
        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        }
    }

}