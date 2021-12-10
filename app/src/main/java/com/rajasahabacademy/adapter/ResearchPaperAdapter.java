package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;


public class ResearchPaperAdapter extends RecyclerView.Adapter<ResearchPaperAdapter.ViewHolder> {
    private Activity context;


    public ResearchPaperAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_research_paper, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        switch (position) {
            case 0:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                break;
            case 1:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                break;
            case 2:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                break;
            case 3:
                viewHolder.ivImage.setBackgroundResource(R.drawable.dummy_avani);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_research_paper_thumbnail);
        }
    }
}
