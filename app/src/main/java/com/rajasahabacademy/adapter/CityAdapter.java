package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.model.CityResponse.ResultCity;
import com.rajasahabacademy.model.StateResponse.Result;

import java.util.List;

public class CityAdapter extends BaseAdapter {
    Context context;
    List<ResultCity> list;
    LayoutInflater inflter;

    public CityAdapter(Context applicationContext, List<ResultCity> list) {
        this.context = applicationContext;
        this.list = list;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_state_city, null);
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(list.get(position).getName());
        return view;
    }
}
