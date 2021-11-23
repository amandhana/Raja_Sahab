package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.VideoActivity;
import com.rajasahabacademy.activity.YoutubeVideoPlayActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.home.latest_course.Video;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class HomeLatestVideoAdapter extends RecyclerView.Adapter<HomeLatestVideoAdapter.ViewHolder> {
    private final Activity context;
    List<Video> list;

    public HomeLatestVideoAdapter(Activity context, List<Video> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<Video> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_latest_video, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Utils.setImageUsingGlide(context, list.get(position).getThumbnail(), viewHolder.ivVideoThumbnail);
        viewHolder.tvVideoTitle.setText(list.get(position).getTitle());
        Utils.setHtmlText(list.get(position).getDescription(), viewHolder.tvVideoDescription);

        viewHolder.itemView.setOnClickListener(view -> {
            if (list.get(position).getCourseBuyStatus().equalsIgnoreCase("1") ||
                    list.get(position).getDemo().equals("1")) {
                if (!list.get(position).getPath().equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.Course.VIDEO_ID, list.get(position).getId());
                    bundle.putString(Constants.Course.VIDEO_PATH, list.get(position).getPath());
                    bundle.putString(Constants.Course.FROM_WHERE, "");
                    bundle.putString(Constants.Course.VIDEO_DESCRIPTION, list .get(position).getDescription());
                    if (list.get(position).getType().equals(Constants.Course.YOUTUBE_VIDEO_TYPE))
                        Utils.startActivityBundle(context, YoutubeVideoPlayActivity.class, bundle);
                    else Utils.startActivityBundle(context, VideoActivity.class, bundle);
                } else
                    Utils.showToastPopup(context, context.getResources().getString(R.string.video_id_valid));
            } else
                Utils.showToastPopup(context, context.getString(R.string.course_paid_validation));
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivVideoThumbnail;
        final TextView tvVideoTitle;
        final TextView tvVideoDescription;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVideoThumbnail = itemView.findViewById(R.id.iv_video_latest_video_home);
            tvVideoTitle = itemView.findViewById(R.id.tv_name_latest_video_home);
            tvVideoDescription = itemView.findViewById(R.id.tv_video_description_home);
        }
    }
}
