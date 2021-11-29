package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.CartActivity;
import com.rajasahabacademy.model.cart.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    final Activity context;
    final List<Result> list;

    public CartAdapter(Activity context,List<Result> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Utils.setImageUsingGlide(context,list.get(position).getThumbnail(),viewHolder.ivCartImage);
        viewHolder.tvCartName.setText(list.get(position).getTitle());
        viewHolder.tvPrice.setText(list.get(position).getPrice());
        viewHolder.tvCancel.setOnClickListener(view -> ((CartActivity)context).removeCart(list.get(position).getId()));
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
            ivCartImage = itemView.findViewById(R.id.iv_cart_image);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCartName = itemView.findViewById(R.id.tv_name);
            tvCancel = itemView.findViewById(R.id.tv_cancel);
        }
    }
}
