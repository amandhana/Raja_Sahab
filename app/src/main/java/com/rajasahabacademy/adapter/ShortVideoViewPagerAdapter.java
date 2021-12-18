package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.rajasahabacademy.R;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.short_video.Datum;

import java.util.List;

public class ShortVideoViewPagerAdapter extends RecyclerView.Adapter<ShortVideoViewPagerAdapter.ViewHolder> {

    private Activity context;
    private final List<Datum> list;
    SimpleExoPlayer simpleExoPlayer;
    DefaultTrackSelector trackSelector;
    int count = 0;

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
        simpleExoPlayer = new SimpleExoPlayer.Builder(context).setTrackSelector(trackSelector).build();
        holder.playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri("https://rs.webseochicago.com/uploads/path/1.mp4");
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();
        count = count + 1;
    }

    public void releasePlayer() {
        if (count == 1)
            simpleExoPlayer.release();
        new Handler(Looper.getMainLooper()).postDelayed(() -> count = 0,1000);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.exoPlayerView);
        }
    }
}
