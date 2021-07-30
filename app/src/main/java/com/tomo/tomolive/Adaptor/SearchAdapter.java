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
import com.tomo.tomolive.Activity.GuestProfileActivity;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ItemUsersBinding;
import com.tomo.tomolive.models.GuestUser;
import com.tomo.tomolive.retrofit.ApiCalling;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<GuestUser> data;

    public SearchAdapter(List<GuestUser> data) {

        this.data = data;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_users, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        GuestUser user = data.get(position);
        Glide.with(context).load(user.getImage()).circleCrop().into(holder.binding.imguser);
        holder.binding.tvusername.setText(user.getName());
        holder.binding.tvlastchet.setText(user.getUsername());
        holder.binding.tvcountry.setText(user.getCountry());

        if (user.isIsFollowing()) {
            holder.binding.lytfollow.setVisibility(View.GONE);
        } else {
            holder.binding.lytfollow.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, GuestProfileActivity.class).putExtra("user", new Gson().toJson(user))));


        holder.binding.lytfollow.setOnClickListener(v -> {


            ApiCalling apiCalling = new ApiCalling(context);
            apiCalling.followUser(context, new SessionManager(context).getUser().getId(), user.getId());
            apiCalling.setResponseListnear(new ApiCalling.ResponseListnear() {
                @Override
                public void responseSuccess() {
                    holder.binding.lytfollow.setVisibility(View.GONE);
                }

                @Override
                public void responseFail() {

                }
            });
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ItemUsersBinding binding;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemUsersBinding.bind(itemView);
        }
    }
}
