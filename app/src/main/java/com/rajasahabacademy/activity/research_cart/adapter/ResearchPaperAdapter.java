package com.rajasahabacademy.activity.research_cart.adapter;

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

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.research_cart.activity.ResearchPaperDetailActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.activity.research_cart.model.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class ResearchPaperAdapter extends RecyclerView.Adapter<ResearchPaperAdapter.ViewHolder> {
    private Activity context;
    private List<Result> list;


    public ResearchPaperAdapter(Activity context,List<Result> list) {
        this.context = context;
        this.list = list;
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
        Utils.setImageUsingGlide(context,list.get(position).getThumbnail(),viewHolder.ivImage);
        viewHolder.tvTitle.setText(list.get(position).getTitle());
        viewHolder.tvView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ResearchPaperDetailActivity.class);
            intent.putExtra(Constants.ResearchPaper.DESCRIPTION, list.get(position).getDescription());
            intent.putExtra(Constants.ResearchPaper.STATUS, list.get(position).getStatus());
            intent.putExtra(Constants.ResearchPaper.TITLE, list.get(position).getTitle());
            intent.putExtra(Constants.ResearchPaper.PATH, list.get(position).getPath());
            intent.putExtra(Constants.ResearchPaper.THUMBNAIL, list.get(position).getThumbnail());
            intent.putExtra(Constants.ResearchPaper.IS_CART, list.get(position).getIsCart());
            intent.putExtra(Constants.ResearchPaper.RESEARCH_ID, list.get(position).getId());
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
        TextView tvTitle;
        TextView tvView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_research_paper_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_research_paper_title);
            tvView = itemView.findViewById(R.id.tv_view);
        }
    }
}
