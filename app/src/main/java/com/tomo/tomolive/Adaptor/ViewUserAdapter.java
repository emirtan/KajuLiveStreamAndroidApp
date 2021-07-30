package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemUsersBinding;
import com.tomo.tomolive.models.ViewUserRoot;

import java.util.ArrayList;
import java.util.List;

public class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.ViewUserViewHOlder> {
    private Context context;
    private List<ViewUserRoot.DataItem> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewUserViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        return new ViewUserViewHOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewUserViewHOlder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).circleCrop().into(holder.binding.imguser);
        holder.binding.tvusername.setText(list.get(position).getName());
        holder.binding.tvcountry.setText(list.get(position).getCountryName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addUsers(List<ViewUserRoot.DataItem> data) {
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i));
            notifyItemInserted(list.size() - 1);
        }
    }

    public class ViewUserViewHOlder extends RecyclerView.ViewHolder {
        ItemUsersBinding binding;

        public ViewUserViewHOlder(@NonNull View itemView) {
            super(itemView);
            binding = ItemUsersBinding.bind(itemView);
        }
    }
}
