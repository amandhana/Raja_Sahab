package com.rajasahabacademy.activity.home.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.fragment.LiveClassFragment;
import com.rajasahabacademy.activity.home.model.live_class.Complated;
import com.rajasahabacademy.activity.home.model.live_class.Oncoming;
import com.rajasahabacademy.activity.home.model.live_class.Upcoming;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class LiveClassAdapter extends RecyclerView.Adapter<LiveClassAdapter.ViewHolder> {
    private final Activity context;
    private final List<Object> list;
    private final LiveClassFragment fragment;

    public LiveClassAdapter(Activity context, List<Object> list, LiveClassFragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_class_live_video, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (list.get(position) instanceof Upcoming) {
            viewHolder.tvJoin.setVisibility(View.GONE);
            Upcoming upcomingModel = (Upcoming) list.get(position);
            Utils.setImageUsingGlide(context, upcomingModel.getThumbnail(), viewHolder.ivImage);
            viewHolder.tvName.setText(upcomingModel.getClassName());
            Utils.setHtmlText(upcomingModel.getDescription(), viewHolder.tvDescription);
        } else if (list.get(position) instanceof Oncoming) {
            viewHolder.tvJoin.setVisibility(View.VISIBLE);
            Oncoming oncomingModel = (Oncoming) list.get(position);
            Utils.setImageUsingGlide(context, oncomingModel.getThumbnail(), viewHolder.ivImage);
            viewHolder.tvName.setText(oncomingModel.getClassName());
            Utils.setHtmlText(oncomingModel.getDescription(), viewHolder.tvDescription);
        } else if (list.get(position) instanceof Complated) {
            viewHolder.tvJoin.setVisibility(View.GONE);
            Complated complatedModel = (Complated) list.get(position);
            Utils.setImageUsingGlide(context, complatedModel.getThumbnail(), viewHolder.ivImage);
            viewHolder.tvName.setText(complatedModel.getClassName());
            Utils.setHtmlText(complatedModel.getDescription(), viewHolder.tvDescription);
        }

        viewHolder.tvJoin.setOnClickListener(view -> {
            Oncoming oncomingModel = (Oncoming) list.get(position);
            fragment.zoomInit(oncomingModel.getZoomApiKey()
                    , oncomingModel.getZoomApiSecret()
                    , oncomingModel.getMeetingId()
                    , oncomingModel.getPassword());
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName;
        TextView tvDescription;
        TextView tvJoin;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvJoin = itemView.findViewById(R.id.tv_join);
        }
    }
}

