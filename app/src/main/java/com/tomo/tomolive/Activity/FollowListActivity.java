package com.tomo.tomolive.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.tomo.tomolive.Adaptor.FollowAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityFollowListBinding;
import com.tomo.tomolive.models.GuestUserRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowListActivity extends AppCompatActivity {
    ActivityFollowListBinding binding;
    SessionManager sessionManager;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_follow_list);
        MainActivity.IS_HOST_LIVE = false;
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUser().getId();
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            binding.tvtitle.setText(title);
            if (title.equals("Followrs")) {
                getFollowrsList();
            } else {
                getFollowingList();
            }
        }

    }

    private void getFollowingList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        Call<GuestUserRoot> call = RetrofitBuilder.create().followingList(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<GuestUserRoot>() {
            @Override
            public void onResponse(Call<GuestUserRoot> call, Response<GuestUserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        FollowAdapter followAdapter = new FollowAdapter(response.body().getData());
                        binding.rvList.setAdapter(followAdapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<GuestUserRoot> call, Throwable t) {

            }
        });
    }

    private void getFollowrsList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        Call<GuestUserRoot> call = RetrofitBuilder.create().followrsList(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<GuestUserRoot>() {
            @Override
            public void onResponse(Call<GuestUserRoot> call, Response<GuestUserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        FollowAdapter followAdapter = new FollowAdapter(response.body().getData());
                        binding.rvList.setAdapter(followAdapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<GuestUserRoot> call, Throwable t) {

            }
        });
    }

    public void onClickBack(View view) {
        finish();
    }
}