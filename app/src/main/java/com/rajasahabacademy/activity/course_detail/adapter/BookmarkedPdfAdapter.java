package com.rajasahabacademy.activity.course_detail.adapter;

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
import com.rajasahabacademy.activity.course_detail.model.bookmarked_pdf.Datum;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class BookmarkedPdfAdapter extends RecyclerView.Adapter<BookmarkedPdfAdapter.ViewHolder> {
    private final Activity context;
    List<Datum> list;

    public BookmarkedPdfAdapter(Activity context, List<Datum> list) {
        this.context = context;
        this.list = list;
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
        Utils.setImageUsingGlide(context, list.get(position).getThumbnail(), viewHolder.ivPdfThumbnail);
        viewHolder.tvEbookName.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
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

