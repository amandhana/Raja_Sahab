package com.rajasahabacademy.activity.job_alert.adapter;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

;import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.job_alert.activity.JobDetailActivity;
import com.rajasahabacademy.activity.job_alert.model.Result;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class JobAlertAdapter extends RecyclerView.Adapter<JobAlertAdapter.ViewHolder> {
    private Activity context;
    List<Result> list;


    public JobAlertAdapter(Activity context,List<Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_alert, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvTextview.setText(list.get(position).getTitle());
        Utils.setImageUsingGlide(context,list.get(position).getThumbnail(),viewHolder.ivImage);
        viewHolder.tvView.setOnClickListener(view -> {
            Intent intent = new Intent(context, JobDetailActivity.class);
            intent.putExtra(Constants.JobAlert.DESCRIPTION, list.get(position).getDescription());
            intent.putExtra(Constants.JobAlert.TITLE, list.get(position).getTitle());
            intent.putExtra(Constants.JobAlert.THUMBNAIL, list.get(position).getThumbnail());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(context, viewHolder.ivImage, context.getResources().getString(R.string.course_image_transition));
            context.startActivity(intent, options.toBundle());
            context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTextview;
        TextView tvView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_job_alert_thumbnail);
            tvTextview = itemView.findViewById(R.id.tv_job_alert_title);
            tvView = itemView.findViewById(R.id.tv_view);
        }
    }
}
