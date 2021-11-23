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

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.QuizReadyForTestActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.quiz.ResultLiveQuiz;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class LiveQuizTestsAdapter extends RecyclerView.Adapter<LiveQuizTestsAdapter.ViewHolder> {
    private final Activity context;
    List<ResultLiveQuiz> list;

    public LiveQuizTestsAdapter(Activity context, List<ResultLiveQuiz> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<ResultLiveQuiz> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_quiz_acc_to_subject, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvQuizTitle.setText(list.get(position).getTitle());
        viewHolder.tvPublishTime.setText(list.get(position).getCreatedAt());
        viewHolder.tvQuizTotalQuestion.setText(context.getString(R.string.quiz_question_format, "Total Question : ", list.get(position).getNoOfQuestion()));
        viewHolder.tvQuizTotalDuration.setText(context.getString(R.string.quiz_time_format, list.get(position).getNoOfQuestion(), " Minutes"));
        viewHolder.tvStart.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Params.TEST_ID,list.get(position).getId());
            bundle.putString(Constants.Params.TEST_DURATION,list.get(position).getDuration());
            bundle.putString(Constants.Quiz.FROM_WHERE,"");
            Utils.startActivityBundle(context, QuizReadyForTestActivity.class,bundle);
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
        final TextView tvQuizTotalDuration;
        final TextView tvStart;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tv_quiz_title);
            tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
            tvQuizTotalQuestion = itemView.findViewById(R.id.tv_quiz_total_question);
            tvQuizTotalDuration = itemView.findViewById(R.id.tv_quiz_time_duration);
            tvStart = itemView.findViewById(R.id.tv_start);
        }
    }
}
