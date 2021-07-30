package com.tomo.tomolive;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.models.Thumb;
import com.tomo.tomolive.retrofit.Const;
import com.tomo.tomolive.retrofit.RetrofitBuilder;
import com.tomo.tomolive.token.RtcTokenBuilderSample;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LivexUtils {
    private static final String TAG = "livexutils";

    public static void setCustomToast(Context context, String s) {
        Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
    }

    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("Nayan");
        names.add("Babu");
        names.add("Ramesh");
        names.add("Nayan");
        names.add("Prem");
        names.add("Raja");
        names.add("Vikrant");

        return names;
    }

    public static List<String> getComments() {
        List<String> names = new ArrayList<>();
        names.add("Hello ji");
        names.add("Heyy!!");
        names.add("I love you");
        names.add("you are so cute");
        names.add("7899044356 ye mera number he");
        names.add("Aap kaha se ho?");
        names.add("hello ji ");

        return names;
    }

    public static List<Thumb> setFakeData() {
        List<Thumb> thumbsFake = new ArrayList<>();
        Thumb thumb1 = new Thumb(true, "INDIA", "https://charming.classicube.in/storage/16120040676101606025796224download2.jpg", "sushmita", "Sushmita kanke", "https://charming.classicube.in/storage/16122657444141606983513265789431967.mp4", 105, "fake", "", 1250, "", "");
        Thumb thumb2 = new Thumb(true, "INDIA", "https://charming.classicube.in/storage/16120059632121606561336655images.jpg", "rajvati", "rajvati sona", "https://humile.classicube.in/storage/1610563128465UN1.mp4", 57, "fake", "", 556, "", "");
        Thumb thumb3 = new Thumb(true, "INDIA", "https://charming.classicube.in/storage/16120059632121606561336655images.jpg", "malika", "malika roni", "https://humile.classicube.in/storage/1610560308785RUSSIAN%2013.mp4", 11, "fake", "", 785, "", "");
        Thumb thumb4 = new Thumb(true, "INDIA", "https://charming.classicube.in/storage/16120040675671606025752652DOWNLOAD3.jpg", "miya", "miya udari", "https://humile.classicube.in/storage/1610557421443PAKISTAN%202.mp4\n", 97, "fake", "", 52, "", "");

        thumbsFake.add(thumb1);
        thumbsFake.add(thumb2);
        thumbsFake.add(thumb3);
        thumbsFake.add(thumb4);

        return thumbsFake;
    }


    public static void destoryHost(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String chenal = sessionManager.getUser().getId();
        try {
            String tkn = RtcTokenBuilderSample.main(chenal);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", chenal); //chanel is user id
            Call<RestResponse> call = RetrofitBuilder.create().destoryHost(Const.DEVKEY, jsonObject);
            call.enqueue(new Callback<RestResponse>() {
                @Override
                public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                    Log.d(TAG, "onResponse: host destoried");
                }

                @Override
                public void onFailure(Call<RestResponse> call, Throwable t) {

                }
            });

            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("token", tkn); //chanel is user id
            Call<RestResponse> call2 = RetrofitBuilder.create().destorytoken(Const.DEVKEY, jsonObject2);
            call2.enqueue(new Callback<RestResponse>() {
                @Override
                public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                    Log.d(TAG, "onResponse: token destoried");
                }

                @Override
                public void onFailure(Call<RestResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void informBackendHostIsLive(String uid) {
        Log.d(TAG, "informBackendHostIsLive: informing");
        Call<RestResponse> call = RetrofitBuilder.create().iAmAlive(Const.DEVKEY, uid);
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200 && response.body().isStatus()) {
                    Log.d(TAG, "onResponse: informed true statue 200");
                } else {
                    Log.d(TAG, "onResponse: inforemed status 402");
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: lochoooo " + t.getMessage());
            }
        });
    }
}
