package com.tomo.tomolive.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tomo.tomolive.models.ChatTopicRoot;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.models.User;
import com.tomo.tomolive.models.UserRoot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalling {

    ResponseListnear responseListnear;
    private OnHostProfileGetListnear onHostProfileGetListnear;

    public ApiCalling(Context context) {

    }

    public void followUser(Context context, String myId, String guestId) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_user_id", myId);
        jsonObject.addProperty("to_user_id", guestId);
        Call<RestResponse> call = RetrofitBuilder.create().follow(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        Toast.makeText(context, "Follow Successfully", Toast.LENGTH_SHORT).show();
                        responseListnear.responseSuccess();
                    } else {
                        responseListnear.responseFail();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: followroort" + t.getMessage());
            }
        });
    }

    public ResponseListnear getResponseListnear() {
        return responseListnear;
    }

    public void setResponseListnear(ResponseListnear responseListnear) {
        this.responseListnear = responseListnear;
    }

    public void unfollowUser(Context context, String myId, String guestId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_user_id", myId);
        jsonObject.addProperty("to_user_id", guestId);
        Call<RestResponse> call = RetrofitBuilder.create().unfollow(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        Toast.makeText(context, "Unfollow Successfully", Toast.LENGTH_SHORT).show();
                        responseListnear.responseSuccess();
                    } else {
                        responseListnear.responseFail();
                    }
                }
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                Log.d("TAG", "onFailure: followroort" + t.getMessage());
            }
        });
    }

    public void getHostProfile(String hostId, OnHostProfileGetListnear onHostProfileGetListnear) {
        this.onHostProfileGetListnear = onHostProfileGetListnear;
        Call<UserRoot> call = RetrofitBuilder.create().getUserProfile(Const.DEVKEY, hostId);
        call.enqueue(new Callback<UserRoot>() {
            @Override
            public void onResponse(Call<UserRoot> call, Response<UserRoot> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus() && response.body().getUser() != null) {
                        onHostProfileGetListnear.onHostGet(response.body().getUser());
                    } else {
                        onHostProfileGetListnear.onFail();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRoot> call, Throwable t) {
                onHostProfileGetListnear.onFail();
            }
        });
    }

    OnRoomGenereteListnear onRoomGenereteListnear;

    public void createChatRoom(String userId, String hostId, OnRoomGenereteListnear onRoomGenereteListnear) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sender_id", userId);
        jsonObject.addProperty("receiver_id", hostId);
        Call<ChatTopicRoot> call = RetrofitBuilder.create().createChatTopic(Const.DEVKEY, jsonObject);
        call.enqueue(new Callback<ChatTopicRoot>() {
            @Override
            public void onResponse(Call<ChatTopicRoot> call, Response<ChatTopicRoot> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() && response.body().getData() != null) {

                        onRoomGenereteListnear.onRoomGenereted(response.body().getData().getId());
                    } else {
                        onRoomGenereteListnear.onFail();
                    }
                } else {
                    onRoomGenereteListnear.onFail();
                }
            }

            @Override
            public void onFailure(Call<ChatTopicRoot> call, Throwable t) {
                onRoomGenereteListnear.onFail();
            }
        });
    }


    public interface ResponseListnear {
        void responseSuccess();

        void responseFail();
    }

    public interface OnHostProfileGetListnear {
        void onHostGet(User user);

        void onFail();
    }

    public interface OnRoomGenereteListnear {
        void onRoomGenereted(String roomId);

        void onFail();
    }
}
