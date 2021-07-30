package com.tomo.tomolive.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.JsonObject;
import com.tomo.tomolive.Adaptor.DailyCoinAdaptor;
import com.tomo.tomolive.Adaptor.MoreCoinAdapter;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityMyWalletBinding;
import com.tomo.tomolive.models.DailyTaskRoot;
import com.tomo.tomolive.models.PlanRoot;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.models.User;
import com.tomo.tomolive.models.UserRoot;
import com.tomo.tomolive.popus.ChangeRatePopup;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletActivity extends BaseActivity implements BillingProcessor.IBillingHandler {
    ActivityMyWalletBinding binding;

    private BillingProcessor bp;
    SessionManager sessionManager;
    private String planId;
    private String userId;
    private User user;
    private int currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_wallet);

        MainActivity.IS_HOST_LIVE = false;
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUser().getId();
        getUserData();
        checkDailyTask();
        getPlanList();
    }

    private void checkDailyTask() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userId);
        Call<DailyTaskRoot> call = RetrofitBuilder.create().checkDailyTask(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<DailyTaskRoot>() {
            @Override
            public void onResponse(Call<DailyTaskRoot> call, Response<DailyTaskRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        currentTask = response.body().getNumber();
                        setDailyTaskList();
                    }
                }
            }

            @Override
            public void onFailure(Call<DailyTaskRoot> call, Throwable t) {

            }
        });
    }

    private void setDailyTaskList() {
        DailyCoinAdaptor dailyCoinAdaptor = new DailyCoinAdaptor(currentTask, new DailyCoinAdaptor.DailyCoinClickListnear() {
            @Override
            public void onCardClick() {

                getUserData();
                checkDailyTask();
            }
        });
        binding.rvDailyCoin.setAdapter(dailyCoinAdaptor);
    }

    private void getUserData() {
        Call<UserRoot> call = RetrofitBuilder.create().getUserProfile(Const.DEVKEY, userId);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getUser() != null) {
                        user = response.body().getUser();
                        sessionManager.saveUser(user);
                        binding.tvBalancde.setText(String.valueOf(user.getCoin()));
                        binding.tvrate.setText(String.valueOf(user.getRate()));

                        binding.lytrate.setOnClickListener(v -> {
                            ChangeRatePopup changeRatePopup = new ChangeRatePopup(MyWalletActivity.this, user);
                            changeRatePopup.setOnSubmitClickListnear(rate -> {
                                Log.d("TAG", "submit: ");
                                updateRate(rate);
                            });
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {

            }
        });
    }

    private void updateRate(String rate) {
        RequestBody coin = RequestBody.create(MediaType.parse("text/plain"), rate);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userId);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user_id", userid);
        map.put("rate", coin);

        Call<UserRoot> call = RetrofitBuilder.create().updateUser(Const.DEVKEY, map);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getUser() != null) {
                        Toast.makeText(MyWalletActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        sessionManager.saveUser(response.body().getUser());

                    } else {
                        Toast.makeText(MyWalletActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {

            }
        });
    }

    private void getPlanList() {
        Call<PlanRoot> call = RetrofitBuilder.create().getPlanList(Const.DEVKEY);
        call.enqueue(new Callback<PlanRoot>() {
            @Override
            public void onResponse(Call<PlanRoot> call, Response<PlanRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && !response.body().getData().isEmpty()) {
                        MoreCoinAdapter moreCoinAdapter = new MoreCoinAdapter(response.body().getData(), new MoreCoinAdapter.OnBuyCoinClickListnear() {
                            @Override
                            public void onButClick(PlanRoot.DataItem dataItem) {
                                buyItem(dataItem);
                            }
                        });
                        binding.rvMoreCoins.setAdapter(moreCoinAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlanRoot> call, Throwable t) {

            }
        });
    }

    private void buyItem(PlanRoot.DataItem dataItem) {
        planId = dataItem.getId();
        bp = new BillingProcessor(this, getResources().getString(R.string.play_lic_key), this);
        bp.initialize();
        bp.purchase(this, "android.test.purchased");
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        bp.consumePurchase(productId);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_user_id", userId);
        jsonObject.addProperty("plan_id", planId);
        Call<RestResponse> call = RetrofitBuilder.create().purchaseCoin(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        Toast.makeText(MyWalletActivity.this, "Purchased", Toast.LENGTH_SHORT).show();
                        getUserData();
                    } else {
                        Toast.makeText(MyWalletActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Toast.makeText(MyWalletActivity.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        bp.purchase(this, "android.test.purchased");
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClickBack(View view) {
        finish();
    }
}