package com.rajasahabacademy.activity.notification.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.activity.CourseDetailActivity;
import com.rajasahabacademy.activity.my_save_ebook.activity.MySaveEbookActivity;
import com.rajasahabacademy.activity.notification.activity.NotificationActivity;
import com.rajasahabacademy.activity.quiz.activity.QuizReadyForTestActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.activity.notification.model.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Activity context;
    List<Result> list;

    public NotificationAdapter(Activity context, List<Result> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvNotificationTitle.setText(list.get(position).getSubject());
        String message = "<font color='black'>" + "<b>" + list.get(position).getMessage() + "</b>" + "<font color='cyan'>" + "<font size='22'></font>";
        viewHolder.webViewDescription.loadData(message, "text/html", "utf8");
        viewHolder.tvNotificationDescription.setText(list.get(position).getMessage());
        viewHolder.tvNotificationTime.setText(list.get(position).getCreated());
        viewHolder.itemView.setOnClickListener(view -> {
            if (list.get(position).getIsType().equalsIgnoreCase("course")) {
                if (!list.get(position).getCourseId().equals("")) {
                    Intent intent = new Intent(context, CourseDetailActivity.class);
                    intent.putExtra(Constants.Preference.COURSE_IMAGE, list.get(position).getThumbnail());
                    intent.putExtra(Constants.Course.COURSE_ID, list.get(position).getCourseId());
                    intent.putExtra(Constants.Course.COURSE_TITLE, list.get(position).getTitle());
                    intent.putExtra(Constants.Course.COURSE_DESCRIPTION, ""); // model me key add kr den agr res[ose me aye to
                    intent.putExtra(Constants.Course.EXPIRE_AMOUNT, "");
                    intent.putExtra(Constants.Course.TOTAL_AMOUNT, list.get(position).getPrice());
                    intent.putExtra(Constants.Course.COURSE_BUY_STATUS, list.get(position).getCourseBuyStatus());
                    intent.putExtra(Constants.Course.FROM_WHERE, "");
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            } else if (list.get(position).getIsType().equalsIgnoreCase("quiz")) {
                if (!list.get(position).getQuizId().equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.Params.TEST_ID, list.get(position).getQuizId());
                    bundle.putString(Constants.Params.TEST_DURATION, list.get(position).getDuration());
                    bundle.putString(Constants.Quiz.FROM_WHERE, "");
                    Utils.startActivityBundle(context, QuizReadyForTestActivity.class, bundle);
                }
            } else if (list.get(position).getIsType().equalsIgnoreCase("manual")) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNotificationTitle;
        final TextView tvNotificationDescription;
        final TextView tvNotificationTime;
        final WebView webViewDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationTitle = itemView.findViewById(R.id.tv_notification_title);
            tvNotificationDescription = itemView.findViewById(R.id.tv_notification_description);
            tvNotificationTime = itemView.findViewById(R.id.tv_notification_time);
            webViewDescription = itemView.findViewById(R.id.web_view_description);
        }
    }
}

