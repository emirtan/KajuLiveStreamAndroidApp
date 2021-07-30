package com.tomo.tomolive.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tomo.tomolive.Adaptor.BottomViewPagerAdapter;
import com.tomo.tomolive.Adaptor.CommentAdapterOriginal;
import com.tomo.tomolive.Adaptor.EmojiAdapter;
import com.tomo.tomolive.BuildConfig;
import com.tomo.tomolive.LivexUtils;
import com.tomo.tomolive.R;
import com.tomo.tomolive.SessionManager;
import com.tomo.tomolive.databinding.ActivityFakeBinding;
import com.tomo.tomolive.models.EmojiIconRoot;
import com.tomo.tomolive.models.EmojicategoryRoot;
import com.tomo.tomolive.models.Thumb;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;

public class FakeActivity extends AppCompatActivity {
    private static final String TAG = "fakeact";
    private static final int SHEET_OPEN = 1;
    private static final int SHEET_CLOSE = 2;
    ActivityFakeBinding binding;
    Handler commentHandler = new Handler();
    Handler viewHandler = new Handler();
    private SimpleExoPlayer player;
    private SessionManager sessionManager;
    private String videoURL = "";
    private CommentAdapterOriginal commentAdapter = new CommentAdapterOriginal();
    Runnable commentRunnable = new Runnable() {
        @Override
        public void run() {

            addSingleComment();
            commentHandler.postDelayed(commentRunnable, 3000);
        }
    };
    private List<EmojicategoryRoot.Datum> finelCategories = new ArrayList<>();
    private Thumb datum;
    private int view = 0;
    Runnable viewRunnable = new Runnable() {
        @Override
        public void run() {
            view = view + new Random().nextInt(50) + 50;
            binding.tvviews.setText(String.valueOf(view));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fake);
        sessionManager = new SessionManager(this);

        getGiftsCategories();

        MainActivity.IS_HOST_LIVE = false;
        getIntentData();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String objstr = intent.getStringExtra("model");
            if (objstr != null && !objstr.equals("")) {
                Log.d(TAG, "onCreate: intent objstr " + objstr);
                datum = new Gson().fromJson(objstr, Thumb.class);
                Log.d(TAG, "onCreate: data " + datum.getHostId());


                String girlname = datum.getName();
                int girlCoin = datum.getCoin();

                binding.tvName.setText(girlname);
                binding.tvCoin.setText(String.valueOf(girlCoin));

                Glide.with(getApplicationContext())
                        .load(datum.getImage())
                        .circleCrop()
                        .into(binding.imgprofile);

                view = datum.getView();
                binding.tvviews.setText(String.valueOf(view));
                videoURL = datum.getChannel();
                Log.d(TAG, "getIntentData: " + datum.getChannel());
                initView();
                initUiButtonListnar();

            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentHandler.removeCallbacks(commentRunnable);
        viewHandler.removeCallbacks(viewRunnable);
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setVolume(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.setVolume(1);
        }
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
                    finelCategories.addAll(categories);
                    try {
                        setGiftList(finelCategories);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                        updetUI(SHEET_CLOSE);
                    });
                }
            }

            private void setGiftList(List<EmojicategoryRoot.Datum> finelCategories) {
                Call<EmojiIconRoot> call1 = RetrofitBuilder.create().getEmojiByCategory(Const.DEVKEY, finelCategories.get(1).get_id());
                call1.enqueue(new Callback<EmojiIconRoot>() {
                    private void onEmojiClick(Bitmap bitmap, Long coin, EmojiIconRoot.Datum emoji) {
                        // sendGift(bitmap, coin, emoji);
                        updetUI(SHEET_CLOSE);
                    }

                    @Override
                    public void onResponse(Call<EmojiIconRoot> call, Response<EmojiIconRoot> response) {
                        Log.d(TAG, "onResponse: emoji yes" + response.code());
                        if (response.code() == 200 && response.body().getStatus() && !response.body().getData().isEmpty()) {

                            EmojiAdapter emojiAdapter = new EmojiAdapter(response.body().getData());
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
                View v = LayoutInflater.from(FakeActivity.this).inflate(R.layout.custom_tabgift, null);
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

    private void initUiButtonListnar() {
        binding.btnsend.setOnClickListener(v -> {
            JSONObject object = new JSONObject();
            try {
                object.put("name", sessionManager.getUser().getName());
                object.put("comment", binding.etComment.getText().toString());
                commentAdapter.addComment(object);
                binding.etComment.setText("");
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
        binding.bottomPage.btnclose.setOnClickListener(v -> updetUI(SHEET_CLOSE));

        binding.lytShare.setOnClickListener(v -> startActivity(new Intent(this, ChatListActivityOriginal.class).putExtra("hostid", "6038efc14e6b4609c2d7e3d2").putExtra("image", datum.getImage()).putExtra("name", datum.getName())));
    }

    private void initView() {
        binding.rvComments.setAdapter(commentAdapter);
        setComment();
        setPlayer();
    }

    private void addSingleComment() {


        List<String> names = LivexUtils.getNames();
        List<String> comments = LivexUtils.getComments();

        if (!names.isEmpty()) {
            Collections.shuffle(names);
            if (!comments.isEmpty()) {
                Collections.shuffle(comments);
                JSONObject object = new JSONObject();
                try {
                    object.put("name", names.get(0));
                    object.put("comment", comments.get(0));
                    commentAdapter.addComment(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }


    }

    private void setComment() {
        commentHandler.postDelayed(commentRunnable, 3000);
        viewHandler.postDelayed(viewRunnable, 3000);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void setPlayer() {


        player = new SimpleExoPlayer.Builder(this).build();
        binding.playerview.setPlayer(player);
        binding.playerview.setShowBuffering(true);
        Log.d(TAG, "setvideoURL: " + videoURL);
        Uri uri = Uri.parse(videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        Log.d(TAG, "initializePlayer: " + uri);
        player.setPlayWhenReady(true);
        player.seekTo(0, 0);
        player.prepare(mediaSource, false, false);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case STATE_BUFFERING:
                        Log.d(TAG, "buffer: " + uri);
                        break;
                    case STATE_ENDED:
                        Toast.makeText(FakeActivity.this, "Live Ended!", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                        Log.d(TAG, "end: " + uri);
                        break;
                    case STATE_IDLE:
                        Log.d(TAG, "idle: " + uri);
//                        if (player != null) {
//                            player.release();
//                        }
                        //  finish();
                        // getVideo(countryid);
                        break;

                    case STATE_READY:

                        Log.d(TAG, "ready: " + uri);

                        break;
                    default:
                        break;
                }
            }
        });

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
            binding.lytusercoin.setVisibility(View.GONE);
        }
    }

    public void onclickGiftIcon(View view) {
        updetUI(SHEET_OPEN);
    }

    public void onClickClose(View view) {
        onBackPressed();
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