package com.rajasahabacademy.adapter;

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
import com.rajasahabacademy.activity.JobDetailActivity;
import com.rajasahabacademy.activity.MyOrdersActivity;
import com.rajasahabacademy.support.Utils;


public class JobAlertAdapter extends RecyclerView.Adapter<JobAlertAdapter.ViewHolder> {
    private Activity context;
    Activity mActivity;


    public JobAlertAdapter(Activity context) {
        this.context = context;
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
        switch (position) {
            case 0:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                viewHolder.tvTextview.setText("Indian Navy 2021 - Sailor (MR) - April 2022");
                break;
            case 1:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                viewHolder.tvTextview.setText("Indian Navy Recruitment 2021 for Various Post Job");
                break;
            case 2:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                viewHolder.tvTextview.setText("Indian Army TGC - 134 Jan Recruitment 2021");
                break;
            case 3:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                viewHolder.tvTextview.setText("Indian Air Force Airmen Exam Date 2021");
                break;
            case 4:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                viewHolder.tvTextview.setText("Indian Army TGC - Apply Online for 40 Vacancy");
                break;
            case 5:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                viewHolder.tvTextview.setText("AFCAT (01/2022) Recruitment 2021 - Apply");
                break;

        }

        viewHolder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.tv_view) {
                    Utils.startActivity(mActivity, JobDetailActivity.class);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return 6;
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
