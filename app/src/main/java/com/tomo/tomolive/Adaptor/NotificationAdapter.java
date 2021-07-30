package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemNotificationsFollowBinding;
import com.tomo.tomolive.databinding.ItemNotificationsLiveBinding;
import com.tomo.tomolive.databinding.ItemNotificationsPurchaseBinding;
import com.tomo.tomolive.models.NotificationRoot;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter {


    private static final int FOLLOWTYPE = 1;
    private static final int LIVETYPE = 2;
    private static final int PURCHASETYPE = 3;
    private List<NotificationRoot.DataItem> notifications = new ArrayList<>();
    private Context context;

    @Override
    public int getItemViewType(int position) {
        if (notifications.get(position).getType().equals("purchase")) {
            return PURCHASETYPE;
        } else if (notifications.get(position).getType().equals("follow")) {
            return FOLLOWTYPE;
        } else {
            return LIVETYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == FOLLOWTYPE) {
            return new FollowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications_follow, parent, false));
        } else if (viewType == LIVETYPE) {
            return new LiveViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications_live, parent, false));
        } else {
            return new PurchaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications_purchase, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FollowViewHolder) {
            FollowViewHolder followViewHolder = (FollowViewHolder) holder;
            followViewHolder.setData(position);
        } else if (holder instanceof LiveViewHolder) {
            LiveViewHolder liveViewHolder = (LiveViewHolder) holder;
            liveViewHolder.setData(position);
        } else {
            PurchaseViewHolder purchaseViewHolder = (PurchaseViewHolder) holder;
            purchaseViewHolder.setData(position);
        }

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void addData(List<NotificationRoot.DataItem> data) {
        for (int i = 0; i < data.size(); i++) {
            notifications.add(data.get(i));
            notifyItemInserted(notifications.size() - 1);
        }
    }


    class FollowViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationsFollowBinding binding;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNotificationsFollowBinding.bind(itemView);
        }

        public void setData(int position) {
            NotificationRoot.DataItem item = notifications.get(position);
            Glide.with(context).load(item.getImage()).circleCrop().into(binding.imgUser);
            binding.tvName.setText(item.getTitle());
            binding.tvdes.setText(item.getDescription());

        }
    }

    class LiveViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationsLiveBinding binding;

        public LiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNotificationsLiveBinding.bind(itemView);
        }

        public void setData(int position) {
            NotificationRoot.DataItem item = notifications.get(position);
            Glide.with(context).load(item.getImage()).circleCrop().into(binding.imgUser);
            binding.tvtitle.setText(item.getTitle());
            binding.tvdes.setText(item.getDescription());
            binding.tvtime.setText(item.getTime());
        }
    }

    class PurchaseViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationsPurchaseBinding binding;

        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNotificationsPurchaseBinding.bind(itemView);
        }

        public void setData(int position) {
            NotificationRoot.DataItem item = notifications.get(position);
            // Glide.with(context).load(item.getImage()).circleCrop().into(binding.imgUser);
            binding.tvtitle.setText(item.getTitle());
            binding.tvdes.setText(item.getDescription());
            binding.tvtime.setText(item.getTime());
        }
    }
}
