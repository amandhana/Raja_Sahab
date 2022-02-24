package com.rajasahabacademy.activity.my_save_ebook.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.pdf_view.PdfViewActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.support.Utils;

import java.io.File;
import java.util.List;

public class EbookOfflineAdapter extends RecyclerView.Adapter<EbookOfflineAdapter.ViewHolder> {

    private final Activity context;
    List<File> list;

    public EbookOfflineAdapter(Activity context, List<File> list) {
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
        if (String.valueOf(list.get(position)).contains("/")) {
            int lastIndex = String.valueOf(list.get(position)).lastIndexOf("/");
            String name = String.valueOf(list.get(position)).substring(++lastIndex);
            viewHolder.tvEbookName.setText(name);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.Course.EBOOK_PATH, list.get(position).getAbsolutePath());
            bundle.putString(Constants.Course.EBOOK_NAME, viewHolder.tvEbookName.getText().toString());
            bundle.putString(Constants.Course.FROM_WHERE, "Offline");
            Utils.startActivityBundle(context, PdfViewActivity.class, bundle);
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
