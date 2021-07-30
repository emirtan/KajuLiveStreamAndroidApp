package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.BuildConfig;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemGifsBinding;
import com.tomo.tomolive.models.StikerRoot;

import java.util.ArrayList;
import java.util.List;

public class StikerHostAdapter extends RecyclerView.Adapter<StikerHostAdapter.EmojiViewHolder> {
    private Context context;
    private OnStikerClickListnear onGifClickListnear;
    private List<StikerRoot.DataItem> stikers = new ArrayList<>();


    public StikerHostAdapter(List<StikerRoot.DataItem> stikers, OnStikerClickListnear onGifClickListnear) {

        this.stikers = stikers;
        this.onGifClickListnear = onGifClickListnear;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EmojiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gifs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {

        StikerRoot.DataItem stiker = stikers.get(position);
        Log.d("TAG", "onBindViewHolder: " + stiker.getSticker());
        Glide.with(context).asGif().load(BuildConfig.BASE_URL + stiker.getSticker()).centerCrop().into(holder.binding.imgEmoji);
        holder.binding.imgEmoji.setOnClickListener(v -> onGifClickListnear.onStikerClick(stiker));
    }

    @Override
    public int getItemCount() {
        return stikers.size();
    }

    public interface OnStikerClickListnear {
        void onStikerClick(StikerRoot.DataItem stiker);
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        ItemGifsBinding binding;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemGifsBinding.bind(itemView);
        }
    }
}
