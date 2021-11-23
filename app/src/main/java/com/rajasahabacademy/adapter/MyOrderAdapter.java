package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.CourseDetailActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.order_history.Result;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private final Activity context;
    List<Result> list;

    public MyOrderAdapter(Activity context, List<Result> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Utils.setImageUsingGlide(context, list.get(position).getThumbnail(), viewHolder.ivImage);
        viewHolder.tvName.setText(list.get(position).getCourseTitle());
        viewHolder.tvTotalAmount.setText(list.get(position).getPrice());
        viewHolder.tvPublishTime.setText(list.get(position).getExpireDate());
        viewHolder.tvOrderRepeat.setVisibility(View.GONE);

        viewHolder.tvViewDetails.setOnClickListener(view -> {
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra(Constants.Preference.COURSE_IMAGE, list.get(position).getThumbnail());
            intent.putExtra(Constants.Course.COURSE_ID, list.get(position).getCourseId());
            intent.putExtra(Constants.Course.COURSE_TITLE, list.get(position).getCourseTitle());
            intent.putExtra(Constants.Course.EXPIRE_AMOUNT, "");
            intent.putExtra(Constants.Course.TOTAL_AMOUNT, list.get(position).getPrice());
            intent.putExtra(Constants.Course.COURSE_BUY_STATUS, "1");
            intent.putExtra(Constants.Course.FROM_WHERE, "");
            context.startActivity(intent);
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
        final TextView tvPublishTime;
        final TextView tvOrderRepeat;
        final TextView tvViewDetails;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_order_history);
            tvName = itemView.findViewById(R.id.tv_order_history_name);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
            tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
            tvOrderRepeat = itemView.findViewById(R.id.tv_order_repeat);
            tvViewDetails = itemView.findViewById(R.id.tv_view_details);
        }
    }
}
