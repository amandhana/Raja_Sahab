package com.rajasahabacademy.activity.course_detail.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.course_detail.model.course_pdf.Ebook;
import com.rajasahabacademy.activity.pdf_view.PdfViewActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class BookmarkedPdfAdapter extends RecyclerView.Adapter<BookmarkedPdfAdapter.ViewHolder> {
    private final Activity context;
    String courseByStatus = "";

    public BookmarkedPdfAdapter(Activity context) {
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvEbookName;
        final ImageView ivPdfThumbnail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEbookName = itemView.findViewById(R.id.tv_ebook_name);
            ivPdfThumbnail = itemView.findViewById(R.id.iv_pdf_thumbnail);
        }
    }
}

