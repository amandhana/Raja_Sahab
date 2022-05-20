package com.rajasahabacademy.activity.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.quiz.adapter.QuestionNoAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.quiz.model.question_ans.QuizQuestionAnsResponse;
import com.rajasahabacademy.activity.quiz.model.question_ans.Result;
import com.rajasahabacademy.support.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizQuestionAnsActivity extends AppCompatActivity implements View.OnClickListener {

    Activity mActivity;
    ShimmerFrameLayout questionAnsShimmer;
    String testId = "";
    String testDuration = "";
    String quizNameStr = "";
    String fromWhereStr = "";
    List<Result> listQuestionAns;
    int questionNoIndex = 0;
    TextView tvCurrentQuestion;
    TextView tvTotalQuestion;
    TextView tvTimer;
    WebView tvOption1;
    WebView tvOption2;
    WebView tvOption3;
    WebView tvOption4;
    TextView tvNext;
    int totalSeconds;
    CountDownTimer countDownTimer;
    int secondsRemaining;
    RelativeLayout selectOption1Lay;
    RelativeLayout selectOption2Lay;
    RelativeLayout selectOption3Lay;
    RelativeLayout selectOption4Lay;

    RelativeLayout unSelectOption1Lay;
    RelativeLayout unSelectOption2Lay;
    RelativeLayout unSelectOption3Lay;
    RelativeLayout unSelectOption4Lay;

    ProgressBar progressBar;
    DrawerLayout drawerLayout;
    QuestionNoAdapter questionNoAdapter;
    RelativeLayout menuLay;

    ImageView ivBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        setContentView(R.layout.activity_quiz_question_ans);
        init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    public void onBackPressed() {
        submitTestPopUp();
    }

    private void init() {
        mActivity = this;
        clickListener();
        getBundleData();
        getQuestionAns();
    }

    private void clickListener() {
        questionAnsShimmer = findViewById(R.id.question_ans_shimmer);
        tvCurrentQuestion = findViewById(R.id.tv_current_question);
        tvTotalQuestion = findViewById(R.id.tv_total_question);
        tvTimer = findViewById(R.id.tv_timer);
        drawerLayout = findViewById(R.id.question_no_drawer_Layout);
        tvOption1 = findViewById(R.id.web_view_option_1);
        tvOption2 = findViewById(R.id.web_view_option_2);
        tvOption3 = findViewById(R.id.web_view_option_3);
        tvOption4 = findViewById(R.id.web_view_option_4);
        tvNext = findViewById(R.id.tv_next);
        progressBar = findViewById(R.id.progressBar);

        selectOption1Lay = findViewById(R.id.select_option_1_lay);
        selectOption2Lay = findViewById(R.id.select_option_2_lay);
        selectOption3Lay = findViewById(R.id.select_option_3_lay);
        selectOption4Lay = findViewById(R.id.select_option_4_lay);

        unSelectOption1Lay = findViewById(R.id.unselect_option_1_lay);
        unSelectOption2Lay = findViewById(R.id.unselect_option_2_lay);
        unSelectOption3Lay = findViewById(R.id.unselect_option_3_lay);
        unSelectOption4Lay = findViewById(R.id.unselect_option_4_lay);

        RelativeLayout option1Lay = findViewById(R.id.option_1_lay);
        RelativeLayout option2Lay = findViewById(R.id.option_2_lay);
        RelativeLayout option3Lay = findViewById(R.id.option_3_lay);
        RelativeLayout option4Lay = findViewById(R.id.option_4_lay);

        option1Lay.setOnClickListener(this);
        option2Lay.setOnClickListener(this);
        option3Lay.setOnClickListener(this);
        option4Lay.setOnClickListener(this);

        TextView tvExit = findViewById(R.id.tv_exit);
        RelativeLayout skipLay = findViewById(R.id.skip_lay);
        tvExit.setOnClickListener(this);
        skipLay.setOnClickListener(this);
        RelativeLayout nextLay = findViewById(R.id.next_lay);
        RelativeLayout submitLay = findViewById(R.id.submit_lay);
        nextLay.setOnClickListener(this);
        submitLay.setOnClickListener(this);
        RelativeLayout progressBarLay = findViewById(R.id.progress_lay);
        progressBarLay.setOnClickListener(this);
        menuLay = findViewById(R.id.menu_lay);
        menuLay.setOnClickListener(this);
        ivBookmark = findViewById(R.id.iv_bookmark);
        ivBookmark.setOnClickListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            testId = bundle.getString(Constants.Params.TEST_ID);
            testDuration = bundle.getString(Constants.Params.TEST_DURATION);
            quizNameStr = bundle.getString(Constants.Quiz.QUIZ_NAME);
            fromWhereStr = bundle.getString(Constants.Quiz.FROM_WHERE);
        }
    }

    private void getQuestionAns() {
        if (Utils.isNetworkAvailable(mActivity)) {
            questionAnsShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.TEST_ID, testId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.QUIZ_QUESTION_ANS, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        questionAnsShimmer.setVisibility(View.GONE);
                        findViewById(R.id.data_lay).setVisibility(View.VISIBLE);
                    }, 1500);
                    try {
                        if (response != null && !response.equals("")) {
                            QuizQuestionAnsResponse modelResponse = (QuizQuestionAnsResponse) Utils.getObject(response, QuizQuestionAnsResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    listQuestionAns = modelResponse.getResults();
                                    tvCurrentQuestion.setText(getCurrentQuestion());
                                    tvTotalQuestion.setText(String.valueOf(listQuestionAns.size()));
                                    listQuestionAns.get(questionNoIndex).setVisitedFlag(true);
                                    long millis = Integer.parseInt(testDuration) * 60 * 1000;
                                    timerStart(millis);
                                    loadQuestion();
                                    setQuestionNoLayout();
                                } else {
                                    Toast.makeText(mActivity, getString(R.string.no_question), Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.showToastPopup(mActivity, getString(R.string.quiz_list_failure));
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private String getCurrentQuestion() {
        int currentQuestion = questionNoIndex + 1;
        return String.valueOf(currentQuestion);
    }

    private void timerStart(long millSecond) {
        try {
            final float numberOfSeconds = millSecond / 1000;
            totalSeconds = (int) numberOfSeconds;
            final float factor = 100 / numberOfSeconds;
            countDownTimer = new CountDownTimer(millSecond, 1000) {
                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText(getString(R.string.quiz_question_ans_timer_format, TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                            , TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    secondsRemaining = (int) (millisUntilFinished / 1000);
                    float progressPercentage = (numberOfSeconds - secondsRemaining) * factor;
                    progressBar.setProgress((int) progressPercentage);
                }

                public void onFinish() {
                    progressBar.setProgress(100);
                    menuLay.setEnabled(false);
                    findViewById(R.id.skip_lay).setVisibility(View.GONE);
                    findViewById(R.id.next_lay).setVisibility(View.GONE);
                    findViewById(R.id.option_1_lay).setEnabled(false);
                    findViewById(R.id.option_2_lay).setEnabled(false);
                    findViewById(R.id.option_3_lay).setEnabled(false);
                    findViewById(R.id.option_4_lay).setEnabled(false);
                    submitTestPopUp();
                }

            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQuestion() {
        WebView webView = findViewById(R.id.web_view_question);
        webView.setBackgroundColor(Color.TRANSPARENT);
        tvOption1.setBackgroundColor(Color.TRANSPARENT);
        tvOption2.setBackgroundColor(Color.TRANSPARENT);
        tvOption3.setBackgroundColor(Color.TRANSPARENT);
        tvOption4.setBackgroundColor(Color.TRANSPARENT);

        if (listQuestionAns.get(questionNoIndex).getBookmark().equals("1"))
            ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill);
        else ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_empty);

        String message = "<font color='white'>" + "<b>" + listQuestionAns.get(questionNoIndex).getQuestion() + "</b>" + "<font color='cyan'>" + "<font size='22'></font>";
        webView.loadData(message, "text/html", "utf8");

        String option1 = "<font color='white'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption1() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption1.loadData(option1, "text/html", "utf8");
        String option2 = "<font color='white'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption2() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption2.loadData(option2, "text/html", "utf8");
        String option3 = "<font color='white'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption3() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption3.loadData(option3, "text/html", "utf8");
        String option4 = "<font color='white'>" + "<b>" + listQuestionAns.get(questionNoIndex).getOption4() + "</b>" + "<font color='cyan'>" + "<font size='16'></font>";
        tvOption4.loadData(option4, "text/html", "utf8");

        tvCurrentQuestion.setText(getCurrentQuestion());

        selectOption1Lay.setVisibility(View.GONE);
        selectOption2Lay.setVisibility(View.GONE);
        selectOption3Lay.setVisibility(View.GONE);
        selectOption4Lay.setVisibility(View.GONE);

        unSelectOption1Lay.setVisibility(View.VISIBLE);
        unSelectOption2Lay.setVisibility(View.VISIBLE);
        unSelectOption3Lay.setVisibility(View.VISIBLE);
        unSelectOption4Lay.setVisibility(View.VISIBLE);

        switch (listQuestionAns.get(questionNoIndex).getSelectedOption()) {
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

    private void setQuestionNoLayout() {
        TextView tvQuizName = findViewById(R.id.tv_quiz_name);
        TextView tvQuizNameMenu = findViewById(R.id.tv_quiz_name_menu);
        TextView tvQuestionCount = findViewById(R.id.tv_question_count);
        tvQuizNameMenu.setText(getString(R.string.rank_format, getString(R.string.question_label), quizNameStr));
        tvQuizName.setText(quizNameStr);
        tvQuestionCount.setText(String.valueOf(listQuestionAns.size()));
        RecyclerView recyclerView = findViewById(R.id.question_no_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 5, RecyclerView.VERTICAL, false));
        questionNoAdapter = new QuestionNoAdapter(mActivity, listQuestionAns);
        recyclerView.setAdapter(questionNoAdapter);
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
                int timetakenInSeconds = totalSeconds - secondsRemaining;
                listQuestionAns.get(questionNoIndex).setTimeTakenInSeconds(timetakenInSeconds);
                listQuestionAns.get(questionNoIndex).setSelectedOption("1");
                listQuestionAns.get(questionNoIndex).setSelectFlag(true);
                break;
            case "option2":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                selectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                timetakenInSeconds = totalSeconds - secondsRemaining;
                listQuestionAns.get(questionNoIndex).setTimeTakenInSeconds(timetakenInSeconds);
                listQuestionAns.get(questionNoIndex).setSelectedOption("2");
                listQuestionAns.get(questionNoIndex).setSelectFlag(true);
                break;
            case "option3":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                selectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                timetakenInSeconds = totalSeconds - secondsRemaining;
                listQuestionAns.get(questionNoIndex).setTimeTakenInSeconds(timetakenInSeconds);
                listQuestionAns.get(questionNoIndex).setSelectedOption("3");
                listQuestionAns.get(questionNoIndex).setSelectFlag(true);
                break;
            case "option4":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                selectOption4Lay.setVisibility(View.VISIBLE);
                timetakenInSeconds = totalSeconds - secondsRemaining;
                listQuestionAns.get(questionNoIndex).setTimeTakenInSeconds(timetakenInSeconds);
                listQuestionAns.get(questionNoIndex).setSelectedOption("4");
                listQuestionAns.get(questionNoIndex).setSelectFlag(true);
                break;
            case "unselect":
                unSelectOption1Lay.setVisibility(View.VISIBLE);
                unSelectOption2Lay.setVisibility(View.VISIBLE);
                unSelectOption3Lay.setVisibility(View.VISIBLE);
                unSelectOption4Lay.setVisibility(View.VISIBLE);
                listQuestionAns.get(questionNoIndex).setSelectedOption("");
                break;
        }
        if (questionNoAdapter != null)
            questionNoAdapter.notifyDataSetChanged();
    }

    public void exitQuizPopup() {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            tvMessage.setText(getString(R.string.exit_quiz));
            CardView cvCancel = dialog.findViewById(R.id.cv_cancel);
            CardView cvOk = dialog.findViewById(R.id.cv_ok);


            cvCancel.setOnClickListener(v -> dialog.dismiss());
            cvOk.setOnClickListener(v -> {
                if (fromWhereStr.equalsIgnoreCase(Constants.Quiz.FROM_WHERE_VALUE)) {
                    dialog.dismiss();
                    finishAffinity();
                    Utils.startActivityFinish(mActivity, HomeActivity.class);
                } else {
                    dialog.dismiss();
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitTestPopUp() {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            tvMessage.setText(getString(R.string.submit_test_message));
            CardView cvCancel = dialog.findViewById(R.id.cv_cancel);
            CardView cvOk = dialog.findViewById(R.id.cv_ok);


            cvCancel.setOnClickListener(v -> dialog.dismiss());
            cvOk.setOnClickListener(v -> {
                dialog.dismiss();
                submitTest();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitTest() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < listQuestionAns.size(); i++) {
                    JSONObject object = new JSONObject();
                    object.put(Constants.Params.QUESTION_ID, listQuestionAns.get(i).getId());
                    object.put(Constants.Params.OPTION, listQuestionAns.get(i).getSelectedOption());
                    object.put(Constants.Params.ANSWERED, listQuestionAns.get(i).getSelectFlag());
                    object.put(Constants.Params.TIME_TAKEN, listQuestionAns.get(i).getTimeTakenInSeconds());
                    jsonArray.put(object);
                }
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.TEST_ID, testId);
                params.put(Constants.Params.QUESTION_ANS_DATA, jsonArray);
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.SUBMIT_TEST, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("message").equalsIgnoreCase("ok")) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.Params.TEST_ID, testId);
                            bundle.putString(Constants.Params.TEST_DURATION, testDuration);
                            bundle.putString(Constants.Quiz.QUIZ_NAME, quizNameStr);
                            bundle.putString(Constants.Quiz.FROM_WHERE, fromWhereStr);
                            Utils.startActivityBundleFinish(mActivity, QuizRankActivity.class, bundle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    Utils.hideProgressBar();
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    public void loadNextQuestion(String type, int position) {
        totalSeconds = secondsRemaining;
        if (type.equalsIgnoreCase("skip")) {
            listQuestionAns.get(questionNoIndex).setSkipFlag(true);
        }
        if (!type.equalsIgnoreCase("manual"))
            questionNoIndex++;
        else
            questionNoIndex = position;
        listQuestionAns.get(questionNoIndex).setVisitedFlag(true);
        if (questionNoIndex < listQuestionAns.size()) {
            loadQuestion();
            if (questionNoIndex == listQuestionAns.size() - 1) {
                findViewById(R.id.skip_lay).setVisibility(View.GONE);
                findViewById(R.id.next_lay).setVisibility(View.GONE);
            } else {
                findViewById(R.id.skip_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.next_lay).setVisibility(View.VISIBLE);
            }
        } else {
            questionNoIndex = listQuestionAns.size() - 1;
            findViewById(R.id.skip_lay).setVisibility(View.GONE);
            findViewById(R.id.next_lay).setVisibility(View.GONE);
        }
        if (questionNoAdapter != null)
            questionNoAdapter.notifyDataSetChanged();
    }

    public void performNavMenuAction() {
        Utils.hideKeyboard(this);
        if (drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawers();
        else
            drawerLayout.openDrawer(GravityCompat.END);
    }

    private void addBookmark() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.QUESTION_ID, listQuestionAns.get(questionNoIndex).getId());
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.ADD_BOOKMARK_QUESTION, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_fill);
                                listQuestionAns.get(questionNoIndex).setBookmark("1");
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

    private void removeBookmark() {
        if (Utils.isNetworkAvailable(mActivity)) {
            Utils.showProgressBar(mActivity);
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.QUESTION_ID, listQuestionAns.get(questionNoIndex).getId());
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.REMOVE_BOOKMARK_QUESTION, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optBoolean("success")) {
                                ivBookmark.setBackgroundResource(R.drawable.ic_bookmark_empty);
                                listQuestionAns.get(questionNoIndex).setBookmark("0");
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
        if (id == R.id.option_1_lay) {
            if (listQuestionAns.get(questionNoIndex).getSelectedOption().equalsIgnoreCase(""))
                resetAllOption("option1");
            else {
                listQuestionAns.get(questionNoIndex).setSelectedOption("");
                listQuestionAns.get(questionNoIndex).setSkipFlag(false);
                resetAllOption("unselect");
            }
        } else if (id == R.id.option_2_lay) {
            if (listQuestionAns.get(questionNoIndex).getSelectedOption().equalsIgnoreCase(""))
                resetAllOption("option2");
            else {
                listQuestionAns.get(questionNoIndex).setSelectedOption("");
                listQuestionAns.get(questionNoIndex).setSkipFlag(false);
                resetAllOption("unselect");
            }
        } else if (id == R.id.option_3_lay) {
            if (listQuestionAns.get(questionNoIndex).getSelectedOption().equalsIgnoreCase(""))
                resetAllOption("option3");
            else {
                listQuestionAns.get(questionNoIndex).setSelectedOption("");
                listQuestionAns.get(questionNoIndex).setSkipFlag(false);
                resetAllOption("unselect");
            }
        } else if (id == R.id.option_4_lay) {
            if (listQuestionAns.get(questionNoIndex).getSelectedOption().equalsIgnoreCase(""))
                resetAllOption("option4");
            else {
                listQuestionAns.get(questionNoIndex).setSelectedOption("");
                listQuestionAns.get(questionNoIndex).setSkipFlag(false);
                resetAllOption("unselect");
            }
        } else if (id == R.id.tv_exit) {
            exitQuizPopup();
        } else if (id == R.id.next_lay)
            loadNextQuestion("next", 0);
        else if (id == R.id.submit_lay)
            submitTestPopUp();
        else if (id == R.id.skip_lay)
            loadNextQuestion("skip", 0);
        else if (id == R.id.progress_lay)
            exitQuizPopup();
        else if (id == R.id.menu_lay)
            performNavMenuAction();
        else if (id == R.id.iv_bookmark) {
            if (!listQuestionAns.get(questionNoIndex).getBookmark().equals("1"))
                addBookmark();
            else removeBookmark();
        }
    }
}