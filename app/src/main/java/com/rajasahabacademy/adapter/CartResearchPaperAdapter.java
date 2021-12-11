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
import com.rajasahabacademy.activity.CartActivity;
import com.rajasahabacademy.activity.ResearchCartActivity;
import com.rajasahabacademy.model.research_paper.cart.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class CartResearchPaperAdapter extends RecyclerView.Adapter<CartResearchPaperAdapter.ViewHolder> {
    private Activity context;
    private List<Result> list;

    public CartResearchPaperAdapter(Activity context,List<Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_research_paper, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Utils.setImageUsingGlide(context,list.get(position).getThumbnail(),viewHolder.ivCartImage);
        viewHolder.tvCartName.setText(list.get(position).getTitle());
        viewHolder.tvPrice.setText(list.get(position).getPrice());
        viewHolder.tvCancel.setOnClickListener(view -> ((ResearchCartActivity)context).removeCart(list.get(position).getId()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivCartImage;
        final TextView tvCartName;
        final TextView tvPrice;
        final TextView tvCancel;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCartImage = itemView.findViewById(R.id.iv_cart_image_research_paper);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCartName = itemView.findViewById(R.id.tv_name);
            tvCancel = itemView.findViewById(R.id.tv_cancel);
        }
    }
}
