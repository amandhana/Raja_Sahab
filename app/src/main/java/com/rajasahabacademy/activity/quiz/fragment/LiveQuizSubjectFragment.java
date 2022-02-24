package com.rajasahabacademy.activity.quiz.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.quiz.adapter.LiveQuizSubjectAdapter;

public class LiveQuizSubjectFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static LiveQuizSubjectFragment fragment;
    private Activity mActivity;
    private View rootView;

    public static LiveQuizSubjectFragment newInstance() {
        fragment = new LiveQuizSubjectFragment();
        return fragment;
    }

    public static LiveQuizSubjectFragment getInstance() {
        if (fragment == null)
            return new LiveQuizSubjectFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_live_quiz_subject, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(true);
        ((HomeActivity) mActivity).showHideCart(false);
        ((HomeActivity) mActivity).resetAllBottom("Live Quiz");
    }

    private void init() {
        mActivity = getActivity();
        setUpSubjectList();
    }

    private void setUpSubjectList() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new LiveQuizSubjectAdapter(mActivity));
    }

    @Override
    public void onClick(View view) {

    }
}