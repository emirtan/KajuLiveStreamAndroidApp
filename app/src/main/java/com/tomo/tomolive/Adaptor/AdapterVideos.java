package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.tomo.tomolive.Activity.FakeActivity;
import com.tomo.tomolive.Activity.WatchLiveActivity;
import com.tomo.tomolive.R;
import com.tomo.tomolive.databinding.ItemVideoBinding;
import com.tomo.tomolive.models.Thumb;

import java.util.ArrayList;
import java.util.List;

public class AdapterVideos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ADTYPE = 1;
    private static final int VIEWTYPE = 2;
    private final int adPosition = 2;
    private final int adSetPos = 0;
    public List<Thumb> data = new ArrayList<>();
    private Context context;
    private String cid;

    @Override
    public int getItemViewType(int position) {
       /* if (data.get(position) instanceof ThumbRoot.Datum) {
            return VIEWTYPE;
        } else {
            return ADTYPE;
        }*/
        return VIEWTYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == ADTYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new AdViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            return new VideoViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      /*  if (getItemViewType(position) == ADTYPE) {
            AdViewHolder adViewHolder = (AdViewHolder) holder;
            adViewHolder.setData(position);
        } else {

        }*/

        VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        videoViewHolder.setData(position);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<Thumb> data) {
        Log.d("TAG", "addData: " + data.size());
        for (int i = 0; i < data.size(); i++) {
            this.data.add(data.get(i));
            notifyItemInserted(this.data.size() - 1);
        }

    }

    public void clearAll() {
        this.data = new ArrayList<>();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {


        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(int position) {

        }


    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ItemVideoBinding binding;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemVideoBinding.bind(itemView);
        }

        public void setData(int position) {
            Thumb girl = data.get(position);
            binding.tvCountry.setText(girl.getCountryName());
            binding.tvName.setText(girl.getName());
            binding.tvcoin.setText(String.valueOf(girl.getCoin()));

            if (girl.getImage() != null && !girl.getImage().equals("")) {
                Glide.with(context).load(girl.getImage()).centerCrop().addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.tvchar.setVisibility(View.VISIBLE);
                        binding.tvchar.setText(String.valueOf(girl.getName().charAt(0)).toUpperCase());

                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.imgThumb.setImageDrawable(resource);
                        return true;
                    }
                }).into(binding.imgThumb);
            } else {
                binding.tvchar.setVisibility(View.VISIBLE);
                binding.tvchar.setText(String.valueOf(girl.getName().charAt(0)).toUpperCase());

            }

            binding.tvview.setText(String.valueOf(girl.getView()));

            if (girl.isFake()) {
                binding.imgThumb.setOnClickListener(v -> context.startActivity(new Intent(context, FakeActivity.class).putExtra("model", new Gson().toJson(girl))));

            } else {
                binding.imgThumb.setOnClickListener(v -> context.startActivity(new Intent(context, WatchLiveActivity.class).putExtra("model", new Gson().toJson(girl))));
            }/* Random rand = new Random();
            int randInt1 = rand.nextInt((9999 - 999) + 1) + 999;

            double coin;
            if (randInt1 >= 1000) {
                coin = (double) randInt1 / 1000;
                DecimalFormat df = new DecimalFormat("#.##");

                binding.tvCoins.setText(df.format(coin).concat("K"));
            } else {
                coin = randInt1;
                binding.tvCoins.setText(String.valueOf(coin));
            }*/
            // Log.d("TAG", "onBindViewHolder: " + randInt1);
            // Log.d("TAG", "onBindViewHolder: " + coin);


        }
    }

}
