package com.rajasahabacademy.activity.course_detail.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.activity.CourseTopicPdfActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.activity.course_detail.model.course_subject.EbookCategory;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class CourseDetailPdfAdapter extends RecyclerView.Adapter<CourseDetailPdfAdapter.ViewHolder> {
    private final Activity context;
    List<EbookCategory> list;
    String courseId = "";
    String totalAmount = "";
    String expireAmount = "";
    String courseByStatus = "";

    public CourseDetailPdfAdapter(Activity context, List<EbookCategory> list,
                                  String courseId, String totalAmount, String expireAmount,
                                  String courseByStatus) {
        this.context = context;
        this.list = list;
        this.courseId = courseId;
        this.totalAmount = totalAmount;
        this.expireAmount = expireAmount;
        this.courseByStatus = courseByStatus;
    }
    public void updateStatus(String courseByStatus){
        this.courseByStatus = courseByStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_detail, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvSubjectName.setText(list.get(position).getName());
        viewHolder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Course.COURSE_ID, courseId);
            bundle.putString(Constants.Course.SUBJECT, list.get(position).getName());
            bundle.putString(Constants.Course.TOTAL_AMOUNT, totalAmount);
            bundle.putString(Constants.Course.EXPIRE_AMOUNT, expireAmount);
            bundle.putString(Constants.Course.SUBJECT_ID, list.get(position).getSubjectId());
            bundle.putString(Constants.Course.COURSE_BUY_STATUS, courseByStatus);
            Utils.startActivityBundle(context,CourseTopicPdfActivity.class,bundle);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvSubjectName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tv_course_detail_subject_name);
        }
    }
}
