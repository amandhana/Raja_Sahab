package com.rajasahabacademy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.adapter.LiveClassAdapter;

public class NonLiveClassFragment extends Fragment implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    private static NonLiveClassFragment fragment;
    private Activity mActivity;
    private View rootView;

    public static NonLiveClassFragment newInstance() {
        fragment = new NonLiveClassFragment();
        return fragment;
    }

    public static NonLiveClassFragment getInstance() {
        if (fragment == null)
            return new NonLiveClassFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_non_live_class, container, false);
        init();
        return rootView;
    }

    private void setUpLiveVideo() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_live_video);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new LiveClassAdapter());
    }

    private void init() {
        setUpLiveVideo();
    }

    @Override
    public void onClick(View view) {

    }
}