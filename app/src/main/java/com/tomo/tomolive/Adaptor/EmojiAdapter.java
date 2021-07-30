package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.BuildConfig;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemEmojiBinding;
import com.tomo.tomolive.models.EmojiIconRoot;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    OnEmojiClickListnear onEmojiClickListnear;
    private List<EmojiIconRoot.Datum> data;
    private Context contect;

    public EmojiAdapter(List<EmojiIconRoot.Datum> data) {

        this.data = data;
    }

    public OnEmojiClickListnear getOnEmojiClickListnear() {
        return onEmojiClickListnear;
    }

    public void setOnEmojiClickListnear(OnEmojiClickListnear onEmojiClickListnear) {
        this.onEmojiClickListnear = onEmojiClickListnear;
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        EmojiIconRoot.Datum emoji = data.get(position);

        holder.binding.tvCoin.setText(String.valueOf(emoji.getCoin()));
        Glide.with(contect.getApplicationContext())
                .load(BuildConfig.BASE_URL + emoji.getIcon())
                .placeholder(R.drawable.ic_gift)
                .into(holder.binding.imgEmoji);
        holder.binding.imgEmoji.setOnClickListener(v -> {
            View content = holder.binding.imgEmoji;
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = content.getDrawingCache();
            onEmojiClickListnear.onEmojiClick(bitmap, emoji.getCoin(), emoji);
        });
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        contect = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnEmojiClickListnear {
        void onEmojiClick(Bitmap bitmap, Long coin, EmojiIconRoot.Datum emojiurl);
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder {
        ItemEmojiBinding binding;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemEmojiBinding.bind(itemView);
        }
    }
}
