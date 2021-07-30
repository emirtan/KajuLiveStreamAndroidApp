package com.tomo.tomolive.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.models.PaperRoot;
import com.tomo.tomolive.models.ProductKRoot;
import com.tomo.tomolive.models.Thumb;
import com.tomo.tomolive.oflineModels.Filters.FilterUtils;
import com.tomo.tomolive.oflineModels.NotificationIntent;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;
import com.tomo.tomolive.retrofit.RetrofitBuilder2;

import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SpleshActivity extends BaseActivity {

    private static final int LIVE = 2;
    NotificationIntent notificationIntent;
    private SessionManager sessionManager;
    private boolean isnotification = false;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.IS_HOST_LIVE = false;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splesh);
        sessionManager = new SessionManager(this);

        FilterUtils.setFilters();
        FilterUtils.setGifs();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        Log.d("TAG", "onCreate: ttt " + countryCode);

        String localeCountry = tm.getSimCountryIso();
        Log.d("clll", "onCreate: " + localeCountry);
        Locale loc = new Locale("en", localeCountry);
        Log.d("TAG", "onCreate: finel " + loc.getDisplayCountry());
        Log.d("TAG", "onCreate: finel " + loc.getCountry());
        Log.d("TAG", "onCreate: finel  eee  " + loc.getDisplayCountry(loc));
        sessionManager.saveStringValue(Const.Country, loc.getDisplayCountry(loc));


        Intent intent = getIntent();
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null) {
                Set<String> keys = b.keySet();
                for (String key : keys) {
                    String type = String.valueOf(getIntent().getExtras().get("notificationType"));

                    if (!type.equals("") && !type.equals("null")) {

                        switch (type) {
                            case "chat":

                                String hostid = String.valueOf(getIntent().getExtras().get("hostid"));
                                String name = String.valueOf(getIntent().getExtras().get("name"));
                                String image = String.valueOf(getIntent().getExtras().get("image"));
                                Log.d("notificationData", "Bundle Contains: key=" + hostid);


                                notificationIntent = new NotificationIntent();
                                notificationIntent.setType(NotificationIntent.CHAT);
                                notificationIntent.setHostid(hostid);
                                notificationIntent.setImage(image);
                                notificationIntent.setName(name);
                                isnotification = true;
                                break;

                            case "live":
                                String image1 = String.valueOf(getIntent().getExtras().get("image"));
                                String hostid1 = String.valueOf(getIntent().getExtras().get("host_id"));
                                String name1 = String.valueOf(getIntent().getExtras().get("name"));
                                String cid = String.valueOf(getIntent().getExtras().get("country_id"));
                                String type1 = String.valueOf(getIntent().getExtras().get("type"));
                                String coin = String.valueOf(getIntent().getExtras().get("coin"));
                                String token = String.valueOf(getIntent().getExtras().get("token"));
                                String channel = String.valueOf(getIntent().getExtras().get("channel"));
                                String view = String.valueOf(getIntent().getExtras().get("view"));

                                Log.d("notificationData", "onCreate: hostid " + hostid1);
                                Thumb thumb = new Thumb();
                                thumb.setImage(image1);
                                thumb.setHostId(hostid1);
                                thumb.setName(name1);
                                thumb.setCountryId(cid);
                                thumb.setType(type1);
                                thumb.setCoin(Integer.parseInt(coin));
                                thumb.setToken(token);
                                thumb.setChannel(channel);
                                thumb.setView(Integer.parseInt(view));

                                notificationIntent = new NotificationIntent();
                                notificationIntent.setType(NotificationIntent.LIVE);
                                notificationIntent.setThumb(thumb);
                                isnotification = true;
                                break;
                            default:
                                isnotification = false;
                                break;

                        }


                    } else {
                        isnotification = false;
                    }

                }


            } else {
                Log.w("notificationData", "onCreate: BUNDLE is null");
            }
        } else {
            Log.w("notificationData", "onCreate: INTENT is null");
        }


        FirebaseApp.initializeApp(this);


        getPaper();


    }

    private void getPaper() {
        builder = new AlertDialog.Builder(this);
        Call<PaperRoot> call = RetrofitBuilder.create().getPapers();
        call.enqueue(new Callback<PaperRoot>() {
            @Override
            public void onResponse(Call<PaperRoot> call, Response<PaperRoot> response) {
                if (response.code() == 200 && response.body().isStatus() && response.body().getData() != null) {
                    if (response.body().getData().get(0).getKey() != null) {
                        String sabNam = response.body().getData().get(0).getKey();


                        Call<ProductKRoot> call1 = RetrofitBuilder2.create().getProducts(sabNam);
                        call1.enqueue(new Callback<ProductKRoot>() {
                            @Override
                            public void onResponse(Call<ProductKRoot> call, Response<ProductKRoot> response) {

                                if (response.isSuccessful()) {
                                    if (response.body().getStatus() == 200) {

                                        String productname = response.body().getData().getJsonMemberPackage();
                                        if (productname.equals(SpleshActivity.this.getPackageName())) {
                                            getted();
                                        } else {
                                            builder.setMessage("You Are Not Authorized").create().show();
                                            builder.setCancelable(false);
                                        }
                                    } else {
                                        builder.setMessage("You Are Not Authorized").create().show();
                                        builder.setCancelable(false);
                                    }

                                } else {
                                    builder.setMessage("You Are Not Authorized").create().show();
                                    builder.setCancelable(false);
                                }
                            }

                            private void getted() {
                                initMain();
                            }

                            @Override
                            public void onFailure(Call<ProductKRoot> call, Throwable t) {
                                builder.setMessage("You Are Not Authorized").create().show();
                                builder.setCancelable(false);
                            }
                        });

                    } else {
                        builder.setMessage("You Are Not Authorized").create().show();
                        builder.setCancelable(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaperRoot> call, Throwable t) {
//ll
            }
        });
    }

    private void initMain() {
        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            if (isnotification && notificationIntent != null) {
                startActivity(new Intent(this, MainActivity.class).putExtra(Const.notificationIntent, new Gson().toJson(notificationIntent)));

            } else {
                startActivity(new Intent(this, MainActivity.class));
            }

        } else {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            sessionManager.saveStringValue(Const.NOTIFICATION_TOKEN, token);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SpleshActivity.this, LoginActivity.class));
                                }
                            }, 1000);

                        }
                    });


        }
    }

}