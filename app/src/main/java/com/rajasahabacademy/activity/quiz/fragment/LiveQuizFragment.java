package com.rajasahabacademy.activity.quiz.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.RequestParams;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.quiz.adapter.LiveQuizTestsAdapter;
import com.rajasahabacademy.api.Communicator;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.api.CustomResponseListener;
import com.rajasahabacademy.activity.quiz.model.QuizTestResponse;
import com.rajasahabacademy.activity.quiz.model.ResultLiveQuiz;
import com.rajasahabacademy.support.Utils;

import java.util.ArrayList;
import java.util.List;

public class LiveQuizFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static LiveQuizFragment fragment;
    private Activity mActivity;
    private View rootView;
    String categoryId = "";
    ShimmerFrameLayout quizTestShimmer;
    RecyclerView recyclerView;
    LiveQuizTestsAdapter liveQuizTestsAdapter;
    List<ResultLiveQuiz> list = new ArrayList<>();

    public static LiveQuizFragment newInstance() {
        fragment = new LiveQuizFragment();
        Constants.FragmentReference.liveQuizFragment = fragment;
        return fragment;
    }

    public static LiveQuizFragment getInstance() {
        if (fragment == null)
            return new LiveQuizFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null && getArguments().getString(Constants.Params.CATEGORY_ID) != null)
            categoryId = getArguments().getString(Constants.Params.CATEGORY_ID);
        rootView = inflater.inflate(R.layout.fragment_live_quiz, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(true);
        ((HomeActivity) mActivity).showHideCart(false);
        ((HomeActivity) mActivity).resetAllBottom("Live Quiz");
        setUpQuizList();
    }

    private void init() {
        mActivity = getActivity();
        clickListener();
    }

    private void clickListener() {
        quizTestShimmer = rootView.findViewById(R.id.quiz_test_shimmer);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
    }

    private void setUpQuizList() {
        if (Utils.isNetworkAvailable(mActivity)) {
            quizTestShimmer.startShimmer();
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
            communicator.post(101, mActivity, Constants.Apis.QUIZ_TESTS, params, new CustomResponseListener() {
                @Override
                public void onResponse(int requestCode, String response) {
                    quizTestShimmer.setVisibility(View.GONE);
                    try {
                        if (response != null && !response.equals("")) {
                            QuizTestResponse modelResponse = (QuizTestResponse) Utils.getObject(response, QuizTestResponse.class);
                            if (modelResponse != null) {
                                if (modelResponse.getResults() != null && modelResponse.getResults().size() > 0) {
                                    rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.GONE);
                                    for (int i = 0; i < modelResponse.getResults().size(); i++) {
                                        if (modelResponse.getResults().get(i).getAttended().equalsIgnoreCase("0"))
                                            list.add(modelResponse.getResults().get(i));
                                    }
                                    if (list.size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        liveQuizTestsAdapter = new LiveQuizTestsAdapter(mActivity, list);
                                        recyclerView.setAdapter(liveQuizTestsAdapter);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerView.setVisibility(View.GONE);
                        rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error) {
                    quizTestShimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
                }
            });
        } else Utils.showToastPopup(mActivity, getString(R.string.internet_error));
    }

    public List<ResultLiveQuiz> getLiveQuizList() {
        return list;
    }

    public void setCourseFilterList(List<ResultLiveQuiz> list) {
        if (recyclerView != null) {
            if (list != null && list.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.GONE);
                if (liveQuizTestsAdapter != null)
                    liveQuizTestsAdapter.updateList(list);
            } else {
                recyclerView.setVisibility(View.GONE);
                rootView.findViewById(R.id.tv_no_quiz_test).setVisibility(View.VISIBLE);
            }
        }
    }

}