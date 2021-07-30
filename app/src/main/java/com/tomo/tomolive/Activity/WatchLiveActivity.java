package com.tomo.tomolive.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tomo.tomolive.Adaptor.BottomViewPagerAdapter;
import com.tomo.tomolive.Adaptor.CommentAdapterOriginal;
import com.tomo.tomolive.Adaptor.EmojiAdapter;
import com.tomo.tomolive.BuildConfig;
import com.tomo.tomolive.LivexUtils;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityWatchBinding;
import com.tomo.tomolive.databinding.BottomSheetChatBinding;
import com.tomo.tomolive.models.ChatSendRoot;
import com.tomo.tomolive.models.CommentRootOriginal;
import com.tomo.tomolive.models.EmojiIconRoot;
import com.tomo.tomolive.models.EmojicategoryRoot;
import com.tomo.tomolive.models.HostEmojiRoot;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.models.StikerRoot;
import com.tomo.tomolive.models.Thumb;
import com.tomo.tomolive.models.User;
import com.tomo.tomolive.oflineModels.Filters.FilterRoot;
import com.tomo.tomolive.oflineModels.gif.GifRoot;
import com.tomo.tomolive.oflineModels.gift.GiftRoot;
import com.tomo.tomolive.retrofit.ApiCalling;
import com.tomo.tomolive.retrofit.CoinWork;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchLiveActivity extends AppCompatActivity {
    private static final String TAG = "watchact";
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int SHEET_OPEN = 1;
    private static final int SHEET_CLOSE = 2;
    ActivityWatchBinding binding;
    Handler timerHandler = new Handler();
    Emitter.Listener filterListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: filterlistnear" + args.length);
            Log.d(TAG, "call: filterlistnear   " + args[0].toString());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {


                    if (args[0] != null) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {


                                String filtertype = null;

                                filtertype = args[0].toString();
                                FilterRoot filterRoot = new Gson().fromJson(filtertype, FilterRoot.class);
                                if (filterRoot != null) {
                                    if (filterRoot.getFilter() == 0) {
                                        Log.d(TAG, "run: filter");
                                        binding.imgFilter.setImageDrawable(null);
                                    } else {
                                        Log.d(TAG, "run: filteryes" + filterRoot.getFilter());
                                        //  Glide.with(WatchLiveActivity.this).load(filterRoot.getFilter()).centerCrop().into(binding.imgFilter);
                                        binding.imgFilter.setImageDrawable(ContextCompat.getDrawable(WatchLiveActivity.this, filterRoot.getFilter()));
                                    }
                                }

                            }
                        });

                    }

                }
            });


        }
    };
    Emitter.Listener gifListnear = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: gifListnear" + args.length);
            Log.d(TAG, "call: gifListnear   " + args[0].toString());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {



                    if (args[0] != null) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                String filtertype = null;

                                filtertype = args[0].toString();
                                GifRoot filterRoot = new Gson().fromJson(filtertype, GifRoot.class);
                                if (filterRoot != null) {
                                    if (filterRoot.getFilter() == 0) {
                                        binding.imgFilter2.setImageDrawable(null);
                                    } else {
                                        Glide.with(WatchLiveActivity.this).asGif().load(filterRoot.getFilter()).centerCrop().into(binding.imgFilter2);
                                    }
                                }

                            }
                        });

                    }

                }
            });


        }
    };
    Emitter.Listener emojiListnear = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: emiji" + args.length);
            Log.d(TAG, "call: emijoi   " + args[0].toString());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //  Toast.makeText(HostActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();


                    if (args[0] != null) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                String filtertype = null;

                                filtertype = args[0].toString();
                                HostEmojiRoot.DataItem emoji = new Gson().fromJson(filtertype, HostEmojiRoot.DataItem.class);
                                if (emoji != null) {
                                    Glide.with(WatchLiveActivity.this).load(BuildConfig.BASE_URL + emoji.getEmoji()).centerCrop().into(binding.imgEmoji);

                                    new Handler().postDelayed(() -> {
                                        binding.imgEmoji.setImageDrawable(null);
                                    }, 1500);

                                    // binding.imgFilter2.setImageDrawable(ContextCompat.getDrawable(HostActivity.this,filterRoot.getFilter()));

                                }

                            }
                        });

                    }

                }
            });


        }
    };
    Emitter.Listener stickerListnear = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: gifListnear" + args.length);
            Log.d(TAG, "call: gifListnear   " + args[0].toString());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //  Toast.makeText(WatchLiveActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();


                    if (args[0] != null) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                String filtertype = null;

                                filtertype = args[0].toString();
                                StikerRoot.DataItem stiker = new Gson().fromJson(filtertype, StikerRoot.DataItem.class);
                                if (stiker != null) {

                                    Glide.with(WatchLiveActivity.this).asGif().load(BuildConfig.BASE_URL + stiker.getSticker()).centerCrop().into(binding.imgstiker);
                                    // binding.imgFilter2.setImageDrawable(ContextCompat.getDrawable(HostActivity.this,filterRoot.getFilter()));
                                    new Handler().postDelayed(() -> {
                                        binding.imgstiker.setImageDrawable(null);
                                    }, 1500);
                                }

                            }
                        });

                    }

                }
            });


        }
    };
    Emitter.Listener giftReciveListnear = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: gifListnear" + args.length);
            Log.d(TAG, "call: gifListnear   " + args[0].toString());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //


                    if (args[0] != null) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                String filtertype = null;

                                filtertype = args[0].toString();
                                GiftRoot gift = new Gson().fromJson(filtertype, GiftRoot.class);
                                if (gift != null) {
                                    Glide.with(WatchLiveActivity.this).load(BuildConfig.BASE_URL + gift.getImage()).centerCrop().into(binding.imgEmoji);
                                    new Handler().postDelayed(() -> {
                                        binding.imgEmoji.setImageDrawable(null);
                                    }, 1500);


                                }

                            }
                        });

                    }

                }
            });


        }
    };
    Emitter.Listener viewListnear = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: listnerrrr" + args.length);
            Log.d(TAG, "call: listnerrrrmsg   " + args[0].toString());

            if (args[0] != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            binding.tvviews.setText(String.valueOf(args[0].toString()));
                            Log.d(TAG, "run: viewssss " + args[0].toString());
                        } catch (Exception o) {
                            Log.d(TAG, "run: eooros" + o.getMessage());
                        }

                    }
                });

            }


        }
    };
    private int uidAgora = -1;
    private SessionManager sessionManager;
    private String userId;
    private Thumb datum;
    private String tkn;
    private String hostId;
    private String chennal;
    private RelativeLayout mRemoteContainer;
    private RtcEngine mRtcEngine;
    private int totalCoins;
    private List<EmojicategoryRoot.Datum> categories = new ArrayList<>();
    private CommentAdapterOriginal commentAdapter;
    Emitter.Listener listener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.d(TAG, "call: listnerrrrmsg   " + args[0].toString());

            if (args[0] != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JSONObject response = (JSONObject) args[0];

                            commentAdapter.addComment(response);
                        } catch (Exception o) {
                            Log.d(TAG, "run: eooros" + o.getMessage());
                        }

                    }
                });

            }


        }
    };
    private EmojicategoryRoot.Datum tempobj;
    private EmojiAdapter emojiAdapter;
    private Socket socket;
    private VideoCanvas mRemoteVideo;
    Emitter.Listener vidEndedListnear = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: listnerrrrVideoEnd" + args.length);
            Log.d(TAG, "call: listnerrrrVidEnd   " + args[0].toString());

            if (args[0] != null) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        endActivityAndCall();
                       /* try {

                            binding.tvviews.setText(String.valueOf(args[0].toString()));
                            Log.d(TAG, "run: viewssss " + args[0].toString());
                        } catch (Exception o) {
                            Log.d(TAG, "run: eooros" + o.getMessage());
                        }
*/
                    }
                });

            }


        }
    };
    private boolean mCallEnd;
    private int rate = 0;
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: onr min finishedc");
            CoinWork coinWork = new CoinWork();
            coinWork.transferCoin(userId, hostId, String.valueOf(rate));
            coinWork.setOnCoinWorkLIstner(new CoinWork.OnCoinWorkLIstner() {
                @Override
                public void onSuccess(User user) {
                    binding.bottomPage.tvUsereCoin.setText(String.valueOf(user.getCoin()));

                    Log.d(TAG, "onResponse: success coin minused");
                    sessionManager.saveUser(user);

                    //todo gost and user profile get show baki
                    setUpHostData();
                    setupMyData();
                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onInsufficientBalance() {
                    Toast.makeText(WatchLiveActivity.this, "Not Enough Balance", Toast.LENGTH_SHORT).show();
                    endActivityAndCall();
                    openWalletActivity();
                }
            });

            timerHandler.postDelayed(this, 10000);
        }
    };
    private boolean isVideoDecoded = false;
    private ArrayList<EmojicategoryRoot.Datum> finelCategories;
    private BottomSheetDialog bottomSheetDialog;
    private boolean isBottomSheetChatOpen = false;
    private String chatRoomId;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            uidAgora = uid;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "sssss=- run: joined chenal");
                    getoldComments();
                    initTimer();
                    showFollowPopup();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isVideoDecoded) {
                                Log.d(TAG, "sssss=- run: yreeeeeeehhhhh  video decoded");
                            } else {
                                deleteHostInform();
                            }
                        }

                        private void deleteHostInform() {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("user_id", datum.getChannel()); //chanel is user id
                            Call<RestResponse> call = RetrofitBuilder.create().destoryHost(Const.DEVKEY, jsonObject);
                            call.enqueue(new Callback<RestResponse>() {
                                @Override
                                public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                                    Log.d(TAG, "sssss=- onResponse: host destoried");
                                    mCallEnd = true;

                                    endCall();
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<RestResponse> call, Throwable t) {

                                }
                            });

                        }
                    }, 5000);
                    //  newUserJ.oinedIngformToBackend();

                }
            });
        }

        private void showFollowPopup() {
            Log.d(TAG, "showFollowPopup: ");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("host_id", hostId);
            jsonObject.addProperty("guest_id", userId);
            Call<RestResponse> call = RetrofitBuilder.create().checkFollow(Const.DEVKEY, jsonObject);
            call.enqueue(new Callback<RestResponse>() {
                @Override
                public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                    if (response.code() == 200) {
                        if (!response.body().isStatus()) {
                            Log.d(TAG, "onResponse: is following false ");
                            ApiCalling apiCalling = new ApiCalling(WatchLiveActivity.this);
                            apiCalling.getHostProfile(hostId, new ApiCalling.OnHostProfileGetListnear() {
                                @Override
                                public void onHostGet(User user) {
                                    Log.d(TAG, "onHostGet: host fetched");

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "run: 5 sec delayed");
                                            if (isFinishing()) {
                                                Log.d(TAG, "run: is finishing when popup");
                                                return;
                                            }
                                            Animation pop = AnimationUtils.loadAnimation(WatchLiveActivity.this, R.anim.zoom);
                                            binding.btnfollow.startAnimation(pop);
                                            binding.btnfollow.setVisibility(View.VISIBLE);
                                            binding.btnfollow.setOnClickListener(v -> {
                                                apiCalling.followUser(WatchLiveActivity.this, userId, hostId);
                                                apiCalling.setResponseListnear(new ApiCalling.ResponseListnear() {
                                                    @Override
                                                    public void responseSuccess() {
                                                        Log.d(TAG, "responseSuccess: followed");

                                                        binding.btnfollow.setVisibility(View.GONE);
                                                    }

                                                    @Override
                                                    public void responseFail() {
                                                        binding.btnfollow.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            });


                                        }
                                    }, 1000);

                                }

                                @Override
                                public void onFail() {

                                }
                            });

                        } else {
                        }
                    }
                }

                @Override
                public void onFailure(Call<RestResponse> call, Throwable t) {
                    Log.d("TAG", "onFailure: followroort" + t.getMessage());
                }
            });

        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isVideoDecoded = true;
                    Log.d(TAG, "sssss=- run: vide decode");
                    setupRemoteVideo(uid);
                    addViewCount();
                }
            });
        }


        @Override
        public void onUserOffline(final int uid, int reason) {
            Log.d(TAG, "sssss=- onUserOffline: watch");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endActivityAndCall();


                }
            });
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.d(TAG, "sssss=- onError: " + err);
        }

        @Override
        public void onFirstLocalAudioFrame(int elapsed) {
            super.onFirstLocalAudioFrame(elapsed);
            Log.d(TAG, "sssss=- onFirstLocalAudioFrame: ");
        }

        @Override
        public void onMediaEngineLoadSuccess() {
            super.onMediaEngineLoadSuccess();
            Log.d(TAG, "sssss=- onMediaEngineLoadSuccess: ");
        }

        @Override
        public void onMediaEngineStartCallSuccess() {
            super.onMediaEngineStartCallSuccess();
            Log.d(TAG, "sssss=- onMediaEngineStartCallSuccess: ");
        }
    };

    private void setUpHostData() {
        ApiCalling apiCalling = new ApiCalling(WatchLiveActivity.this);
        apiCalling.getHostProfile(hostId, new ApiCalling.OnHostProfileGetListnear() {
            @Override
            public void onHostGet(User user) {
                binding.tvCoin.setText(String.valueOf(user.getCoin()));
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void openWalletActivity() {
        Toast.makeText(this, "Reacharge your Wallet First", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MyWalletActivity.class));
    }

    private void initTimer() {
        timerHandler.postDelayed(timerRunnable, 10000);
        //todo baki     user coin show
    }

    private void endActivityAndCall() {
        LivexUtils.setCustomToast(WatchLiveActivity.this, "Video Ended!!");
        timerHandler.removeCallbacks(timerRunnable);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: user left");
                //  userLeftIngformToBackend();
                if (uidAgora != -1) {

                    onRemoteUserLeft(uidAgora);
                }
                endCall();
                finish();
            }
        }, 2000);
    }

    private void lessViewCount() {
        Log.d(TAG, "lessViewCount: ");
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", sessionManager.getUser().getId());
            object.put("name", sessionManager.getUser().getName());
            object.put("token", tkn);
            object.put("image", sessionManager.getUser().getImage());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("viewless", object);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watch);
        MainActivity.IS_HOST_LIVE = false;
        sessionManager = new SessionManager(this);
        if (sessionManager.getBooleanValue(Const.ISLOGIN)) {
            userId = sessionManager.getUser().getId();
        }
        getGiftsCategories();
        Intent intent = getIntent();
        if (intent != null) {
            String objstr = intent.getStringExtra("model");
            if (objstr != null && !objstr.equals("")) {
                Log.d(TAG, "onCreate: intent objstr " + objstr);
                datum = new Gson().fromJson(objstr, Thumb.class);
                Log.d(TAG, "onCreate: data " + datum.getHostId());
                tkn = datum.getToken();
                hostId = datum.getHostId();
                chennal = datum.getChannel();
                rate = datum.getRate();
                Log.d(TAG, "onCreate: tkn " + tkn);
                Log.d(TAG, "onCreate:hostid " + hostId);
                Log.d(TAG, "onCreate: chanel " + chennal);
                initUI();
                createChatRoom();
                checkHostIsValidOrNot();
                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                    initEngineAndJoinChannel();
                    Log.d(TAG, "onCreate: permisson");
                } else {
                    Log.d(TAG, "onCreate: permisson denied");
                }
            }
        }


    }

    private void createChatRoom() {
        ApiCalling apiCalling = new ApiCalling(WatchLiveActivity.this);
        apiCalling.createChatRoom(userId, hostId, new ApiCalling.OnRoomGenereteListnear() {
            @Override
            public void onRoomGenereted(String roomId) {
                chatRoomId = roomId;
            }

            @Override
            public void onFail() {
                Log.d(TAG, "onFail: chatroom crete faillll");
            }
        });
    }

    private void checkHostIsValidOrNot() {
        Log.d(TAG, "checkHostIsValidOrNot: histid " + hostId);
        Call<RestResponse> call = RetrofitBuilder.create().checkHostIsValid(Const.DEVKEY, hostId);
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {

                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: hostidvalid or not " + response.body().isStatus());
                    if (!response.body().isStatus()) {
                        Toast.makeText(WatchLiveActivity.this, "Live Ended!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: errr hoisvalid or not " + t.getMessage());
            }
        });
    }


    private void getoldComments() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", tkn);
        Call<CommentRootOriginal> call = RetrofitBuilder.create().getOldComments(Const.DEVKEY, tkn);
        call.enqueue(new Callback<CommentRootOriginal>() {
            @Override
            public void onResponse(Call<CommentRootOriginal> call, Response<CommentRootOriginal> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && !response.body().getData().isEmpty()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("name", response.body().getData().get(i).getName());
                                jsonObject.put("comment", response.body().getData().get(i).getComment());
                                commentAdapter.addComment(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentRootOriginal> call, Throwable t) {
                Log.d(TAG, "onFailure: getold cmt errr " + t.getMessage());
            }
        });
    }

    private void setupRemoteVideo(int uid) {
        Log.d(TAG, "setupRemoteVideo: ");
        ViewGroup parent = mRemoteContainer;

        if (mRemoteVideo != null) {
            Log.d(TAG, "setupRemoteVideo: mremote is null");
            return;
        }
        Log.d(TAG, "setupRemoteVideo: hash");
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());

        //  view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_ADAPTIVE, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
        Log.d(TAG, "setupRemoteVideo: added video");
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            // Destroys remote view
            mRemoteVideo = null;
        }
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        try {
            mRtcEngine = RtcEngine.create(this, getString(R.string.agora_app_id), mRtcEventHandler);
            mRtcEngine.setClientRole(IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_AUDIENCE);

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));

        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        /**In the demo, the default is to enter as the anchor.*/
        mRtcEngine.setClientRole(IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_AUDIENCE);

        String token = tkn;
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }
        mRtcEngine.joinChannel(token, chennal, "Extra Optional Data", 0);
    }

    private void initUI() {
        //   mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        commentAdapter = new CommentAdapterOriginal();
        binding.rvComments.setAdapter(commentAdapter);


        Glide.with(getApplicationContext())
                .load(datum.getImage())
                .circleCrop()
                .into(binding.imgprofile);
        String s = String.valueOf(datum.getName().charAt(0)).toUpperCase();
        binding.tvName.setText(s.concat(datum.getName().substring(1)));


        totalCoins = sessionManager.getUser().getCoin();
        binding.tvusercoins.setText(String.valueOf(totalCoins));
        binding.tvCoin.setText(String.valueOf(totalCoins));
        // Sample logs are optional.


        initSoketIO();
        initListear();

    }

    private void initListear() {
        binding.btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.etComment.getText().toString().equals("")) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("name", sessionManager.getUser().getName());
                        object.put("comment", binding.etComment.getText().toString());
                        object.put("token", tkn);

                        socket.emit("msg", object);
                    } catch (JSONException e) {
                        Log.d(TAG, "onClick: btnsend er  " + e.getMessage());
                        e.printStackTrace();
                    }

                    sendCommentToBackend();
                    binding.etComment.setText("");
                }
            }

            private void sendCommentToBackend() {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", sessionManager.getUser().getName());
                jsonObject.addProperty("comment", binding.etComment.getText().toString());
                jsonObject.addProperty("token", tkn);

                Call<RestResponse> call = RetrofitBuilder.create().sendCommentToServer(Const.DEVKEY, jsonObject);
                call.enqueue(new Callback<RestResponse>() {

                    @Override
                    public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                        if (response.code() == 200) {
                            if (response.body().isStatus()) {
                                Log.d(TAG, "onResponse: comment sended");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RestResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: rest send cmtt " + t.getMessage());
                    }
                });
            }
        });

        //   binding.lytShare.setOnClickListener(v -> startActivity(new Intent(this, ChatListActivityOriginal.class).putExtra("hostid", hostId)));
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
                .setQuery("room=" + tkn + "")
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
        Log.d(TAG, "onCreate: siketid " + socket.id());
        socket.connect();
        socket.on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "call: connection ");
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Log.d(TAG, "call: connect" + args.length);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", sessionManager.getUser().getName());
                jsonObject.addProperty("comment", "joined");
                socket.emit("msg", jsonObject);
                socket.on("msg", listener);
                socket.on("filter", filterListener);
                socket.on("gif", gifListnear);
                socket.on("sticker", stickerListnear);
                socket.on("emoji", emojiListnear);
                socket.on("gift", giftReciveListnear);
                socket.on("view", viewListnear);
                socket.on("ended", vidEndedListnear);


            }
        });

    }

    private void addViewCount() {
        Log.d(TAG, "addViewCount: ");
        JSONObject object = new JSONObject();
        try {
            object.put("user_id", sessionManager.getUser().getId());
            object.put("name", sessionManager.getUser().getName());
            object.put("token", tkn);
            object.put("image", sessionManager.getUser().getImage());
            socket.emit("viewadd", object);
        } catch (JSONException e) {
            Log.d(TAG, "addViewCount: err " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lessViewCount();
    }

    private void getGiftsCategories() {
        Call<EmojicategoryRoot> call = RetrofitBuilder.create().getCategories(Const.DEVKEY);
        call.enqueue(new Callback<EmojicategoryRoot>() {
            @Override
            public void onResponse(Call<EmojicategoryRoot> call, Response<EmojicategoryRoot> response) {
                if (response.code() == 200 && response.body().getStatus() && !response.body().getData().isEmpty()) {

                    List<EmojicategoryRoot.Datum> categories = response.body().getData();
                    Log.d(TAG, "onResponse: categorysixe " + categories.size());
                    finelCategories = new ArrayList<>();
                    for (int i = 0; i < categories.size(); i++) {
                        if (Boolean.TRUE.equals(categories.get(i).getIsTop())) {
                            tempobj = categories.get(i);

                        } else {
                            finelCategories.add(categories.get(i));
                        }
                    }

                    if (tempobj != null) {
                        finelCategories.add(0, tempobj);
                    }

                    setGiftList(finelCategories);


                    BottomViewPagerAdapter bottomViewPagerAdapter = new BottomViewPagerAdapter(finelCategories);
                    binding.bottomPage.viewpager.setAdapter(bottomViewPagerAdapter);
                    settabLayout(finelCategories);
                    binding.bottomPage.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.bottomPage.tablayout));
                    binding.bottomPage.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            binding.bottomPage.viewpager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            //ll
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            //ll
                        }
                    });
                    bottomViewPagerAdapter.setEmojiListnerViewPager((bitmap, coin, emoji) -> {
                        sendGift(bitmap, coin, emoji);
                        updetUI(SHEET_CLOSE);
                    });
                }
            }

            private void setGiftList(List<EmojicategoryRoot.Datum> finelCategories) {
                Call<EmojiIconRoot> call1 = RetrofitBuilder.create().getEmojiByCategory(Const.DEVKEY, finelCategories.get(0).get_id());
                call1.enqueue(new Callback<EmojiIconRoot>() {
                    private void onEmojiClick(Bitmap bitmap, Long coin, EmojiIconRoot.Datum emoji) {
                        sendGift(bitmap, coin, emoji);
                    }

                    @Override
                    public void onResponse(Call<EmojiIconRoot> call, Response<EmojiIconRoot> response) {
                        Log.d(TAG, "onResponse: emoji yes" + response.code());
                        if (response.code() == 200 && response.body().getStatus() && !response.body().getData().isEmpty()) {

                            emojiAdapter = new EmojiAdapter(response.body().getData());
                            binding.rvEmogi.setAdapter(emojiAdapter);
                            emojiAdapter.setOnEmojiClickListnear(this::onEmojiClick);


                        }
                    }

                    @Override
                    public void onFailure(Call<EmojiIconRoot> call, Throwable t) {
//ll
                    }
                });
            }


            private void settabLayout(List<EmojicategoryRoot.Datum> categories) {
                binding.bottomPage.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
                for (int i = 0; i < categories.size(); i++) {

                    binding.bottomPage.tablayout.addTab(binding.bottomPage.tablayout.newTab().setCustomView(createCustomView(categories.get(i))));

                }
            }

            private View createCustomView(EmojicategoryRoot.Datum datum) {
                Log.d(TAG, "settabLayout: " + datum.getName());
                Log.d(TAG, "settabLayout: " + datum.getIcon());
                View v = LayoutInflater.from(WatchLiveActivity.this).inflate(R.layout.custom_tabgift, null);
                TextView tv = (TextView) v.findViewById(R.id.tvTab);
                tv.setText(datum.getName());
                ImageView img = (ImageView) v.findViewById(R.id.imagetab);

                Glide.with(getApplicationContext())
                        .load(BuildConfig.BASE_URL + datum.getIcon())
                        .placeholder(R.drawable.ic_gift)
                        .into(img);
                return v;

            }


            @Override
            public void onFailure(Call<EmojicategoryRoot> call, Throwable t) {
//ll
            }
        });
    }

    private void sendGift(Bitmap bitmap, Long coin, EmojiIconRoot.Datum emoji) {
        CoinWork coinWork = new CoinWork();
        coinWork.transferCoin(userId, hostId, String.valueOf(coin));
        coinWork.setOnCoinWorkLIstner(new CoinWork.OnCoinWorkLIstner() {
            @Override
            public void onSuccess(User user) {
                binding.bottomPage.tvUsereCoin.setText(String.valueOf(user.getCoin()));

                Log.d(TAG, "onResponse: success coin minused");
                sessionManager.saveUser(user);

                GiftRoot giftRoot = new GiftRoot();
                giftRoot.setCoin(coin);
                giftRoot.setImage(emoji.getIcon());
                giftRoot.setUsername(sessionManager.getUser().getName());
                socket.emit("gift", new Gson().toJson(giftRoot));
                setupMyData();
                //todo gost and user profile get show baki
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onInsufficientBalance() {
                Toast.makeText(WatchLiveActivity.this, "Not Enough Balance", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setupMyData() {
        int myCoin = sessionManager.getUser().getCoin();
        //  binding.tvCoin.setText(String.valueOf(myCoin));
        binding.bottomPage.tvUsereCoin.setText(String.valueOf(myCoin));
    }

    private void updetUI(int state) {
        if (state == SHEET_OPEN) {
            binding.bottomPage.lyt2.setVisibility(View.VISIBLE);
            binding.rvComments.setVisibility(View.GONE);
            binding.rvEmogi.setVisibility(View.GONE);
            binding.lytbottom.setVisibility(View.GONE);
            binding.lytShare.setVisibility(View.GONE);
            binding.lytusercoin.setVisibility(View.GONE);
        } else {
            binding.bottomPage.lyt2.setVisibility(View.GONE);
            binding.rvComments.setVisibility(View.VISIBLE);
            binding.rvEmogi.setVisibility(View.VISIBLE);
            binding.lytbottom.setVisibility(View.VISIBLE);
            binding.lytShare.setVisibility(View.VISIBLE);
            binding.lytusercoin.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    public void onClickchat(View view) {
        //  startActivity(new Intent(this, ChatListActivityOriginal.class).putExtra("hostid", hostId));
        bottomSheetDialog = new BottomSheetDialog(this, R.style.customStyle);

        bottomSheetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        BottomSheetChatBinding bottomSheetChatBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottom_sheet_chat, null, false);
        bottomSheetDialog.setContentView(bottomSheetChatBinding.getRoot());

        Glide.with(this).load(datum.getImage()).circleCrop().into(bottomSheetChatBinding.imgprofile);
        String title = "Hello " + sessionManager.getUser().getName() + " ,Send your Message to " + datum.getName() + "";
        bottomSheetChatBinding.tvtitle.setText(title);
        bottomSheetChatBinding.btnsend.setOnClickListener(v -> {
            if (chatRoomId != null && !chatRoomId.equals("")) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sender_id", userId);
                jsonObject.addProperty("receiver_id", hostId);
                jsonObject.addProperty("topic", chatRoomId);
                jsonObject.addProperty("message", bottomSheetChatBinding.etChat.getText().toString().trim());
                Call<ChatSendRoot> call = RetrofitBuilder.create().sendMessageToBackend(Const.DEVKEY, jsonObject);
                call.enqueue(new Callback<ChatSendRoot>() {
                    @Override
                    public void onResponse(Call<ChatSendRoot> call, Response<ChatSendRoot> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() && response.body().getData() != null) {
                                Log.d("TAG", "onResponse: sended msg success to backend");
                                bottomSheetChatBinding.etChat.setText("");
                                Toast.makeText(WatchLiveActivity.this, "Message Send Successfully", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
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

/*
        bottomSheetChatBinding.btnclose.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            isBottomSheetChatOpen = false;
        });
*/


        bottomSheetDialog.show();
    }

    public void onclickGiftIcon(View view) {
        updetUI(SHEET_OPEN);
    }

    public void onClickClose(View view) {
        mCallEnd = true;
        endCall();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lessViewCount();
        mCallEnd = true;
        endCall();
        finish();
    }

    private void endCall() {
        timerHandler.removeCallbacks(timerRunnable);

        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        mRtcEngine.leaveChannel();
        RtcEngine.destroy();
    }

    public void onclickShare(View view) {
        String hostName = "";
        if (datum != null && datum.getUsername() != null) {
            hostName = datum.getUsername();
        }
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = "\nHello Dear, I am " + hostName + "\nLet me recommend you this application\n and watch my LiveVideo \n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
}