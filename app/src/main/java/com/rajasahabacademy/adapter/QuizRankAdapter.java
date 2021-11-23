package com.rajasahabacademy.adapter;

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
import com.rajasahabacademy.model.rank.Ranking;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class QuizRankAdapter extends RecyclerView.Adapter<QuizRankAdapter.ViewHolder> {
    private final Activity context;
    List<Ranking> list;

    public QuizRankAdapter(Activity context, List<Ranking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_rank, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Utils.setProfileImageUsingGlide(context, list.get(position).getImage(), viewHolder.ivImage);
        viewHolder.tvStudentName.setText(list.get(position).getName());
        viewHolder.tvRankNo.setText(context.getResources().getString(R.string.rank_format, "#", list.get(position).getRanking()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView tvStudentName;
        final TextView tvRankNo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_rank_image);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvRankNo = itemView.findViewById(R.id.tv_rank_no);
        }
    }
}
