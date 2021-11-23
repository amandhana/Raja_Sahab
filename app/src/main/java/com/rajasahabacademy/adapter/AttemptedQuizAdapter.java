package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.QuizRankActivity;
import com.rajasahabacademy.activity.QuizReadyForTestActivity;
import com.rajasahabacademy.activity.ViewSolutionActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.attempt_quiz.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class AttemptedQuizAdapter extends RecyclerView.Adapter<AttemptedQuizAdapter.ViewHolder> {
    private final Activity context;
    List<Result> list;

    public AttemptedQuizAdapter(Activity context, List<Result> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attempted_quiz, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvQuizTitle.setText(list.get(position).getTitle());
        viewHolder.tvPublishTime.setText(list.get(position).getCreatedAt());
        viewHolder.tvQuizTotalQuestion.setText(context.getString(R.string.quiz_question_format, "Total Question : ", list.get(position).getNoOfQuestion()));
        viewHolder.tvAttemptCount.setText(context.getString(R.string.quiz_question_format, "Attempt Count : ", list.get(position).getAttended()));

        viewHolder.tvReAttempt.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Params.TEST_ID, list.get(position).getId());
            bundle.putString(Constants.Params.TEST_DURATION, list.get(position).getDuration());
            bundle.putString(Constants.Quiz.FROM_WHERE, "");
            Utils.startActivityBundle(context, QuizReadyForTestActivity.class, bundle);
        });

        viewHolder.tvViewRank.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Params.TEST_ID, list.get(position).getId());
            bundle.putString(Constants.Params.TEST_DURATION, list.get(position).getDuration());
            bundle.putString(Constants.Quiz.QUIZ_NAME, list.get(position).getTitle());
            bundle.putString(Constants.Quiz.FROM_WHERE, "");
            Utils.startActivityBundle(context, QuizRankActivity.class, bundle);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvQuizTitle;
        final TextView tvPublishTime;
        final TextView tvQuizTotalQuestion;
        final TextView tvViewRank;
        final TextView tvReAttempt;
        final TextView tvAttemptCount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tv_quiz_title);
            tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
            tvQuizTotalQuestion = itemView.findViewById(R.id.tv_quiz_total_question);
            tvViewRank = itemView.findViewById(R.id.tv_view_rank);
            tvReAttempt = itemView.findViewById(R.id.tv_reattempt);
            tvAttemptCount = itemView.findViewById(R.id.tv_attempt_count);
        }
    }
}
