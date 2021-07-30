package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemSearchHistoryBinding;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder> {
    private Context context;
    private ArrayList<String> history = new ArrayList<>();
    private OnHistoryItemClickListner onHistoryItemClickListner;


    public SearchHistoryAdapter(ArrayList<String> history, OnHistoryItemClickListner onHistoryItemClickListner) {

        this.history = history;
        this.onHistoryItemClickListner = onHistoryItemClickListner;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SearchHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        holder.binding.tvname.setText(history.get(position).toString());
        holder.itemView.setOnClickListener(v -> onHistoryItemClickListner.onHistoryClick(history.get(position)));
        holder.binding.btnclose.setOnClickListener(v -> onHistoryItemClickListner.onDeleteClick(history.get(position)));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public interface OnHistoryItemClickListner {


        void onHistoryClick(String s);

        void onDeleteClick(String s);
    }

    public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        ItemSearchHistoryBinding binding;

        public SearchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSearchHistoryBinding.bind(itemView);
        }
    }
}
