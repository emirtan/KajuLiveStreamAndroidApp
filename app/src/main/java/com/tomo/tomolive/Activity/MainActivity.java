package com.tomo.tomolive.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tomo.tomolive.Fregment.MessageFregment;
import com.tomo.tomolive.Fregment.ProfileFregment;
import com.tomo.tomolive.Fregment.SearchFregment;
import com.tomo.tomolive.Fregment.VideoFregment;
import com.tomo.tomolive.LivexUtils;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityMainBinding;
import com.tomo.tomolive.models.ChatUserListRoot;
import com.tomo.tomolive.models.User;
import com.tomo.tomolive.models.UserRoot;
import com.tomo.tomolive.oflineModels.NotificationIntent;
import com.tomo.tomolive.popus.ChangeRatePopup;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity {
    private static final String TAG = "mainact";
    ActivityMainBinding binding;
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    SessionManager sessionManager;
    private String userId;
    public static boolean IS_HOST_LIVE = false;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: isHostLive?? =" + IS_HOST_LIVE);
            if (IS_HOST_LIVE) {
                LivexUtils.informBackendHostIsLive(userId);
            }

            handler.postDelayed(runnable, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.IS_HOST_LIVE = false;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sessionManager = new SessionManager(this);
        userId = sessionManager.getUser().getId();
        getIntentData();
        Glide.with(this).asGif().load(R.drawable.liveanimationbutton).circleCrop().into(binding.liveVideoWhite);
        initMain();
        getChatUserList();

        initTimerGlobal();
    }

    private void initTimerGlobal() {

        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.IS_HOST_LIVE = false;
    }

    private void getChatUserList() {

        Log.d(TAG, "getChatUserList: ");
        Call<ChatUserListRoot> call = RetrofitBuilder.create().getChatUserList(Const.DEVKEY, userId);
        call.enqueue(new Callback<ChatUserListRoot>() {
            @Override
            public void onResponse(Call<ChatUserListRoot> call, Response<ChatUserListRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && !response.body().getData().isEmpty()) {
                        Log.d(TAG, "onResponse: size " + response.body().getData().size());
                        String c = sessionManager.getStringValue(Const.CHAT_COUNT);
                        Log.d(TAG, "onResponse: local str " + c);
                        if (!c.equals("")) {
                            int chatCount = Integer.parseInt(c);
                            Log.d(TAG, "onResponse: int count " + chatCount);
                            if (chatCount > response.body().getData().size()) {
                                binding.unreadbuttom.setVisibility(View.VISIBLE);
                            } else {
                                binding.unreadbuttom.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            binding.unreadbuttom.setVisibility(View.INVISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ChatUserListRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {


            String objStr = intent.getStringExtra(Const.notificationIntent);
            if (objStr != null && !objStr.equals("")) {
                NotificationIntent notificationIntent = new Gson().fromJson(objStr, NotificationIntent.class);
                if (notificationIntent.getType() == NotificationIntent.CHAT) {

                    startActivity(new Intent(this, ChatListActivityOriginal.class).putExtra("hostid", notificationIntent.getHostid())
                            .putExtra("name", notificationIntent.getName()).putExtra("image", notificationIntent.getImage()));

                    setDefultBottomBar();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MessageFregment()).commit();
                    binding.msgWhite.setVisibility(View.GONE);
                    binding.msgColor.setVisibility(View.VISIBLE);
                } else if (notificationIntent.getType() == NotificationIntent.LIVE) {
                    startActivity(new Intent(this, WatchLiveActivity.class).putExtra("model", new Gson().toJson(notificationIntent.getThumb())));
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (binding.homeWhite.getVisibility() == View.GONE) {
            finish();
        } else {
            setDefultBottomBar();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new VideoFregment()).commit();
            binding.homeWhite.setVisibility(View.GONE);
            binding.homeColor.setVisibility(View.VISIBLE);
        }
    }

    private void initMain() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, new VideoFregment()).commit();

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new VideoFregment()).commit();
                binding.homeWhite.setVisibility(View.GONE);
                binding.homeColor.setVisibility(View.VISIBLE);
                // change image
            }
        });

        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFregment()).commit();
                binding.likeWhite.setVisibility(View.GONE);
                binding.likeColor.setVisibility(View.VISIBLE);

                // change image
            }
        });
        binding.liveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = sessionManager.getUser();

                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                    if (user != null) {
                        if (user.getRate() == 0) {
                            ChangeRatePopup changeRatePopup = new ChangeRatePopup(MainActivity.this, user);
                            changeRatePopup.setOnSubmitClickListnear(rate -> {
                                Log.d("TAG", "submit: ");
                                updateRate(rate);
                            });
                        } else {
                            setDefultBottomBar();
                            Intent intent = new Intent(MainActivity.this, HostActivity.class);
                            startActivity(intent);
                        }
                    }

                    Log.d(TAG, "onCreate: permisson");
                } else {

                    Log.d(TAG, "onCreate: permisson denied");
                }


            }
        });
        binding.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MessageFregment()).commit();
                binding.msgWhite.setVisibility(View.GONE);
                binding.msgColor.setVisibility(View.VISIBLE);
                binding.unreadbuttom.setVisibility(View.INVISIBLE);
                // change image
            }
        });
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefultBottomBar();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFregment()).commit();
                binding.profileWhite.setVisibility(View.GONE);
                binding.profileColor.setVisibility(View.VISIBLE);

                // change image
            }
        });

    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
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
                        Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        sessionManager.saveUser(response.body().getUser());
                        setDefultBottomBar();
                        Intent intent = new Intent(MainActivity.this, HostActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {

            }
        });
    }

    private void setDefultBottomBar() {
        binding.homeWhite.setVisibility(View.VISIBLE);
        binding.likeWhite.setVisibility(View.VISIBLE);
        binding.liveVideoWhite.setVisibility(View.VISIBLE);
        binding.msgWhite.setVisibility(View.VISIBLE);
        binding.profileWhite.setVisibility(View.VISIBLE);

        binding.homeColor.setVisibility(View.GONE);
        binding.likeColor.setVisibility(View.GONE);

        binding.msgColor.setVisibility(View.GONE);
        binding.profileColor.setVisibility(View.GONE);


    }

}