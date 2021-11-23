package com.rajasahabacademy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.PdfViewActivity;
import com.rajasahabacademy.activity.VideoActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.course.course_pdf.Ebook;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class CourseTopicPdfAdapter extends RecyclerView.Adapter<CourseTopicPdfAdapter.ViewHolder> {
    private final Activity context;
    List<Ebook> list;
    String courseByStatus = "";

    public CourseTopicPdfAdapter(Activity context, List<Ebook> list, String courseByStatus) {
        this.context = context;
        this.list = list;
        this.courseByStatus = courseByStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_notes, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvEbookName.setText(list.get(position).getTitle());
        viewHolder.itemView.setOnClickListener(view -> {
            if (courseByStatus.equalsIgnoreCase("1")) {
                if (!list.get(position).getPath().equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.Course.EBOOK_PATH, list.get(position).getPath());
                    bundle.putString(Constants.Course.EBOOK_NAME, list.get(position).getTitle());
                    bundle.putString(Constants.Course.FROM_WHERE, "");
                    Utils.startActivityBundle(context, PdfViewActivity.class, bundle);
                } else
                    Utils.showToastPopup(context, context.getResources().getString(R.string.ebook_path_valid));
            } else
                Utils.showToastPopup(context, context.getString(R.string.course_paid_validation));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvEbookName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEbookName = itemView.findViewById(R.id.tv_ebook_name);
        }
    }
}

