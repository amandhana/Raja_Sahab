package com.rajasahabacademy.activity.home.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.activity.CourseDetailActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.activity.home.model.latest_course.Course;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class HomeLatestCourseAdapter extends RecyclerView.Adapter<HomeLatestCourseAdapter.ViewHolder> {
    private final Activity context;
    List<Course> list;

    public HomeLatestCourseAdapter(Activity context, List<Course> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<Course> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_latest_course, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Utils.setImageUsingGlide(context, list.get(position).getThumbnail(), viewHolder.ivImage);
        viewHolder.tvName.setText(list.get(position).getTitle());
        viewHolder.tvAmount.setText(list.get(position).getPrice());
        viewHolder.tvTotalAmount.setText(list.get(position).getSaleprice());
        viewHolder.tvTotalAmount.setPaintFlags(viewHolder.tvTotalAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.amountLay.setVisibility(View.VISIBLE);

        switch (list.get(position).getCourseBuyStatus()) {
            case "0":
                viewHolder.tvBuyNow.setVisibility(View.VISIBLE);
                viewHolder.tvBuyNow.setText("Buy Now");
                break;
            case "1":
                viewHolder.tvBuyNow.setVisibility(View.GONE);
                break;
            default:
                viewHolder.tvBuyNow.setVisibility(View.VISIBLE);
                viewHolder.tvBuyNow.setText("Buy Now");
                break;
        }
        if (list.get(position).getPrice().equals("0")) {
            viewHolder.amountLay.setVisibility(View.INVISIBLE);
            viewHolder.tvBuyNow.setText("Free");
        }

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra(Constants.Preference.COURSE_IMAGE, list.get(position).getThumbnail());
            intent.putExtra(Constants.Course.COURSE_ID, list.get(position).getId());
            intent.putExtra(Constants.Course.COURSE_TITLE, list.get(position).getTitle());
            intent.putExtra(Constants.Course.COURSE_DESCRIPTION, list.get(position).getDescription());
            intent.putExtra(Constants.Course.EXPIRE_AMOUNT, list.get(position).getExpire());
            intent.putExtra(Constants.Course.TOTAL_AMOUNT, list.get(position).getPrice());
            intent.putExtra(Constants.Course.COURSE_BUY_STATUS, list.get(position).getCourseBuyStatus());
            intent.putExtra(Constants.Course.FROM_WHERE, "");
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
        final ImageView ivImage;
        final TextView tvName;
        final TextView tvTotalAmount;
        final TextView tvAmount;
        final TextView tvBuyNow;
        final RelativeLayout amountLay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_latest_course_image);
            tvName = itemView.findViewById(R.id.tv_latest_course_name);
            tvTotalAmount = itemView.findViewById(R.id.tv_latest_course_total_amount);
            tvAmount = itemView.findViewById(R.id.tv_latest_course_amount);
            tvBuyNow = itemView.findViewById(R.id.tv_buy_now_home);
            amountLay = itemView.findViewById(R.id.amount_lay);
        }
    }
}
