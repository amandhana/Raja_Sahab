package com.rajasahabacademy.fragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.HomeActivity;
import com.rajasahabacademy.adapter.LiveClassAdapter;
import com.rajasahabacademy.api.Constants;

public class LiveClassFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static LiveClassFragment fragment;
    private Activity mActivity;
    private View rootView;

    public static LiveClassFragment newInstance() {
        fragment = new LiveClassFragment();
        Constants.FragmentReference.liveClassFragment = fragment;
        return fragment;
    }

    public static LiveClassFragment getInstance() {
        if (fragment == null)
            return new LiveClassFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_live_class, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) mActivity).showHideBottomNavigation(true);
        ((HomeActivity) mActivity).showHideCart(false);
        ((HomeActivity) mActivity).resetAllBottom("Live Classes");
    }

    private void init() {
        mActivity = getActivity();
        setUpLiveVideo();
    }


    private void setUpLiveVideo() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_live_video);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new LiveClassAdapter());
    }


    @Override
    public void onClick(View view) {

    }
}