package com.rajasahabacademy.activity.home.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.home.adapter.HomeLatestCourseAdapter;
import com.rajasahabacademy.activity.home.adapter.HomeLatestVideoAdapter;
import com.rajasahabacademy.activity.home.adapter.HomeSliderAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.home.model.HomeSliderResponse;
import com.rajasahabacademy.activity.home.model.latest_course.Course;
import com.rajasahabacademy.activity.home.model.latest_course.HomeLatestCourseResponse;
import com.rajasahabacademy.activity.home.model.latest_course.Video;
import com.rajasahabacademy.activity.login.model.LoginResponse;
import com.rajasahabacademy.activity.profile.model.profile_detail.ProfileDetailResponse;
import com.rajasahabacademy.support.Utils;
import com.rd.PageIndicatorView;

import java.util.List;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

public class HomeFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static HomeFragment fragment;
    private Activity mActivity;
    private View rootView;
    String categoryId = "";
    ShimmerFrameLayout homeAutoScrollShimmer;
    ShimmerFrameLayout homeLatestCourseShimmer;
    List<Video> latestVideoList;
    RecyclerView recyclerView;
    RecyclerView recyclerViewLatestVideo;
    List<Course> listCourses;
    HomeLatestVideoAdapter homeLatestVideoAdapter;
    HomeLatestCourseAdapter homeLatestCourseAdapter;

    public static HomeFragment newInstance() {
        fragment = new HomeFragment();
        Constants.FragmentReference.homeFragment = fragment;
        return fragment;
    }

    public static HomeFragment getInstance() {
        if (fragment == null)
            return new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().getString(Constants.Params.CATEGORY_ID) != null)
            categoryId = getArguments().getString(Constants.Params.CATEGORY_ID);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(true);
        ((HomeActivity) mActivity).showHideCart(false);
        ((HomeActivity) mActivity).resetAllBottom("Home");
        setUpLatestCourse();
        getWalletAmount();
    }

    private void init() {
        mActivity = getActivity();
        clickListener();
        setUpAutoScroll();
    }

    private void clickListener() {
        homeAutoScrollShimmer = rootView.findViewById(R.id.home_auto_scroll_shimmer);
        homeLatestCourseShimmer = rootView.findViewById(R.id.home_latest_course_shimmer);
        recyclerView = rootView.findViewById(R.id.recycler_latest_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));

        recyclerViewLatestVideo = rootView.findViewById(R.id.recycler_latest_video);
        recyclerViewLatestVideo.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
    }

    public void setUpAutoScroll() {
        AutoScrollViewPager autoScrollViewPager = rootView.findViewById(R.id.auto_scroll_viewpager);
        PageIndicatorView pageIndicatorView = rootView.findViewById(R.id.pageIndicatorView);
        if (Utils.isNetworkAvailable(mActivity)) {
            homeAutoScrollShimmer.startShimmer();
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
            communicator.post(101, mActivity, Constants.Apis.HOME_SLIDER_BANNER, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    homeAutoScrollShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            HomeSliderResponse modelResponse = (HomeSliderResponse) Utils.getObject(response, HomeSliderResponse.class);
                            if (modelResponse != null && modelResponse.getSuccess() != null && modelResponse.getSuccess()) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    rootView.findViewById(R.id.auto_scroll_lay).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.tv_no_banner).setVisibility(View.GONE);
                                    HomeSliderAdapter homeSliderAdapter = new HomeSliderAdapter(mActivity, modelResponse.getResults());
                                    autoScrollViewPager.setAdapter(homeSliderAdapter);
                                    autoScrollViewPager.startAutoScroll();
                                    pageIndicatorView.setVisibility(View.VISIBLE);
                                } else {
                                    rootView.findViewById(R.id.auto_scroll_lay).setVisibility(View.GONE);
                                    rootView.findViewById(R.id.tv_no_banner).setVisibility(View.VISIBLE);
                                }
                            } else {
                                rootView.findViewById(R.id.auto_scroll_lay).setVisibility(View.GONE);
                                rootView.findViewById(R.id.tv_no_banner).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        rootView.findViewById(R.id.auto_scroll_lay).setVisibility(View.GONE);
                        rootView.findViewById(R.id.tv_no_banner).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    homeAutoScrollShimmer.setVisibility(View.GONE);
                    rootView.findViewById(R.id.auto_scroll_lay).setVisibility(View.GONE);
                    rootView.findViewById(R.id.tv_no_banner).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    public void setUpLatestCourse() {
        if (Utils.isNetworkAvailable(mActivity)) {
            homeLatestCourseShimmer.startShimmer();
            Utils.hideKeyboard(mActivity);
            RequestParams params = new RequestParams();
            try {
                params.put(Constants.Params.CATEGORY_ID, categoryId);
                params.put(Constants.Params.USER_ID, Utils.getUserId(mActivity));
                params.put(Constants.Params.DEVICE_ID, Utils.getDeviceId(mActivity));
                Utils.printLog("ProfileDetailParams", params.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Communicator communicator = new Communicator();
            communicator.post(101, mActivity, Constants.Apis.HOME_LATEST_COURSE, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    homeLatestCourseShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            HomeLatestCourseResponse modelResponse = (HomeLatestCourseResponse) Utils.getObject(response, HomeLatestCourseResponse.class);
                            if (modelResponse != null && modelResponse.getSuccess() != null && modelResponse.getSuccess()) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().getCourses() != null &&
                                        modelResponse.getResults().getCourses().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.latest_course_text_lay).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.latest_video_text_lay).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.GONE);
                                    listCourses = modelResponse.getResults().getCourses();
                                    homeLatestCourseAdapter = new HomeLatestCourseAdapter(mActivity, listCourses);
                                    recyclerView.setAdapter(homeLatestCourseAdapter);
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.VISIBLE);
                                }
                                if (modelResponse.getResults() != null && modelResponse.getResults().getVideos() != null
                                        && modelResponse.getResults().getVideos().size() > 0) {
                                    latestVideoList = modelResponse.getResults().getVideos();
                                    recyclerViewLatestVideo.setVisibility(View.VISIBLE);
                                    homeLatestVideoAdapter = new HomeLatestVideoAdapter(mActivity, latestVideoList);
                                    recyclerViewLatestVideo.setAdapter(homeLatestVideoAdapter);
                                } else {
                                    recyclerViewLatestVideo.setVisibility(View.GONE);
                                    rootView.findViewById(R.id.tv_no_latest_video).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.VISIBLE);
                                recyclerViewLatestVideo.setVisibility(View.GONE);
                                rootView.findViewById(R.id.tv_no_latest_video).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.VISIBLE);
                        recyclerViewLatestVideo.setVisibility(View.GONE);
                        rootView.findViewById(R.id.tv_no_latest_video).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    homeLatestCourseShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.VISIBLE);
                    recyclerViewLatestVideo.setVisibility(View.GONE);
                    rootView.findViewById(R.id.tv_no_latest_video).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    private void getWalletAmount() {
        if (Utils.isNetworkAvailable(mActivity)) {
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
            communicator.post(101, mActivity, Constants.Apis.PROFILE_DETAIL, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    Utils.hideProgressBar();
                    try {
                        if (response != null && !response.equals("")) {
                            ProfileDetailResponse modelResponse = (ProfileDetailResponse) Utils.getObject(response, ProfileDetailResponse.class);
                            if (modelResponse != null && modelResponse.getSuccess() != null) {
                                LoginResponse loginResponse = Utils.getSaveLoginUser(mActivity);
                                loginResponse.getResults().setWallet(modelResponse.getSuccess().getWallet());
                                Utils.saveLoginUser(mActivity,loginResponse);
                                ((HomeActivity)mActivity).setWalletAmount();
                            }
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


    public List<Course> getHomeLatestCourseList() {
        return listCourses;
    }

    public List<Video> getHomeLatestVideoList() {
        return latestVideoList;
    }

    public void setCourseFilterList(List<Course> list) {
        if (recyclerView != null) {
            if (list != null && list.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.GONE);
                if (homeLatestCourseAdapter != null)
                    homeLatestCourseAdapter.updateList(list);
            } else {
                recyclerView.setVisibility(View.GONE);
                rootView.findViewById(R.id.tv_no_latest_course).setVisibility(View.VISIBLE);
            }
        }
    }

    public void setVideoFilterList(List<Video> list) {
        if (recyclerViewLatestVideo != null) {
            if (list != null && list.size() > 0) {
                recyclerViewLatestVideo.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tv_no_latest_video).setVisibility(View.GONE);
                if (homeLatestVideoAdapter != null)
                    homeLatestVideoAdapter.updateList(list);
            } else {
                recyclerViewLatestVideo.setVisibility(View.GONE);
                rootView.findViewById(R.id.tv_no_latest_video).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}