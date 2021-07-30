package com.tomo.tomolive.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemChatBinding;
import com.tomo.tomolive.models.OriginalMessageRoot;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapterOriginal extends RecyclerView.Adapter<ChatAdapterOriginal.ChatViewholder> {


    private List<OriginalMessageRoot.DataItem> list = new ArrayList<>();

    @NonNull
    @Override
    public ChatAdapterOriginal.ChatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewholder holder, int position) {


        OriginalMessageRoot.DataItem messageRoot = list.get(position);
        holder.binding.tvrobot.setVisibility(View.GONE);
        holder.binding.tvuser.setVisibility(View.GONE);
        holder.binding.lytimagerobot.setVisibility(View.GONE);
        holder.binding.lytimageuser.setVisibility(View.GONE);
        if (messageRoot.getFlag() == 1) {
            if (!messageRoot.getMessage().equals("")) {
                holder.binding.tvuser.setText(messageRoot.getMessage());
                holder.binding.tvuser.setVisibility(View.VISIBLE);
            }


        } else {
            if (!messageRoot.getMessage().equals("")) {
                holder.binding.tvrobot.setText(messageRoot.getMessage());
                holder.binding.tvrobot.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<OriginalMessageRoot.DataItem> data) {

        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i));
            notifyItemInserted(list.size() - 1);
        }
    }

    public void addSingleMessage(OriginalMessageRoot.DataItem dataItem) {
        list.add(dataItem);
        notifyItemInserted(list.size() - 1);
    }


    public class ChatViewholder extends RecyclerView.ViewHolder {
        ItemChatBinding binding;

        public ChatViewholder(@NonNull View itemView) {
            super(itemView);
            binding = ItemChatBinding.bind(itemView);
        }
    }
}
