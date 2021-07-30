package com.tomo.tomolive.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemHistoryBinding;
import com.tomo.tomolive.models.CoinHistoryRoot;
import com.tomo.tomolive.models.RechargeHistoryRoot;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<CoinHistoryRoot.DataItem> data = new ArrayList<>();
    private int type;

    private List<RechargeHistoryRoot.DataItem> recharges = new ArrayList<>();

    public HistoryAdapter(List<CoinHistoryRoot.DataItem> dataItems, List<RechargeHistoryRoot.DataItem> recharges, int type) {
        this.recharges = recharges;

        this.data = dataItems;
        this.type = type;
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        if (type == 1) {
            holder.binding.tvcoin.setText(String.valueOf(data.get(position).getCoin()));
            holder.binding.tvdate.setText(String.valueOf(data.get(position).getDate()));
            holder.binding.tvrupees.setText("@" + data.get(position).getPerson());
        } else {
            holder.binding.tvcoin.setText(String.valueOf(recharges.get(position).getCoin()));
            holder.binding.tvdate.setText(recharges.get(position).getDate());
            holder.binding.tvrupees.setText(String.valueOf(recharges.get(position).getRupee()).concat("$"));
        }
    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return data.size();
        } else {
            return recharges.size();
        }

    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemHistoryBinding.bind(itemView);
        }
    }
}
