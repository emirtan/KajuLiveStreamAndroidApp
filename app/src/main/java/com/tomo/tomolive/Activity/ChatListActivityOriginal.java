package com.tomo.tomolive.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.tomo.tomolive.Adaptor.ChatAdapterOriginal;
import com.tomo.tomolive.BuildConfig;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityChatListOriginalBinding;
import com.tomo.tomolive.models.ChatSendRoot;
import com.tomo.tomolive.models.ChatTopicRoot;
import com.tomo.tomolive.models.OriginalMessageRoot;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivityOriginal extends BaseActivity {
    ActivityChatListOriginalBinding binding;
    ChatAdapterOriginal chatAdapterOriginal = new ChatAdapterOriginal();
    SessionManager sessionManager;

    private String profileImage;
    private String secondUserId;
    private String userId;
    Emitter.Listener listener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("TAG", "call: listnerrrr" + args.length);
            Log.d("TAG", "call: listnerrrrmsg   " + args[0].toString());

            if (args[0] != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            JSONObject response = (JSONObject) args[0];
                            String user_id = response.get("user_id").toString();
                            String topic = response.get("topic").toString();
                            String message = response.get("message").toString();

                            OriginalMessageRoot.DataItem dataItem = new OriginalMessageRoot.DataItem();
                            if (user_id.equals(userId)) {
                                dataItem.setFlag(1);
                            } else {
                                dataItem.setFlag(0);
                            }
                            dataItem.setTopic(topic);
                            dataItem.setMessage(message);

                            chatAdapterOriginal.addSingleMessage(dataItem);
                            binding.rvchats.scrollToPosition(chatAdapterOriginal.getItemCount() - 1);
                        } catch (Exception o) {
                            Log.d("TAG", "run: eooros" + o.getMessage());
                        }

                    }
                });

            }


        }
    };
    private String chatRoomId;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_list_original);
        sessionManager = new SessionManager(this);
        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            userId = sessionManager.getUser().getId();
        }
        initView();
        Intent intent = getIntent();
        MainActivity.IS_HOST_LIVE = false;

        secondUserId = intent.getStringExtra("hostid");
        profileImage = intent.getStringExtra("image");
        Log.d("TAG", "onCreate: image" + profileImage);
        String name = intent.getStringExtra("name");
        if (name != null && !name.equals("")) {
            binding.tvName.setText(name);
        }


        Log.d("TAG", "onCreate: hostid " + secondUserId);
        if (secondUserId != null && !secondUserId.equals("")) {
            CreateTopic();
        }
        Glide.with(this).load(profileImage).circleCrop().addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.d("TAG", "onLoadFailed: " + e.getMessage());
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.d("TAG", "onLoadFailed: ");
                binding.imgProfile.setBackground(ContextCompat.getDrawable(ChatListActivityOriginal.this, R.drawable.bg_whitebtnround));
                binding.imgProfile.setImageDrawable(resource);
                return true;
            }
        }).into(binding.imgProfile);


    }

    private void CreateTopic() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender_id", userId);
        jsonObject.addProperty("receiver_id", secondUserId);
        Call<ChatTopicRoot> call = RetrofitBuilder.create().createChatTopic(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<ChatTopicRoot>() {
            @Override
            public void onResponse(Call<ChatTopicRoot> call, Response<ChatTopicRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && response.body().getData() != null) {
                        chatRoomId = response.body().getData().getId();
                        getOldMessages();
                        initSoketIO();
                        initListnear();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatTopicRoot> call, Throwable t) {

            }
        });
    }

    private void initSoketIO() {
        IO.Options options = IO.Options.builder()
                // IO factory options
                .setForceNew(false)
                .setMultiplex(true)

                // low-level engine options
                .setTransports(new String[]{Polling.NAME, WebSocket.NAME})
                .setUpgrade(true)
                .setRememberUpgrade(false)
                .setPath("/socket.io/")
                .setQuery("chatroom=" + chatRoomId + "")
                .setExtraHeaders(null)

                // Manager options
                .setReconnection(true)
                .setReconnectionAttempts(Integer.MAX_VALUE)
                .setReconnectionDelay(1_000)
                .setReconnectionDelayMax(5_000)
                .setRandomizationFactor(0.5)
                .setTimeout(20_000)

                // Socket options
                .setAuth(null)
                .build();

        URI uri = URI.create(BuildConfig.BASE_URL);
        socket = IO.socket(uri, options);
        Log.d("TAG", "onCreate: " + socket.id());
        socket.connect();
        socket.on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("TAG", "call: ");
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("TAG", "call: connect" + args.length);
                //   JsonObject jsonObject = new JsonObject();
                //  jsonObject.addProperty("name", sessionManager.getUser().getFname());
                //  jsonObject.addProperty("message", "joined");
                //   socket.emit("msg", jsonObject);
                socket.on("chat", listener);


            }
        });
    }

    private void initListnear() {
        binding.btnsend.setOnClickListener(v -> {
            if (!binding.etChat.getText().toString().equals("")) {


                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sender_id", userId);
                jsonObject.addProperty("receiver_id", secondUserId);
                jsonObject.addProperty("topic", chatRoomId);
                jsonObject.addProperty("message", binding.etChat.getText().toString().trim());
                Call<ChatSendRoot> call = RetrofitBuilder.create().sendMessageToBackend(Const.DEVKEY, jsonObject);
                call.enqueue(new Callback<ChatSendRoot>() {
                    @Override
                    public void onResponse(Call<ChatSendRoot> call, Response<ChatSendRoot> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() && response.body().getData() != null) {
                                Log.d("TAG", "onResponse: sended msg success to backend");

                                JSONObject object = new JSONObject();
                                try {
                                    //  object.put("name", sessionManager.getUser().getFname());

                                    object.put("user_id", userId);
                                    object.put("topic", chatRoomId);
                                    object.put("message", binding.etChat.getText().toString().trim());
                                    socket.emit("chat", object);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                binding.etChat.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatSendRoot> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());

                    }
                });


            }

        });
    }

    private void getOldMessages() {
        binding.tvloading.setVisibility(View.VISIBLE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("topic", chatRoomId);
        jsonObject.addProperty("user_id", userId);
        Call<OriginalMessageRoot> call = RetrofitBuilder.create().getOldMessage(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<OriginalMessageRoot>() {
            @Override
            public void onResponse(Call<OriginalMessageRoot> call, Response<OriginalMessageRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && !response.body().getData().isEmpty()) {
                        chatAdapterOriginal.addData(response.body().getData());
                        binding.rvchats.scrollToPosition(chatAdapterOriginal.getItemCount() - 1);

                    }
                }
                binding.tvloading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<OriginalMessageRoot> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initView() {

        binding.rvchats.setAdapter(chatAdapterOriginal);
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickCamara(View view) {

    }
}