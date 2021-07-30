package com.tomo.tomolive.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ItemDailyCoinColorBinding;
import com.tomo.tomolive.databinding.ItemDailyCoinWhiteBinding;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyCoinAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int White = 1;
    private static final int Color = 2;
    private Context context;
    private int currentTask;
    private DailyCoinClickListnear dailyCoinClickListnear;

    public DailyCoinAdaptor(int currentTask, DailyCoinClickListnear dailyCoinClickListnear) {

        this.currentTask = currentTask;
        this.dailyCoinClickListnear = dailyCoinClickListnear;
    }

    @Override
    public int getItemViewType(int position) {

        if (currentTask > position) {
            return White;
        } else {
            return Color;
        }

    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == White) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_coin_white, parent, false);
            return new WhiteViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_coin_color, parent, false);
            return new ColorViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ColorViewHolder) {
            ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
            Random rand = new Random();
            int random_integer = rand.nextInt(300 - 100) + 100;
            colorViewHolder.binding.tvcoin.setText(String.valueOf(random_integer));

            colorViewHolder.binding.imgcoin.setOnClickListener(v -> {
                Log.d("TAG", "onBindViewHolder:" + currentTask + " select ==" + position);
                if (currentTask == position) {
                    colorViewHolder.binding.imgcoin.setVisibility(View.GONE);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("coin", random_integer);
                    jsonObject.addProperty("user_id", new SessionManager(context).getUser().getId());
                    Call<RestResponse> call = RetrofitBuilder.create().updateDailyTask(Const.DEVKEY, jsonObject);
                    call.enqueue(new Callback<RestResponse>() {
                        @Override
                        public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                            if (response.code() == 200 && response.body().isStatus()) {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                dailyCoinClickListnear.onCardClick();

                            } else {
                                colorViewHolder.binding.imgcoin.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<RestResponse> call, Throwable t) {
                            colorViewHolder.binding.imgcoin.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    Toast.makeText(context, "Try after 24 hours", Toast.LENGTH_SHORT).show();
                }
            });
        } else {


        }
    }

    public interface DailyCoinClickListnear {
        void onCardClick();
    }
    @Override
    public int getItemCount() {
        return 10;
    }

    public class WhiteViewHolder extends RecyclerView.ViewHolder {
        ItemDailyCoinWhiteBinding binding;

        public WhiteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDailyCoinWhiteBinding.bind(itemView);
        }
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ItemDailyCoinColorBinding binding;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemDailyCoinColorBinding.bind(itemView);
        }
    }
}
