package com.rajasahabacademy.activity.bookmark.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.bookmark.model.BookmarkQuestionResponse;
import com.rajasahabacademy.activity.bookmark.model.Datum;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class BookmarkActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    List<Datum> listQuestionAns;
    int questionNoIndex = 0;
    WebView tvOption1;
    WebView tvOption2;
    WebView tvOption3;
    WebView tvOption4;

    RelativeLayout selectOption1Lay;
    RelativeLayout selectOption2Lay;
    RelativeLayout selectOption3Lay;
    RelativeLayout selectOption4Lay;

    RelativeLayout unSelectOption1Lay;
    RelativeLayout unSelectOption2Lay;
    RelativeLayout unSelectOption3Lay;
    RelativeLayout unSelectOption4Lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_bookmark);
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
        getBookmarkQuestion();
    }

    private void clickListener() {
        tvOption1 = findViewById(R.id.web_view_option_1);
        tvOption2 = findViewById(R.id.web_view_option_2);
        tvOption3 = findViewById(R.id.web_view_option_3);
        tvOption4 = findViewById(R.id.web_view_option_4);

        selectOption1Lay = findViewById(R.id.select_option_1_lay);
        selectOption2Lay = findViewById(R.id.select_option_2_lay);
        selectOption3Lay = findViewById(R.id.select_option_3_lay);
        selectOption4Lay = findViewById(R.id.select_option_4_lay);

        unSelectOption1Lay = findViewById(R.id.unselect_option_1_lay);
        unSelectOption2Lay = findViewById(R.id.unselect_option_2_lay);
        unSelectOption3Lay = findViewById(R.id.unselect_option_3_lay);
        unSelectOption4Lay = findViewById(R.id.unselect_option_4_lay);

        CardView cvBack = findViewById(R.id.cv_back);
        cvBack.setOnClickListener(this);
        RelativeLayout previousLay = findViewById(R.id.previous_lay);
        previousLay.setOnClickListener(this);
        RelativeLayout nextLay = findViewById(R.id.next_lay);
        nextLay.setOnClickListener(this);
    }

    private void getBookmarkQuestion() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.GET_BOOKMARK_QUESTION, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            BookmarkQuestionResponse modelResponse = (BookmarkQuestionResponse) Utils.getObject(response, BookmarkQuestionResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getSuccess()) {
                                    if (modelResponse.getData().size() > 0) {
                                        findViewById(R.id.data_lay).setVisibility(View.VISIBLE);
                                        findViewById(R.id.tv_no_bookmark).setVisibility(View.GONE);
                                        listQuestionAns = modelResponse.getData();
                                        loadQuestion();
                                    } else {
                                        findViewById(R.id.data_lay).setVisibility(View.GONE);
                                        findViewById(R.id.tv_no_bookmark).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    findViewById(R.id.data_lay).setVisibility(View.GONE);
                                    findViewById(R.id.tv_no_bookmark).setVisibility(View.VISIBLE);
                                }
                            } else {
                                findViewById(R.id.data_lay).setVisibility(View.GONE);
                                findViewById(R.id.tv_no_bookmark).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        findViewById(R.id.data_lay).setVisibility(View.GONE);
                        findViewById(R.id.tv_no_bookmark).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    findViewById(R.id.data_lay).setVisibility(View.GONE);
                    findViewById(R.id.tv_no_bookmark).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void loadQuestion() {
        WebView webView = findViewById(R.id.web_view_question);
        webView.setBackgroundColor(Color.TRANSPARENT);
        tvOption1.setBackgroundColor(Color.TRANSPARENT);
        tvOption2.setBackgroundColor(Color.TRANSPARENT);
        tvOption3.setBackgroundColor(Color.TRANSPARENT);
        tvOption4.setBackgroundColor(Color.TRANSPARENT);

        String message = "<font color='black'>" + "<b>" + listQuestionAns.get(questionNoIndex).getQuestion() + "</b>" + "<font color='cyan'>" + "<font size='22'></font>";
        webView.loadData(message, "text/html", "utf8");

        String option1 = "<font color='black'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption1() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption1.loadData(option1, "text/html", "utf8");
        String option2 = "<font color='black'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption2() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption2.loadData(option2, "text/html", "utf8");
        String option3 = "<font color='black'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption3() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption3.loadData(option3, "text/html", "utf8");
        String option4 = "<font color='black'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption4() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption4.loadData(option4, "text/html", "utf8");

        switch (listQuestionAns.get(questionNoIndex).getAns()) {
            case "1":
                resetAllOption("option1");
                break;
            case "2":
                resetAllOption("option2");
                break;
            case "3":
                resetAllOption("option3");
                break;
            case "4":
                resetAllOption("option4");
                break;
        }
    }

    private void resetAllOption(String type) {
        selectOption1Lay.setVisibility(View.GONE);
        selectOption2Lay.setVisibility(View.GONE);
        selectOption3Lay.setVisibility(View.GONE);
        selectOption4Lay.setVisibility(View.GONE);

        unSelectOption1Lay.setVisibility(View.GONE);
        unSelectOption2Lay.setVisibility(View.GONE);
        unSelectOption3Lay.setVisibility(View.GONE);
        unSelectOption4Lay.setVisibility(View.GONE);

        switch (type) {
            case "option1":
                selectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                break;
            case "option2":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                selectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                break;
            case "option3":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                selectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                break;
            case "option4":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                selectOption4Lay.setVisibility(View.VISIBLE);
                break;
            case "unselect":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cv_back) {
            onBackPressed();
        } else if (id == R.id.previous_lay) {
            if (questionNoIndex > 0) {
                questionNoIndex--;
                loadQuestion();
            }
        } else if (id == R.id.next_lay) {
            questionNoIndex++;
            if (questionNoIndex < listQuestionAns.size())
                loadQuestion();
            else questionNoIndex = listQuestionAns.size() - 1;
        }
    }
}