package com.rajasahabacademy.activity.quiz.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.quiz.activity.QuizAccToSubjectListActivity;
import com.rajasahabacademy.support.Utils;


public class LiveQuizSubjectAdapter extends RecyclerView.Adapter<LiveQuizSubjectAdapter.ViewHolder> {
    private final Activity context;

    public LiveQuizSubjectAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_quiz_subject, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        switch (position) {
            case 0:
                viewHolder.topLay.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.tvSubjectName.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.ivImage.setBackgroundResource(R.drawable.ic_dummy_programming);
                viewHolder.tvSubjectName.setText("Programming");
                break;
            case 1:
                viewHolder.topLay.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.tvSubjectName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.ivImage.setBackgroundResource(R.drawable.ic_dummy_physics);
                viewHolder.tvSubjectName.setText("Physics");
                break;
            case 2:
                viewHolder.topLay.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.tvSubjectName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.ivImage.setBackgroundResource(R.drawable.ic_dummy_biology);
                viewHolder.tvSubjectName.setText("Biology");
                break;
            case 3:
                viewHolder.topLay.setBackgroundColor(context.getResources().getColor(R.color.red));
                viewHolder.tvSubjectName.setTextColor(context.getResources().getColor(R.color.white));
                viewHolder.ivImage.setBackgroundResource(R.drawable.ic_dummy_geography);
                viewHolder.tvSubjectName.setText("Geography");
                break;
            case 4:
                viewHolder.topLay.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                viewHolder.tvSubjectName.setTextColor(context.getResources().getColor(R.color.primary_text_color));
                viewHolder.ivImage.setBackgroundResource(R.drawable.ic_dummy_155);
                viewHolder.tvSubjectName.setText("Hindi");
                break;
            case 5:
                viewHolder.topLay.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.tvSubjectName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                viewHolder.ivImage.setBackgroundResource(R.drawable.ic_dummy_maths);
                viewHolder.tvSubjectName.setText("Maths");
                break;
        }

        viewHolder.itemView.setOnClickListener(view -> Utils.startActivity(context, QuizAccToSubjectListActivity.class));
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView tvSubjectName;
        final RelativeLayout topLay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            topLay = itemView.findViewById(R.id.top_lay);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvSubjectName = itemView.findViewById(R.id.tv_subject_name);
        }
    }
}
