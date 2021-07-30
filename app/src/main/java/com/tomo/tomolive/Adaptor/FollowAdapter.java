package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tomo.tomolive.Activity.ChatListActivityOriginal;
import com.tomo.tomolive.Activity.GuestProfileActivity;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemFollowrsBinding;
import com.tomo.tomolive.models.GuestUser;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.FollowViewHolder> {
    private Context context;
    private List<GuestUser> data;

    public FollowAdapter(List<GuestUser> data) {

        this.data = data;
    }

    @NonNull
    @Override
    public FollowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new FollowViewHolder(LayoutInflater.from(context).inflate(R.layout.item_followrs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FollowViewHolder holder, int position) {

        GuestUser user = data.get(position);
        Glide.with(context).load(user.getImage()).circleCrop().into(holder.binding.imguser);
        holder.binding.tvname.setText(user.getName());
        holder.binding.tvusername.setText(user.getUsername());

        holder.binding.lytchat.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ChatListActivityOriginal.class).putExtra("name", user.getName())
                    .putExtra("image", user.getImage())
                    .putExtra("hostid", user.getId()));
        });
        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, GuestProfileActivity.class).putExtra("user", new Gson().toJson(user)));
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class FollowViewHolder extends RecyclerView.ViewHolder {
        ItemFollowrsBinding binding;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFollowrsBinding.bind(itemView);
        }
    }
}
