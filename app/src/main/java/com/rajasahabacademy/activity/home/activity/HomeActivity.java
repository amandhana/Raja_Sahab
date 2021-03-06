package com.rajasahabacademy.activity.home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.activity.BookmarkPdfActivity;
import com.rajasahabacademy.activity.job_alert.activity.JobAlertActivity;
import com.rajasahabacademy.activity.login.activity.LoginActivity;
import com.rajasahabacademy.activity.my_order.activity.MyOrdersActivity;
import com.rajasahabacademy.activity.my_save_ebook.activity.MySaveEbookActivity;
import com.rajasahabacademy.activity.my_save_video.activity.MySavedVideoActivity;
import com.rajasahabacademy.activity.notification.activity.NotificationActivity;
import com.rajasahabacademy.activity.privacy_policy.PrivacyPolicyActivity;
import com.rajasahabacademy.activity.profile.activity.ProfileActivity;
import com.rajasahabacademy.activity.quiz.activity.QuizAttemptedActivity;
import com.rajasahabacademy.activity.refund_cancel.RefundCancellationActivity;
import com.rajasahabacademy.activity.research_cart.activity.ResearchPaperActivity;
import com.rajasahabacademy.activity.short_video.activity.ShortVideoActivity;
import com.rajasahabacademy.activity.terms_condition.TermsConditionActivity;
import com.rajasahabacademy.activity.about_us.activity.AboutUsActivity;
import com.rajasahabacademy.activity.bookmark.activity.BookmarkActivity;
import com.rajasahabacademy.activity.bookmark.activity.BookmarkVideoActivity;
import com.rajasahabacademy.activity.cart.activity.CartActivity;
import com.rajasahabacademy.activity.chat.activity.ChatActivity;
import com.rajasahabacademy.activity.contact_us.ContactUsActivity;
import com.rajasahabacademy.activity.current_affair.activity.CurrentAffairActivity;
import com.rajasahabacademy.activity.home.adapter.HomeCourseAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.home.fragment.CourseFragment;
import com.rajasahabacademy.activity.home.fragment.HomeFragment;
import com.rajasahabacademy.activity.home.fragment.LiveClassFragment;
import com.rajasahabacademy.activity.quiz.fragment.LiveQuizFragment;
import com.rajasahabacademy.activity.home.fragment.NotesFragment;
import com.rajasahabacademy.activity.home.model.HomeCourseModel;
import com.rajasahabacademy.activity.course_detail.model.Result;
import com.rajasahabacademy.activity.home.model.category.Category;
import com.rajasahabacademy.activity.home.model.category.HomeCategoryResponse;
import com.rajasahabacademy.activity.home.model.latest_course.Course;
import com.rajasahabacademy.activity.home.model.latest_course.Video;
import com.rajasahabacademy.activity.home.model.notes.ResultNotes;
import com.rajasahabacademy.activity.quiz.model.ResultLiveQuiz;
import com.rajasahabacademy.databinding.ActivityHomeBinding;
import com.rajasahabacademy.support.HomeWatcher;
import com.rajasahabacademy.support.Preference;
import com.rajasahabacademy.support.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    DrawerLayout drawerLayout;
    List<HomeCourseModel> courseTypeList = new ArrayList<>();
    HomeCourseAdapter homeCourseAdapter;
    public String Tag = "";
    public String categoryId = "";
    boolean isFirstTime = true;
    Preference preference;
    ShimmerFrameLayout homeCategoryShimmer;
    private Timer timer;
    TextView tvReferralCode;
    EditText etSearch;
    RelativeLayout cartLay;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.darkThemeForceStop();
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpLeftMenuData();
        setBlankSearch();
    }

    private void init() {
        mActivity = this;
        preference = Preference.getInstance(mActivity);
        setClickListener();
        setHomeCourseAdapter();
        startTimer();
        applyFilter();
//        setHomePress();
        setWalletAmount();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof HomeFragment)
                    exitAppPopup();
                else if (fragment instanceof LiveClassFragment) {
                    getSupportFragmentManager().popBackStack(NotesFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
                } else if (fragment instanceof CourseFragment) {
                    getSupportFragmentManager().popBackStack(NotesFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
                } else if (fragment instanceof LiveQuizFragment) {
                    getSupportFragmentManager().popBackStack(NotesFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
                } else if (fragment instanceof NotesFragment) {
                    getSupportFragmentManager().popBackStack(NotesFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
                }
            }
        }
    }

    private void setBlankSearch() {
        etSearch.setText("");
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(() -> setHomeCourseAdapter());
            }
        };
        timer.schedule(timerTask, 5000, 5000);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void setClickListener() {
        RelativeLayout ivmenu = findViewById(R.id.iv_menu);
        ivmenu.setOnClickListener(this);
        CardView cvLogout = findViewById(R.id.cv_logout);
        cvLogout.setOnClickListener(this);
        RelativeLayout layoutNotification = findViewById(R.id.notification_lay);
        layoutNotification.setOnClickListener(this);
        drawerLayout = findViewById(R.id.drawer_Layout);

        etSearch = findViewById(R.id.et_search);

        binding.homeUnselctLay.setOnClickListener(this);
        binding.myCourseUnselctLay.setOnClickListener(this);
        binding.liveClassUnselctLay.setOnClickListener(this);
        binding.liveChatUnselctLay.setOnClickListener(this);
        binding.shortVideoUnselctLay.setOnClickListener(this);


        tvReferralCode = findViewById(R.id.tv_left_menu_ref_code);
        LinearLayout leftmenuhomelay = findViewById(R.id.left_menu_home_lay);
        leftmenuhomelay.setOnClickListener(this);
        LinearLayout leftMenuSavedVideolay = findViewById(R.id.left_menu_my_save_video_lay);
        leftMenuSavedVideolay.setOnClickListener(this);
        LinearLayout leftMenuSavedEbooklay = findViewById(R.id.left_menu_my_save_ebook_lay);
        leftMenuSavedEbooklay.setOnClickListener(this);
        LinearLayout leftmenuprofilelay = findViewById(R.id.left_menu_my_profile_lay);
        leftmenuprofilelay.setOnClickListener(this);
        LinearLayout leftmenuShortVideolay = findViewById(R.id.left_menu_short_video_lay);
        leftmenuShortVideolay.setOnClickListener(this);
        LinearLayout leftmenuResearchPaperlay = findViewById(R.id.left_menu_research_lay_lay);
        leftmenuResearchPaperlay.setOnClickListener(this);
        RelativeLayout leftmenubacklay = findViewById(R.id.back_lay_left_menu);
        leftmenubacklay.setOnClickListener(this);
        LinearLayout leftQuizlay = findViewById(R.id.left_menu_quiz_lay);
        leftQuizlay.setOnClickListener(this);
        LinearLayout leftCurrentAffairlay = findViewById(R.id.left_menu_current_affair_lay);
        leftCurrentAffairlay.setOnClickListener(this);
        LinearLayout leftMenuCourseLay = findViewById(R.id.left_menu_my_courses_lay);
        leftMenuCourseLay.setOnClickListener(this);
        LinearLayout leftMenuBookmarkPdfLay = findViewById(R.id.left_menu_bookmark_pdf_lay);
        leftMenuBookmarkPdfLay.setOnClickListener(this);
        LinearLayout leftMenuBookmarkLay = findViewById(R.id.left_menu_bookmark_lay);
        leftMenuBookmarkLay.setOnClickListener(this);
        LinearLayout leftMenuBookmarkVideoLay = findViewById(R.id.left_menu_bookmark_video_lay);
        leftMenuBookmarkVideoLay.setOnClickListener(this);
        LinearLayout leftMenuMyOrdersLay = findViewById(R.id.left_menu_my_orders_lay);
        leftMenuMyOrdersLay.setOnClickListener(this);
        LinearLayout leftMenuShareLay = findViewById(R.id.left_menu_share_lay);
        leftMenuShareLay.setOnClickListener(this);
        LinearLayout leftMenuAttemptedQuizLay = findViewById(R.id.left_menu_attempted_quiz_lay);
        leftMenuAttemptedQuizLay.setOnClickListener(this);
        LinearLayout leftMenuPrivacyPolicyLay = findViewById(R.id.left_privacy_policy_lay);
        leftMenuPrivacyPolicyLay.setOnClickListener(this);
        LinearLayout contactUsLay = findViewById(R.id.left_contact_us_lay);
        contactUsLay.setOnClickListener(this);
        LinearLayout notificationLay = findViewById(R.id.left_menu_notification_lay);
        notificationLay.setOnClickListener(this);
        LinearLayout rateusLay = findViewById(R.id.left_rate_us_lay);
        rateusLay.setOnClickListener(this);
        LinearLayout aboutUsLay = findViewById(R.id.left_about_us_lay);
        aboutUsLay.setOnClickListener(this);
        LinearLayout termsConditionLay = findViewById(R.id.left_terms_conditions_lay);
        termsConditionLay.setOnClickListener(this);
        LinearLayout refundCancellationLay = findViewById(R.id.left_refund_cancel_lay);
        refundCancellationLay.setOnClickListener(this);
        LinearLayout notesLay = findViewById(R.id.left_menu_notes_lay);
        notesLay.setOnClickListener(this);
        LinearLayout jobAlertLay = findViewById(R.id.left_menu_job_alert_lay);
        jobAlertLay.setOnClickListener(this);

        cartLay = findViewById(R.id.cart_lay);
        cartLay.setOnClickListener(this);

        homeCategoryShimmer = findViewById(R.id.home_category_shimmer);
    }

    private void setUpLeftMenuData() {
        try {
            TextView tvUserName = findViewById(R.id.tv_left_menu_username);
            TextView tvUserPhone = findViewById(R.id.tv_left_menu_phone_number);
            TextView tvFirstName = findViewById(R.id.tv_first_name);
            ImageView ivImage = findViewById(R.id.iv_left_menu_image);
            tvReferralCode.setText("Referral Code : " + Utils.getSaveLoginUser(mActivity).getResults().getRefCode());
            ivImage.setVisibility(View.GONE);
            tvFirstName.setVisibility(View.GONE);
            if (!Utils.getImage(mActivity).equals("") && Utils.getImage(mActivity).contains("http")) {
                ivImage.setVisibility(View.VISIBLE);
                Utils.setProfileImageUsingGlide(mActivity, Utils.getImage(mActivity), ivImage);
            } else {
                tvFirstName.setVisibility(View.VISIBLE);
                tvFirstName.setText(getFirstCharOfName());
            }
            String userNameStr = Utils.getSaveLoginUser(mActivity).getResults().getName();
            String upperString = "";
            if (!userNameStr.isEmpty() && userNameStr.length() > 1)
                upperString = userNameStr.substring(0, 1).toUpperCase() + userNameStr.substring(1).toLowerCase();
            else upperString = "R";
            tvUserName.setText(upperString);
            tvUserPhone.setText(Utils.getSaveLoginUser(mActivity).getResults().getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWalletAmount() {
        TextView tvWalletAmount = findViewById(R.id.tv_wallet_amount);
        tvWalletAmount.setText(getString(R.string.rank_format, "Wallet Amount : ", Utils.getSaveLoginUser(mActivity).getResults().getWallet()));
    }

    private String getFirstCharOfName() {
        if (Utils.getSaveLoginUser(mActivity).getResults().getName().equals(""))
            return "R";
        else {
            String name = Utils.getSaveLoginUser(mActivity).getResults().getName();
            return name.substring(0, 1);
        }
    }

    public void setHomeCourseAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        homeCourseAdapter = new HomeCourseAdapter(mActivity, courseTypeList);
        recyclerView.setAdapter(homeCourseAdapter);
        if (Utils.isNetworkAvailable(mActivity)) {
            homeCategoryShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.HOME_CATEGORY, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    homeCategoryShimmer.setVisibility(View.GONE);
                    stoptimertask();
                    try {
                        if (response != null && !response.equals("")) {
                            HomeCategoryResponse modelResponse = (HomeCategoryResponse) Utils.getObject(response, HomeCategoryResponse.class);
                            if (modelResponse != null && modelResponse.getStatus() != null && modelResponse.getStatus() == 1) {
                                if (modelResponse.getCategory() != null) {
                                    if (modelResponse.getCategory().size() > 0) {
                                        setCourseTypeList(modelResponse.getCategory());
                                        setSelectCat(true, 0, "0");
                                    } else {
                                        courseTypeList.clear();
                                        courseTypeList.add(new HomeCourseModel("", getString(R.string.all_category), "0", true));
                                        setSelectCat(true, 0, "0");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    homeCategoryShimmer.setVisibility(View.GONE);
                    Utils.showToastPopup(mActivity, error.getLocalizedMessage());
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void setCourseTypeList(List<Category> listCat) {
        courseTypeList.clear();
        courseTypeList.add(new HomeCourseModel("", getString(R.string.all_category), "0", true));
        for (int i = 0; i < listCat.size(); i++) {
            courseTypeList.add(new HomeCourseModel(listCat.get(i).getIcon(), listCat.get(i).getTitle(), listCat.get(i).getId(), false));
        }
    }

    public void setSelectCat(boolean allCatFlag, int position, String categoryId) {
        this.categoryId = categoryId;
        setBlankSearch();
        for (int i = 0; i < courseTypeList.size(); i++) {
            courseTypeList.get(i).setSelectCat(false);
        }
        courseTypeList.get(position).setSelectCat(true);
        courseTypeList.get(position).setAllCatFlag(allCatFlag);
        if (homeCourseAdapter != null)
            homeCourseAdapter.notifyDataSetChanged();
        switch (Tag) {
            case "com.rajasahabacademy.activity.home.fragment.CourseFragment":
                loadFragment(CourseFragment.class.getName(), CourseFragment.newInstance(), getHomeBundleData());
                break;
            case "com.rajasahabacademy.activity.quiz.fragment.LiveQuizFragment":
                loadFragment(LiveQuizFragment.class.getName(), LiveQuizFragment.newInstance(), getHomeBundleData());
                break;
            case "com.rajasahabacademy.activity.home.fragment.LiveClassFragment":
                loadFragment(LiveClassFragment.class.getName(), LiveClassFragment.newInstance(), getHomeBundleData());
                break;
            case "com.rajasahabacademy.activity.home.fragment.NotesFragment":
                loadFragment(NotesFragment.class.getName(), NotesFragment.newInstance(), getHomeBundleData());
                break;
            default:
                loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
                break;
        }
    }

    public void applyFilter() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                HomeFragment homeFragment = Constants.FragmentReference.homeFragment;
                CourseFragment courseFragment = Constants.FragmentReference.courseFragment;
                LiveQuizFragment liveQuizFragment = Constants.FragmentReference.liveQuizFragment;
                LiveClassFragment liveClassFragment = Constants.FragmentReference.liveClassFragment;
                NotesFragment notesFragment = Constants.FragmentReference.notesFragment;
                if (!s.toString().equals("")) {
                    switch (Tag) {
                        case "com.rajasahabacademy.activity.home.fragment.CourseFragment":
                            List<Result> courseFilterList = new ArrayList<>();
                            if (courseFragment.getCourseList() != null && courseFragment.getCourseList().size() > 0) {
                                for (Result model : courseFragment.getCourseList()) {
                                    if (model.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                                        courseFilterList.add(model);
                                }
                                courseFragment.setCourseFilterList(courseFilterList);
                            }
                            break;  // postman
                        case "com.rajasahabacademy.activity.quiz.fragment.LiveQuizFragment":
                            List<ResultLiveQuiz> liveQuizFilterList = new ArrayList<>();
                            if (liveQuizFragment.getLiveQuizList() != null && liveQuizFragment.getLiveQuizList().size() > 0) {
                                for (ResultLiveQuiz model : liveQuizFragment.getLiveQuizList()) {
                                    if (model.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                                        liveQuizFilterList.add(model);
                                }
                                liveQuizFragment.setCourseFilterList(liveQuizFilterList);
                            }
                            break;
                        case "com.rajasahabacademy.activity.home.fragment.LiveClassFragment":
                            break;
                        case "com.rajasahabacademy.activity.home.fragment.NotesFragment":
                            List<ResultNotes> notesFilterList = new ArrayList<>();
                            if (notesFragment.getList() != null && notesFragment.getList().size() > 0) {
                                for (ResultNotes model : notesFragment.getList()) {
                                    if (model.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                                        notesFilterList.add(model);
                                }
                                notesFragment.setCourseFilterList(notesFilterList);
                            }
                            break;
                        default:
                            List<Course> homeFilterCourseList = new ArrayList<>();
                            if (homeFragment.getHomeLatestCourseList() != null && homeFragment.getHomeLatestCourseList().size() > 0) {
                                for (Course model : homeFragment.getHomeLatestCourseList()) {
                                    if (model.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                                        homeFilterCourseList.add(model);
                                }
                                homeFragment.setCourseFilterList(homeFilterCourseList);
                            }
                            List<Video> homeFilterVideoList = new ArrayList<>();
                            if (homeFragment.getHomeLatestVideoList() != null && homeFragment.getHomeLatestVideoList().size() > 0) {
                                for (Video model : homeFragment.getHomeLatestVideoList()) {
                                    if (model.getTitle().toLowerCase().contains(s.toString().toLowerCase()))
                                        homeFilterVideoList.add(model);
                                }
                                homeFragment.setVideoFilterList(homeFilterVideoList);
                            }
                            break;
                    }
                } else {
                    switch (Tag) {
                        case "com.rajasahabacademy.activity.home.fragment.CourseFragment":
                            if (courseFragment != null)
                                courseFragment.setCourseFilterList(courseFragment.getCourseList());
                            break;
                        case "com.rajasahabacademy.activity.quiz.fragment.LiveQuizFragment":
                            if (liveQuizFragment != null)
                                liveQuizFragment.setCourseFilterList(liveQuizFragment.getLiveQuizList());
                            break;
                        case "com.rajasahabacademy.activity.home.fragment.LiveClassFragment":
                            break;
                        case "com.rajasahabacademy.activity.home.fragment.NotesFragment":
                            if (notesFragment != null)
                                notesFragment.setCourseFilterList(notesFragment.getList());
                            break;
                        default:
                            if (homeFragment != null) {
                                homeFragment.setCourseFilterList(homeFragment.getHomeLatestCourseList());
                                homeFragment.setVideoFilterList(homeFragment.getHomeLatestVideoList());
                            }
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setHomePress() {
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                Utils.hideKeyboard(mActivity);
                stoptimertask();
                finishAffinity();
            }

            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();
    }

    private void shareIntent() {
        String appLink = "Hey, \n Your Friend " + Utils.getSaveLoginUser(mActivity).getResults().getName()
                + " invited you to join Raja Sahab Group of Education . Use the invite code=" + Utils.getSaveLoginUser(mActivity).getResults().getRefCode()
                + " to sign up and earn ???50 . \n "
                + "App Link https://play.google.com/store/apps/details?id=" + mActivity.getPackageName();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appLink);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void showHideBottomNavigation(boolean flag) {
        if (flag) {
            binding.bottomNavigationLay.setVisibility(View.VISIBLE);
            binding.bottomLay.setVisibility(View.VISIBLE);
        } else {
            binding.bottomNavigationLay.setVisibility(View.GONE);
            binding.bottomLay.setVisibility(View.GONE);
        }
    }

    public void showHideCart(boolean flag) {
        if (flag)
            cartLay.setVisibility(View.VISIBLE);
        else cartLay.setVisibility(View.GONE);
    }

    public void showCartCount() {
        RelativeLayout cartCountLay = findViewById(R.id.cart_count_lay);
        TextView tvCartCount = findViewById(R.id.tv_cart_count);
        tvCartCount.setText(Constants.AppSaveData.homeCartCount);
        if (Constants.AppSaveData.homeCartCount.equalsIgnoreCase("0"))
            cartCountLay.setVisibility(View.GONE);
        else cartCountLay.setVisibility(View.VISIBLE);
    }

    public void exitAppPopup() {
        try {
            final Dialog dialog = new Dialog(mActivity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            tvMessage.setText(getString(R.string.exit_message));
            CardView cvCancel = dialog.findViewById(R.id.cv_cancel);
            CardView cvOk = dialog.findViewById(R.id.cv_ok);

            cvCancel.setOnClickListener(v -> dialog.dismiss());
            cvOk.setOnClickListener(v -> {
                Utils.hideKeyboard(mActivity);
                stoptimertask();
                finishAffinity();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logoutPopup() {
        try {
            final Dialog dialog = new Dialog(mActivity);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.toast_popup_exit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            TextView tvMessage = dialog.findViewById(R.id.tv_message);
            tvMessage.setText(getString(R.string.logout_message));
            CardView cvCancel = dialog.findViewById(R.id.cv_cancel);
            CardView cvOk = dialog.findViewById(R.id.cv_ok);


            cvCancel.setOnClickListener(v -> dialog.dismiss());
            cvOk.setOnClickListener(v -> {
                logoutApi();
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logoutApi() {
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
            communicator.post(101, mActivity, Constants.Apis.LOGOUT, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optString("message").equalsIgnoreCase("ok")) {
                            finishAffinity();
                            preference.clearPreference();
                            Utils.hideKeyboard(mActivity);
                            stoptimertask();
                            Utils.startActivity(mActivity, LoginActivity.class);
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

    public void loadFragment(String tag, Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (isFirstTime) {
            transaction.add(R.id.fragment_container, fragment);
        } else {
            transaction.replace(R.id.fragment_container, fragment);
        }
        if (!isFirstTime) {
            Tag = tag;
            transaction.addToBackStack(tag);
        } else {
            Tag = tag;
            isFirstTime = false;
        }

        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public void resetAllBottom(String type) {
        binding.homeCircleSelectLay.setVisibility(View.INVISIBLE);
        binding.myCourseCircleSelectLay.setVisibility(View.INVISIBLE);
        binding.liveClassCircleSelectLay.setVisibility(View.INVISIBLE);
        binding.shortVideoCircleSelectLay.setVisibility(View.INVISIBLE);
        binding.liveChatCircleSelectLay.setVisibility(View.INVISIBLE);

        binding.homeSelectLay.setVisibility(View.INVISIBLE);
        binding.myCourseSelectLay.setVisibility(View.INVISIBLE);
        binding.liveClassSelectLay.setVisibility(View.INVISIBLE);
        binding.shortVideoSelectLay.setVisibility(View.INVISIBLE);
        binding.liveChatSelectLay.setVisibility(View.INVISIBLE);

        binding.homeUnselctLay.setVisibility(View.VISIBLE);
        binding.myCourseUnselctLay.setVisibility(View.VISIBLE);
        binding.shortVideoUnselctLay.setVisibility(View.VISIBLE);
        binding.liveClassUnselctLay.setVisibility(View.VISIBLE);
        binding.liveChatUnselctLay.setVisibility(View.VISIBLE);
        /*ImageView ivMyOrders = findViewById(R.id.iv_my_orders);
        ImageView ivHome = findViewById(R.id.iv_home);
        ImageView ivLiveQuiz = findViewById(R.id.iv_live_quiz);
        ImageView ivLiveChat = findViewById(R.id.iv_live_chat);


        TextView tvMyOrders = findViewById(R.id.tv_my_orders);
        TextView tvHome = findViewById(R.id.tv_home);
        TextView tvLiveQuiz = findViewById(R.id.tv_live_quiz);
        TextView tvLiveChat = findViewById(R.id.tv_live_chat);

        ivLiveQuiz.setBackgroundResource(R.drawable.ic_home_live_quiz);
        ivMyOrders.setBackgroundTintList(getResources().getColorStateList(R.color.primary_text_color));
        ivLiveChat.setBackgroundTintList(getResources().getColorStateList(R.color.primary_text_color));
        ivHome.setBackgroundTintList(getResources().getColorStateList(R.color.primary_text_color));

//        homeLay.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryVariant));

        tvMyOrders.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvHome.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvLiveQuiz.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvLiveChat.setTextColor(getResources().getColor(R.color.primary_text_color));*/

        switch (type) {
            case "Home":
                binding.homeCircleSelectLay.setVisibility(View.VISIBLE);
                binding.homeSelectLay.setVisibility(View.VISIBLE);
                binding.homeUnselctLay.setVisibility(View.INVISIBLE);
                /*ivHome.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                tvHome.setTextColor(getResources().getColor(R.color.white));*/
                break;
            case "My Order":
                binding.myCourseCircleSelectLay.setVisibility(View.VISIBLE);
                binding.myCourseSelectLay.setVisibility(View.VISIBLE);
                binding.myCourseUnselctLay.setVisibility(View.INVISIBLE);
                /*ivMyOrders.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                tvMyOrders.setTextColor(getResources().getColor(R.color.white));*/
                break;
            case "Live Class":
                binding.liveClassCircleSelectLay.setVisibility(View.VISIBLE);
                binding.liveClassSelectLay.setVisibility(View.VISIBLE);
                binding.liveClassUnselctLay.setVisibility(View.INVISIBLE);
                /*ivLiveQuiz.setBackgroundResource(R.drawable.ic_home_live_quiz_active);
                tvLiveQuiz.setTextColor(getResources().getColor(R.color.white));*/
                break;
            case "Live Chat":
                binding.liveChatCircleSelectLay.setVisibility(View.VISIBLE);
                binding.liveChatSelectLay.setVisibility(View.VISIBLE);
                binding.liveChatUnselctLay.setVisibility(View.INVISIBLE);
                /*ivLiveChat.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                tvLiveChat.setTextColor(getResources().getColor(R.color.white));*/
                break;

            case "Short Video":
                binding.shortVideoCircleSelectLay.setVisibility(View.VISIBLE);
                binding.shortVideoSelectLay.setVisibility(View.VISIBLE);
                binding.shortVideoUnselctLay.setVisibility(View.INVISIBLE);
                /*ivLiveChat.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                tvLiveChat.setTextColor(getResources().getColor(R.color.white));*/
                break;
        }
    }

    public void performNavMenuAction() {
        Utils.hideKeyboard(this);
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }

    private void rateUs() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.rajasahabacademy"));
        startActivity(intent);
    }

    private void performMenuActionDelay() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(this::performNavMenuAction, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bundle getHomeBundleData() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Params.CATEGORY_ID, categoryId);
        return bundle;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_menu) {
            performNavMenuAction();
        } else if (id == R.id.back_lay_left_menu) {
            performNavMenuAction();
        } /*else if (id == R.id.live_classes_lay) {
            setBlankSearch();
            resetAllBottom("Live Classes");
            loadFragment(LiveClassFragment.class.getName(), LiveClassFragment.newInstance(), null);
        }*/ else if (id == R.id.home_unselct_lay) {
            setBlankSearch();
            resetAllBottom("Home");
            loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
        } else if (id == R.id.my_course_unselct_lay) {
            setBlankSearch();
            resetAllBottom("My Order");
            Utils.startActivity(mActivity, MyOrdersActivity.class);
        } else if (id == R.id.live_class_unselct_lay) {
            setBlankSearch();
            resetAllBottom("Live Class");
            loadFragment(LiveClassFragment.class.getName(), LiveClassFragment.newInstance(), null);
        } else if (id == R.id.live_chat_unselct_lay) {
            setBlankSearch();
            resetAllBottom("Live Chat");
            Utils.startActivity(mActivity, ChatActivity.class);
        } else if (id == R.id.short_video_unselct_lay) {
            setBlankSearch();
            resetAllBottom("Short Video");
            Utils.startActivity(mActivity, ShortVideoActivity.class);
        } else if (id == R.id.left_menu_home_lay) {
            setBlankSearch();
            performNavMenuAction();
            loadFragment(HomeFragment.class.getName(), HomeFragment.newInstance(), getHomeBundleData());
        } else if (id == R.id.left_menu_notes_lay) {
            setBlankSearch();
            performNavMenuAction();
            setHomeCourseAdapter();
            loadFragment(NotesFragment.class.getName(), NotesFragment.newInstance(), null);
        } else if (id == R.id.left_menu_quiz_lay) {
            setBlankSearch();
            performNavMenuAction();
            loadFragment(LiveQuizFragment.class.getName(), LiveQuizFragment.newInstance(), getHomeBundleData());
        } else if (id == R.id.left_menu_my_courses_lay) {
            setBlankSearch();
            performNavMenuAction();
            loadFragment(CourseFragment.class.getName(), CourseFragment.newInstance(), getHomeBundleData());
        } else if (id == R.id.left_menu_my_profile_lay) {
            setBlankSearch();
            performMenuActionDelay();
            Utils.startActivity(mActivity, ProfileActivity.class);
        } else if (id == R.id.left_menu_short_video_lay) {
            setBlankSearch();
            performMenuActionDelay();
            Utils.startActivity(mActivity, ShortVideoActivity.class);
        } else if (id == R.id.left_menu_my_orders_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, MyOrdersActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_menu_attempted_quiz_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, QuizAttemptedActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_menu_current_affair_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, CurrentAffairActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_privacy_policy_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, PrivacyPolicyActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_contact_us_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, ContactUsActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_menu_notification_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, NotificationActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_about_us_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, AboutUsActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_menu_share_lay) {
            shareIntent();
        } else if (id == R.id.left_menu_research_lay_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, ResearchPaperActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_menu_job_alert_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, JobAlertActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_terms_conditions_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, TermsConditionActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_refund_cancel_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, RefundCancellationActivity.class);
            performMenuActionDelay();
        } else if (id == R.id.left_rate_us_lay) {
            rateUs();
        } else if (id == R.id.cv_logout) {
            performNavMenuAction();
            logoutPopup();
        } else if (id == R.id.notification_lay) {
            setBlankSearch();
            Utils.startActivity(mActivity, NotificationActivity.class);
        } else if (id == R.id.left_menu_my_save_video_lay) {
            performMenuActionDelay();
            Utils.startActivity(mActivity, MySavedVideoActivity.class);
        } else if (id == R.id.left_menu_my_save_ebook_lay) {
            performMenuActionDelay();
            Utils.startActivity(mActivity, MySaveEbookActivity.class);
        } else if (id == R.id.left_menu_bookmark_lay) {
            performMenuActionDelay();
            Utils.startActivity(mActivity, BookmarkActivity.class);
        } else if (id == R.id.left_menu_bookmark_video_lay) {
            performMenuActionDelay();
            Utils.startActivity(mActivity, BookmarkVideoActivity.class);
        } else if (id == R.id.cart_lay) {
            performMenuActionDelay();
            Utils.startActivity(mActivity, CartActivity.class);
        } else if (id == R.id.left_menu_bookmark_pdf_lay) {
            performMenuActionDelay();
            Utils.startActivity(mActivity, BookmarkPdfActivity.class);
        }
    }
}