package com.tomo.tomolive.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.tomo.tomolive.Adaptor.NotificationAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityNotificationBinding;
import com.tomo.tomolive.models.NotificationRoot;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {
    ActivityNotificationBinding binding;
    SessionManager sessionManager;
    NotificationAdapter notificationAdapter = new NotificationAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        sessionManager = new SessionManager(this);
        getNotifications();
        MainActivity.IS_HOST_LIVE = false;
    }

    private void getNotifications() {
        Call<NotificationRoot> call = RetrofitBuilder.create().getNotifications(sessionManager.getUser().getId());
        call.enqueue(new Callback<NotificationRoot>() {
            @Override
            public void onResponse(Call<NotificationRoot> call, Response<NotificationRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        notificationAdapter.addData(response.body().getData());

                        binding.rvNotifications.setAdapter(notificationAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationRoot> call, Throwable t) {

            }
        });
    }

    public void onClickBack(View view) {
        onBackPressed();
    }
}