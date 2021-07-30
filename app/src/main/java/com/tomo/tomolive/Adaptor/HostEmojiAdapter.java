package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.BuildConfig;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemGifsBinding;
import com.tomo.tomolive.models.HostEmojiRoot;

import java.util.List;

public class HostEmojiAdapter extends RecyclerView.Adapter<HostEmojiAdapter.EmojiViewHolder> {
    private Context context;
    private List<HostEmojiRoot.DataItem> hostEmojis;
    private OnEmojiClickListnear onEmojiClickListnear;

    public HostEmojiAdapter(List<HostEmojiRoot.DataItem> hostEmojis, OnEmojiClickListnear onEmojiClickListnear) {

        this.hostEmojis = hostEmojis;
        this.onEmojiClickListnear = onEmojiClickListnear;
    }


    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EmojiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gifs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        HostEmojiRoot.DataItem emoji = hostEmojis.get(position);
        Glide.with(context).load(BuildConfig.BASE_URL + emoji.getEmoji()).centerCrop().into(holder.binding.imgEmoji);
        holder.binding.imgEmoji.setOnClickListener(v -> onEmojiClickListnear.onEmojiClick(emoji));
    }

    @Override
    public int getItemCount() {
        return hostEmojis.size();
    }

    public interface OnEmojiClickListnear {
        void onEmojiClick(HostEmojiRoot.DataItem emoji);
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        ItemGifsBinding binding;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemGifsBinding.bind(itemView);
        }
    }
}
