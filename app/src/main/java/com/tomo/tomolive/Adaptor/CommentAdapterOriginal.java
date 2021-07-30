package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemCommentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommentAdapterOriginal extends RecyclerView.Adapter<CommentAdapterOriginal.CommentViewHolder> {
    Random random = new Random();
    private Context context;
    private List<JSONObject> data = new ArrayList<JSONObject>();


    public CommentAdapterOriginal() {
//ll

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        JSONObject jsonObject = data.get(position);
        String name = null;
        try {
            name = jsonObject.get("name").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (name != null && !name.equals("")) {
            holder.binding.tvName.setText(name);
        }
        String message = null;
        try {
            message = jsonObject.get("comment").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (message != null && !message.equals("")) {
            holder.binding.tvcomment.setText(message);
        }

        setrandomColor(holder.binding.tvName);
    }

    private void setrandomColor(TextView tvName) {


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void addComment(JSONObject toString) {
        data.add(toString);
        notifyItemInserted(data.size() - 1);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentBinding.bind(itemView);
        }
    }
}
