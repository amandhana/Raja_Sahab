package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.CourseDetailActivity;
import com.rajasahabacademy.activity.CourseTopicPdfActivity;
import com.rajasahabacademy.activity.CourseTopicVideoActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.course.course_subject.VideosCategory;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class CourseDetailVideoAdapter extends RecyclerView.Adapter<CourseDetailVideoAdapter.ViewHolder> {
    private final Activity context;
    List<VideosCategory> list;
    String courseId = "";
    String totalAmount = "";
    String expireAmount = "";
    String courseByStatus = "";

    public CourseDetailVideoAdapter(Activity context, List<VideosCategory> list
            , String courseId, String totalAmount, String expireAmount, String courseByStatus) {
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
            Intent intent = new Intent(context, CourseTopicVideoActivity.class);
            intent.putExtra(Constants.Course.COURSE_ID, courseId);
            intent.putExtra(Constants.Course.SUBJECT, list.get(position).getName());
            intent.putExtra(Constants.Course.TOTAL_AMOUNT, totalAmount);
            intent.putExtra(Constants.Course.EXPIRE_AMOUNT, expireAmount);
            intent.putExtra(Constants.Course.SUBJECT_ID, list.get(position).getSubjectId());
            intent.putExtra(Constants.Course.COURSE_BUY_STATUS, courseByStatus);
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
