package com.rajasahabacademy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.HomeActivity;
import com.rajasahabacademy.adapter.CourseAdapter;
import com.rajasahabacademy.adapter.HomeSliderAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.model.course.CourseResponse;
import com.rajasahabacademy.model.course.Result;
import com.rajasahabacademy.model.home.HomeSliderResponse;
import com.rajasahabacademy.model.home.latest_course.Course;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class CourseFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static CourseFragment fragment;
    private Activity mActivity;
    private View rootView;
    String categoryId = "";
    ShimmerFrameLayout courseShimmer;
    RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    List<Result> courseList;

    public static CourseFragment newInstance() {
        fragment = new CourseFragment();
        Constants.FragmentReference.courseFragment = fragment;
        return fragment;
    }

    public static CourseFragment getInstance() {
        if (fragment == null)
            return new CourseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().getString(Constants.Params.CATEGORY_ID) != null)
            categoryId = getArguments().getString(Constants.Params.CATEGORY_ID);
        rootView = inflater.inflate(R.layout.fragment_course, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(false);
        ((HomeActivity) mActivity).showHideCart(false);
    }

    private void init() {
        mActivity = getActivity();
        clickListener();
        setUpCourses();
    }

    private void clickListener() {
        courseShimmer = rootView.findViewById(R.id.course_shimmer);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2, RecyclerView.VERTICAL, false));
    }

    private void setUpCourses() {
        if (Utils.isNetworkAvailable(mActivity)) {
            courseShimmer.startShimmer();
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
            communicator.post(101, mActivity, Constants.Apis.COURSES, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    courseShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            CourseResponse modelResponse = (CourseResponse) Utils.getObject(response, CourseResponse.class);
                            if (modelResponse != null && modelResponse.getSuccess() != null && modelResponse.getSuccess()) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.tv_no_course).setVisibility(View.GONE);
                                    courseList = modelResponse.getResults();
                                    courseAdapter = new CourseAdapter(mActivity, courseList);
                                    recyclerView.setAdapter(courseAdapter);
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    rootView.findViewById(R.id.tv_no_course).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                rootView.findViewById(R.id.tv_no_course).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        rootView.findViewById(R.id.tv_no_course).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    courseShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    rootView.findViewById(R.id.tv_no_course).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    public List<Result> getCourseList() {
        return courseList;
    }

    public void setCourseFilterList(List<Result> list) {
        if (recyclerView != null) {
            if (list != null && list.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tv_no_course).setVisibility(View.GONE);
                if (courseAdapter != null)
                    courseAdapter.updateList(list);
            } else {
                recyclerView.setVisibility(View.GONE);
                rootView.findViewById(R.id.tv_no_course).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
