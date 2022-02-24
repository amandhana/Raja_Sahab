package com.rajasahabacademy.activity.chat.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.chat.model.Result;
import com.rajasahabacademy.support.Utils;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity context;
    List<Result> list;

    public ChatAdapter(Activity context, List<Result> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<Result> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSenderId().equals(Utils.getUserId(context)))
            return 1;
        else return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_send, parent, false);
            return new StudentViewHolder(itemView);
        }
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_receive, parent, false);
        return new AdminViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 1:
                final StudentViewHolder studentViewHolder = (StudentViewHolder) holder;
                final Result model = list.get(position);
                studentViewHolder.tvMessage.setText(model.getMessage());
                studentViewHolder.tvTime.setText(Utils.getFormattedDate(model.getCreatedAt()));
                break;
            case 2:
                final AdminViewHolder adminViewHolder = (AdminViewHolder) holder;
                final Result modelAdmin = list.get(position);
                adminViewHolder.tvMessage.setText(modelAdmin.getMessage());
                adminViewHolder.tvTime.setText(Utils.getFormattedDate(modelAdmin.getCreatedAt()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        final TextView tvMessage;
        final TextView tvTime;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }

    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        final TextView tvMessage;
        final TextView tvTime;

        AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
