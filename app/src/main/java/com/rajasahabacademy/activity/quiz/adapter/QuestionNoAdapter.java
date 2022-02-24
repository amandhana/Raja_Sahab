package com.rajasahabacademy.activity.quiz.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.quiz.activity.QuizQuestionAnsActivity;
import com.rajasahabacademy.activity.quiz.model.question_ans.Result;

import java.util.List;

public class QuestionNoAdapter extends RecyclerView.Adapter<QuestionNoAdapter.ViewHolder> {
    private final Activity context;
    List<Result> list;

    public QuestionNoAdapter(Activity context, List<Result> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<Result> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_no, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String questionNo = String.valueOf(position + 1);
        viewHolder.tvQuestionNo.setText(questionNo);
        if (list.get(position).getSelectFlag()) {
            viewHolder.circleLay.setBackgroundResource(R.drawable.circle_white);
            viewHolder.circleLay.setBackgroundTintList(context.getResources().getColorStateList(R.color.answered));
            viewHolder.tvQuestionNo.setTextColor(context.getResources().getColor(R.color.white));
        } else if (list.get(position).getSkipFlag()) {
            viewHolder.circleLay.setBackgroundResource(R.drawable.circle_white);
            viewHolder.circleLay.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
            viewHolder.tvQuestionNo.setTextColor(context.getResources().getColor(R.color.white));
        } else if (list.get(position).getVisitedFlag()) {
            viewHolder.circleLay.setBackgroundResource(R.drawable.circle_white);
            viewHolder.circleLay.setBackgroundTintList(context.getResources().getColorStateList(R.color.not_visited));
            viewHolder.tvQuestionNo.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.circleLay.setBackgroundResource(R.drawable.circle_border);
            viewHolder.circleLay.setBackgroundTintList(context.getResources().getColorStateList(R.color.not_visited));
            viewHolder.tvQuestionNo.setTextColor(context.getResources().getColor(R.color.not_visited));
        }
        viewHolder.itemView.setOnClickListener(view -> {
            ((QuizQuestionAnsActivity) context).performNavMenuAction();
            ((QuizQuestionAnsActivity) context).loadNextQuestion("manual", position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final RelativeLayout circleLay;
        final TextView tvQuestionNo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleLay = itemView.findViewById(R.id.circle_lay);
            tvQuestionNo = itemView.findViewById(R.id.tv_question_no);
        }
    }
}
