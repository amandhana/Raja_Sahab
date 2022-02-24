package com.rajasahabacademy.activity.payment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajasahabacademy.R;
import com.rajasahabacademy.activity.payment.activity.PaymentActivity;
import com.rajasahabacademy.activity.payment.model.Datum;
import com.rajasahabacademy.support.Utils;

import java.util.List;

public class PaymenrOffersAdapter extends RecyclerView.Adapter<PaymenrOffersAdapter.ViewHolder> {
    private final Activity context;
    List<Datum> list;

    public PaymenrOffersAdapter(Activity context, List<Datum> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tvOffer.setText(list.get(position).getName());
        if (list.get(position).isApply())
            viewHolder.tvApply.setText(context.getString(R.string.remove));
        else viewHolder.tvApply.setText(context.getString(R.string.apply));

        viewHolder.tvApply.setOnClickListener(view -> {
            if (!list.get(position).getAmount().equals("")) {
                if (viewHolder.tvApply.getText().toString().equals(context.getString(R.string.remove))) {
                    ((PaymentActivity) context).removeOffer();
                    resetAllOffer();
                } else {
                    ((PaymentActivity) context).applyOffer(list.get(position).getAmount());
                    resetAll(position);
                }
            } else
                Utils.showToastPopup(context, context.getString(R.string.apply_offer_validation));
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetAll(int position) {
        for (Datum model : list) {
            model.setApply(false);
        }
        list.get(position).setApply(true);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetAllOffer() {
        for (Datum model : list) {
            model.setApply(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvOffer;
        final TextView tvApply;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOffer = itemView.findViewById(R.id.tv_offer);
            tvApply = itemView.findViewById(R.id.tv_apply);
        }
    }
}

