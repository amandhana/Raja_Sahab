package com.rajasahabacademy.activity.home.adapter;

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
import com.rajasahabacademy.activity.pdf_view.PdfViewActivity;
import com.rajasahabacademy.api.Constants;
import com.rajasahabacademy.activity.home.fragment.NotesFragment;
import com.rajasahabacademy.activity.home.model.notes.ResultNotes;
import com.rajasahabacademy.support.Utils;

import java.util.List;


public class NotesViewAdapter extends RecyclerView.Adapter<NotesViewAdapter.ViewHolder> {
    private final Activity context;
    List<ResultNotes> list;
    NotesFragment notesFragment;

    public NotesViewAdapter(Activity context, List<ResultNotes> list, NotesFragment notesFragment) {
        this.context = context;
        this.list = list;
        this.notesFragment = notesFragment;
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
        Utils.setImageUsingGlide(context, list.get(position).getThumbnail(), viewHolder.ivthumbnail);
        viewHolder.tvName.setText(list.get(position).getTitle());
        if (list.get(position).getPaid().equalsIgnoreCase("0")){
            viewHolder.ivView.setVisibility(View.GONE);
            viewHolder.tvAddCart.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivView.setVisibility(View.VISIBLE);
            viewHolder.tvAddCart.setVisibility(View.GONE);
        }
        if (list.get(position).getIsCart().equalsIgnoreCase("1"))
            viewHolder.tvAddCart.setText(context.getString(R.string.remove_cart));
        else viewHolder.tvAddCart.setText(context.getString(R.string.add_cart));

        viewHolder.ivView.setOnClickListener(view -> {
            if (!list.get(position).getPath().equals("")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Course.EBOOK_PATH, list.get(position).getPath());
                bundle.putString(Constants.Course.EBOOK_NAME, list.get(position).getTitle());
                bundle.putString(Constants.Course.FROM_WHERE, "");
                Utils.startActivityBundle(context, PdfViewActivity.class, bundle);
            } else
                Utils.showToastPopup(context, context.getResources().getString(R.string.ebook_path_valid));
        });

        viewHolder.tvAddCart.setOnClickListener(view -> {
            if (viewHolder.tvAddCart.getText().toString().equals(context.getString(R.string.add_cart)))
                notesFragment.addToCart(list.get(position).getId(),position);
            else notesFragment.removeCart(list.get(position).getId(),position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final ImageView ivthumbnail;
        final ImageView ivView;
        final TextView tvAddCart;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_course_name);
            ivView = itemView.findViewById(R.id.iv_view);
            tvAddCart = itemView.findViewById(R.id.tv_add_cart);
            ivthumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
