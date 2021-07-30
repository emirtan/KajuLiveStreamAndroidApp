package com.tomo.tomolive.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemBuycoinsBinding;
import com.tomo.tomolive.models.PlanRoot;

import java.util.List;

public class MoreCoinAdapter extends RecyclerView.Adapter<MoreCoinAdapter.MoreCoinViewHolder> {
    private List<PlanRoot.DataItem> data;
    private OnBuyCoinClickListnear onBuyCoinClickListnear;

    public MoreCoinAdapter(List<PlanRoot.DataItem> data, OnBuyCoinClickListnear onBuyCoinClickListnear) {

        this.data = data;
        this.onBuyCoinClickListnear = onBuyCoinClickListnear;
    }

    @NonNull
    @Override
    public MoreCoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoreCoinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buycoins, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoreCoinViewHolder holder, int position) {
        holder.buycoinsBinding.tvamount.setText(String.valueOf(data.get(position).getRupee()));
        holder.buycoinsBinding.tvcoin.setText(String.valueOf(data.get(position).getCoin()));
        holder.buycoinsBinding.tvbuy.setOnClickListener(v -> {
            onBuyCoinClickListnear.onButClick(data.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnBuyCoinClickListnear {
        void onButClick(PlanRoot.DataItem dataItem);
    }

    public class MoreCoinViewHolder extends RecyclerView.ViewHolder {
        ItemBuycoinsBinding buycoinsBinding;

        public MoreCoinViewHolder(@NonNull View itemView) {
            super(itemView);
            buycoinsBinding = ItemBuycoinsBinding.bind(itemView);
        }
    }
}
