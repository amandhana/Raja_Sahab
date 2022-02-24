package com.rajasahabacademy.activity.short_video.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.short_video.activity.ShortVideoActivity;
import com.rajasahabacademy.activity.short_video.model.Datum;

import java.util.List;

public class ShortVideoViewPagerAdapter extends RecyclerView.Adapter<ShortVideoViewPagerAdapter.ViewHolder> {

    private final Activity context;
    private final List<Datum> list;
    SimpleExoPlayer simpleExoPlayer;
    DefaultTrackSelector trackSelector;
    ViewHolder holder;

    public ShortVideoViewPagerAdapter(Activity context, List<Datum> list) {
        this.context = context;
        this.list = list;
        trackSelector = new DefaultTrackSelector(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;
        if (simpleExoPlayer != null)
            simpleExoPlayer.release();
        if (list.get(position).isFlag()) {
            simpleExoPlayer = new SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
            holder.playerView.setPlayer(simpleExoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(list.get(position).getPath());
            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();
            simpleExoPlayer.play();
            simpleExoPlayer.addListener(new ExoPlayer.Listener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_ENDED) {
                        int nextPosition = position + 1;
                        if (nextPosition <= list.size() - 1)
                            ((ShortVideoActivity)context).loadNextPage(nextPosition);
                    }
                }
            });
        }
        holder.titleShortVideo.setText(list.get(position).getTitle());
        if (list.get(position).getLike() == 0)
            holder.ivlikeShortVideo.setBackgroundResource(R.drawable.ic_like_empty);
        else holder.ivlikeShortVideo.setBackgroundResource(R.drawable.ic_like_fill);
        holder.ivlikeShortVideo.setOnClickListener(view -> {
            if (list.get(position).getLike() == 0)
                ((ShortVideoActivity) context).likeVideo(position, list.get(position).getId());
            else ((ShortVideoActivity) context).unlikeVideo(position, list.get(position).getId());
        });
    }

    public void setLikeDislikeImage(int position) {
        if (list.get(position).getLike() == 0)
            holder.ivlikeShortVideo.setBackgroundResource(R.drawable.ic_like_empty);
        else holder.ivlikeShortVideo.setBackgroundResource(R.drawable.ic_like_fill);
    }

    public void releasePlayer() {
        if (simpleExoPlayer != null)
            simpleExoPlayer.release();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        TextView titleShortVideo;
        ImageView ivlikeShortVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.exoPlayerView);
            titleShortVideo = itemView.findViewById(R.id.tv_short_video_title);
            ivlikeShortVideo = itemView.findViewById(R.id.iv_short_video_like);
        }
    }
}
