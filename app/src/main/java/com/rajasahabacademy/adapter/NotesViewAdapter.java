package com.rajasahabacademy.adapter;

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
import com.rajasahabacademy.activity.PdfViewActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.model.notes.ResultNotes;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class NotesViewAdapter extends RecyclerView.Adapter<NotesViewAdapter.ViewHolder> {
    private final Activity context;
    List<ResultNotes> list;

    public NotesViewAdapter(Activity context, List<ResultNotes> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(List<ResultNotes> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_notes_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvName.setText(list.get(position).getTitle());
        viewHolder.tvView.setOnClickListener(view -> {
            if (!list.get(position).getPath().equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Course.EBOOK_PATH, list.get(position).getPath());
                bundle.putString(Constants.Course.EBOOK_NAME, list.get(position).getTitle());
                bundle.putString(Constants.Course.FROM_WHERE, "");
                Utils.startActivityBundle(context, PdfViewActivity.class, bundle);
            } else
                Utils.showToastPopup(context, context.getResources().getString(R.string.ebook_path_valid));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_course_name);
            tvView = itemView.findViewById(R.id.tv_view);
        }
    }
}
