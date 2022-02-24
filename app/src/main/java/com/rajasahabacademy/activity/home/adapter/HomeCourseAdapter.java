package com.rajasahabacademy.activity.home.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.home.activity.HomeActivity;
import com.rajasahabacademy.activity.home.model.HomeCourseModel;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class HomeCourseAdapter extends RecyclerView.Adapter<HomeCourseAdapter.ViewHolder> {
    private final Activity context;
    private final List<HomeCourseModel> list;

    public HomeCourseAdapter(Activity context, List<HomeCourseModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_course_type, parent, false);
        return new ViewHolder(view);
    }
 
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (list.get(position).isAllCatFlag()) {
            viewHolder.allCatLay.setVisibility(View.VISIBLE);
            viewHolder.catLay.setVisibility(View.GONE);
            if (list.get(position).isSelectCat()) {
                viewHolder.ivAllCat.setBackgroundTintList(context.getResources().getColorStateList(R.color.primary_text_color));
                viewHolder.tvAllCat.setTextColor(context.getResources().getColor(R.color.colorPrimaryVariant));
                viewHolder.viewAllCat.setVisibility(View.VISIBLE);

            } else {
                viewHolder.ivAllCat.setBackgroundTintList(context.getResources().getColorStateList(R.color.primary_text_color));
                viewHolder.tvAllCat.setTextColor(context.getResources().getColor(R.color.colorPrimaryVariant));
                viewHolder.viewAllCat.setVisibility(View.GONE);
            }
        } else {
            viewHolder.allCatLay.setVisibility(View.GONE);
            viewHolder.catLay.setVisibility(View.VISIBLE);
        }
        if (list.get(position).isSelectCat())
            viewHolder.viewAllCat.setVisibility(View.VISIBLE);

        else
            viewHolder.viewAllCat.setVisibility(View.GONE);

        if (position == 0)
            viewHolder.ivCategory.setBackgroundResource(R.drawable.ic_home_all_category);

        else
            Utils.setImageUsingGlide(context, list.get(position).getImage(), viewHolder.ivCategory);
        viewHolder.tvCategory.setText(list.get(position).getName());

        viewHolder.itemView.setOnClickListener(view -> {
            ((HomeActivity) context).setSelectCat(position == 0, position, list.get(position).getCatId());
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final RelativeLayout allCatLay;
        final LinearLayout catLay;
        final ImageView ivAllCat;
        final ImageView ivCategory;
        final TextView tvAllCat;
        final View viewAllCat;
        final TextView tvCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            allCatLay = itemView.findViewById(R.id.all_cat_lay);
            viewAllCat = itemView.findViewById(R.id.view_all_cat);
            catLay = itemView.findViewById(R.id.category_lay);
            ivAllCat = itemView.findViewById(R.id.iv_all_cat);
            tvAllCat = itemView.findViewById(R.id.tv_all_cat);
            ivCategory = itemView.findViewById(R.id.iv_category);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }
}
