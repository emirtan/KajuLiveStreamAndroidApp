package com.tomo.tomolive.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityGuestProfileBinding;
import com.tomo.tomolive.models.GuestUser;
import com.tomo.tomolive.retrofit.ApiCalling;

public class GuestProfileActivity extends BaseActivity {

    ActivityGuestProfileBinding binding;
    private GuestUser guestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_profile);
        MainActivity.IS_HOST_LIVE = false;
        getIntentData();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        String objStr = intent.getStringExtra("user");
        if (objStr != null && !objStr.equals("")) {
            guestUser = new Gson().fromJson(objStr, GuestUser.class);
            if (guestUser != null) {
                setUserData();
                initListner();
            }
        }
    }

    private void initListner() {
        binding.lytfollow.setOnClickListener(v -> {

            followUser();

        });
        binding.lytunfollow.setOnClickListener(v -> {

            unFOllowUser();

        });
        binding.lytchat.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatListActivityOriginal.class).putExtra("name", guestUser.getName())
                    .putExtra("image", guestUser.getImage())
                    .putExtra("hostid", guestUser.getId()));

        });
    }

    private void followUser() {
        ApiCalling apiCalling = new ApiCalling(this);
        apiCalling.followUser(this, new SessionManager(this).getUser().getId(), guestUser.getId());
        apiCalling.setResponseListnear(new ApiCalling.ResponseListnear() {
            @Override
            public void responseSuccess() {
                binding.lytfollow.setVisibility(View.GONE);
                binding.lytunfollow.setVisibility(View.VISIBLE);
            }

            @Override
            public void responseFail() {

            }
        });
    }

    private void unFOllowUser() {
        ApiCalling apiCalling = new ApiCalling(this);
        apiCalling.unfollowUser(this, new SessionManager(this).getUser().getId(), guestUser.getId());
        apiCalling.setResponseListnear(new ApiCalling.ResponseListnear() {
            @Override
            public void responseSuccess() {
                binding.lytunfollow.setVisibility(View.GONE);
                binding.lytfollow.setVisibility(View.VISIBLE);
            }

            @Override
            public void responseFail() {

            }
        });

    }

    private void setUserData() {
        Glide.with(this).load(guestUser.getImage()).centerCrop().into(binding.imggirl);
        binding.tvcoin.setText(String.valueOf(guestUser.getCoin()));
        binding.tvfollowing.setText(String.valueOf(guestUser.getFollowingCount()));
        binding.tvfollowrs.setText(String.valueOf(guestUser.getFollowersCount()));
        binding.tvlocation.setText(guestUser.getCountry());
        binding.tvName.setText(guestUser.getName());
        binding.tvusername.setText(guestUser.getUsername());


        if (guestUser.isIsFollowing()) {
            binding.lytfollow.setVisibility(View.GONE);
            binding.lytunfollow.setVisibility(View.VISIBLE);

        } else {
            binding.lytunfollow.setVisibility(View.GONE);
            binding.lytfollow.setVisibility(View.VISIBLE);
        }



    }

    public void onClickBack(View view) {
        finish();
    }
}