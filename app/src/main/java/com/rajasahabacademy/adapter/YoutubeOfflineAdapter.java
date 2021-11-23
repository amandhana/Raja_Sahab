package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.VideoActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

import java.io.File;
import java.util.List;

public class YoutubeOfflineAdapter extends RecyclerView.Adapter<YoutubeOfflineAdapter.ViewHolder> {

    private final Activity context;
    List<File> list;

    public YoutubeOfflineAdapter(Activity context, List<File> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube_offline, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(list.get(position).getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        viewHolder.ivVideoThumbnail.setImageBitmap(bMap);
        if(String.valueOf(list.get(position)).contains("/")){
            int lastIndex = String.valueOf(list.get(position)).lastIndexOf("/");
            String name = String.valueOf(list.get(position)).substring(++lastIndex);
            viewHolder.tvVideoTitle.setText(name);
        }


        viewHolder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Course.FROM_WHERE, "Offline");
            bundle.putString(Constants.Course.OFFLINE_FILE, String.valueOf(list.get(position)));
            Utils.startActivityBundle(context, VideoActivity.class, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivVideoThumbnail;
        final TextView tvVideoTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVideoThumbnail = itemView.findViewById(R.id.iv_video_thumbnail);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_title);
        }
    }
}
