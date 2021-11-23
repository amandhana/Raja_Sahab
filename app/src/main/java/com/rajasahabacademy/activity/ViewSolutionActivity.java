package com.rajasahabacademy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.attempt_quiz.Question;

import java.util.List;

public class ViewSolutionActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    WebView tvOption1;
    WebView tvOption2;
    WebView tvOption3;
    WebView tvOption4;
    RelativeLayout selectOption1RightLay;
    RelativeLayout selectOption2RightLay;
    RelativeLayout selectOption3RightLay;
    RelativeLayout selectOption4RightLay;

    RelativeLayout selectOption1WrongLay;
    RelativeLayout selectOption2WrongLay;
    RelativeLayout selectOption3WrongLay;
    RelativeLayout selectOption4WrongLay;

    TextView tvCurrentQuestion;
    TextView tvTotalQuestion;

    List<Question> list;
    int questionNoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_solution);
        init();
    }

    private void init() {
        mActivity = this;
        getIntentData();
        clickListener();
    }

    private void getIntentData() {
        list = Constants.AppSaveData.viewSolutionList;
    }

    private void clickListener() {
        CardView cvmenu = findViewById(R.id.cv_back);
        cvmenu.setOnClickListener(this);

        tvCurrentQuestion = findViewById(R.id.tv_current_question);
        tvTotalQuestion = findViewById(R.id.tv_total_question);

        tvOption1 = findViewById(R.id.web_view_option_1);
        tvOption2 = findViewById(R.id.web_view_option_2);
        tvOption3 = findViewById(R.id.web_view_option_3);
        tvOption4 = findViewById(R.id.web_view_option_4);

        selectOption1RightLay = findViewById(R.id.select_option_1_right_lay);
        selectOption2RightLay = findViewById(R.id.select_option_2_right_lay);
        selectOption3RightLay = findViewById(R.id.select_option_3_right_lay);
        selectOption4RightLay = findViewById(R.id.select_option_4_right_lay);

        selectOption1WrongLay = findViewById(R.id.select_option_1_wrong_lay);
        selectOption2WrongLay = findViewById(R.id.select_option_2_wrong_lay);
        selectOption3WrongLay = findViewById(R.id.select_option_3_wrong_lay);
        selectOption4WrongLay = findViewById(R.id.select_option_4_wrong_lay);

        RelativeLayout previousLay = findViewById(R.id.previous_lay);
        RelativeLayout nextLay = findViewById(R.id.next_lay);
        previousLay.setOnClickListener(this);
        nextLay.setOnClickListener(this);

        tvTotalQuestion.setText(String.valueOf(list.size()));
        loadQuestion();
    }

    private void loadQuestion() {
        if (list != null && list.size() > 0) {
            WebView webView = findViewById(R.id.web_view_question);
            webView.setBackgroundColor(Color.TRANSPARENT);
            tvOption1.setBackgroundColor(Color.TRANSPARENT);
            tvOption2.setBackgroundColor(Color.TRANSPARENT);
            tvOption3.setBackgroundColor(Color.TRANSPARENT);
            tvOption4.setBackgroundColor(Color.TRANSPARENT);
            String message = "<font color='black'>" + "<b>" + list.get(questionNoIndex).getQuestion() + "</b>" + "<font color='cyan'>" + "<font size='22'></font>";
            webView.loadData(message, "text/html", "utf8");

            String option1 = "<font color='black'>" + "<b>" + list.get(questionNoIndex).getOption1() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
            tvOption1.loadData(option1, "text/html", "utf8");
            String option2 = "<font color='black'>" + "<b>" + list.get(questionNoIndex).getOption2() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
            tvOption2.loadData(option2, "text/html", "utf8");
            String option3 = "<font color='black'>" + "<b>" + list.get(questionNoIndex).getOption3() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
            tvOption3.loadData(option3, "text/html", "utf8");
            String option4 = "<font color='black'>" + "<b>" + list.get(questionNoIndex).getOption4() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
            tvOption4.loadData(option4, "text/html", "utf8");

            tvCurrentQuestion.setText(getCurrentQuestion());

            selectOption1RightLay.setVisibility(View.GONE);
            selectOption2RightLay.setVisibility(View.GONE);
            selectOption3RightLay.setVisibility(View.GONE);
            selectOption4RightLay.setVisibility(View.GONE);

            selectOption1WrongLay.setVisibility(View.GONE);
            selectOption2WrongLay.setVisibility(View.GONE);
            selectOption3WrongLay.setVisibility(View.GONE);
            selectOption4WrongLay.setVisibility(View.GONE);

            switch (list.get(questionNoIndex).getYourAnswer()) {
                case "1":
                    selectOption1WrongLay.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    selectOption2WrongLay.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    selectOption3WrongLay.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    selectOption4WrongLay.setVisibility(View.VISIBLE);
                    break;
            }
            switch (list.get(questionNoIndex).getCorrentAnswer()) {
                case "1":
                    selectOption1RightLay.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    selectOption2RightLay.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    selectOption3RightLay.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    selectOption4RightLay.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private String getCurrentQuestion() {
        int currentQuestion = questionNoIndex + 1;
        return String.valueOf(currentQuestion);
    }

    private void loadNextQuestion() {
        questionNoIndex++;
        if (questionNoIndex < list.size())
            loadQuestion();
        else
            questionNoIndex = list.size() - 1;
    }

    private void loadPreviousQuestion() {
        questionNoIndex--;
        if (questionNoIndex >= 0)
            loadQuestion();
        else
            questionNoIndex = 0;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cv_back) {
            onBackPressed();
        } else if (view.getId() == R.id.previous_lay)
            loadPreviousQuestion();
        else if (view.getId() == R.id.next_lay)
            loadNextQuestion();
    }
}