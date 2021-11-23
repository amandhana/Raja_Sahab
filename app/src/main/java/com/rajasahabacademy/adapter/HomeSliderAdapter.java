package com.rajasahabacademy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.CourseDetailActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.home.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class HomeSliderAdapter extends PagerAdapter {
    Activity context;
    LayoutInflater layoutInflater;
    List<Result> list;

    public HomeSliderAdapter(Activity context, List<Result> list) {
        this.context = context;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewHolder, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item_home_auto_scroll, viewHolder, false);
        final ImageView imageView = itemView.findViewById(R.id.iv_home_slider_image);
        Utils.setImageUsingGlide(context, list.get(position).getImagePath(), imageView);
        viewHolder.addView(itemView);
        itemView.setOnClickListener(view -> {
            if (!list.get(position).getCourseId().equalsIgnoreCase("")) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra(Constants.Course.COURSE_ID, list.get(position).getCourseId());
                intent.putExtra(Constants.Course.FROM_WHERE, Constants.Course.FROM_WHERE_VALUE_SLIDER);
                context.startActivity(intent);
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }
}
