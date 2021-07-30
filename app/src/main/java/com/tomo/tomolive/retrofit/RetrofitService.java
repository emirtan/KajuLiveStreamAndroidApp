package com.tomo.tomolive.retrofit;

import com.google.gson.JsonObject;
import com.tomo.tomolive.models.ChatSendRoot;
import com.tomo.tomolive.models.ChatTopicRoot;
import com.tomo.tomolive.models.ChatUserListRoot;
import com.tomo.tomolive.models.CoinHistoryRoot;
import com.tomo.tomolive.models.CommentRootOriginal;
import com.tomo.tomolive.models.CountryRoot;
import com.tomo.tomolive.models.DailyTaskRoot;
import com.tomo.tomolive.models.EmojiIconRoot;
import com.tomo.tomolive.models.EmojicategoryRoot;
import com.tomo.tomolive.models.GirlThumbListRoot;
import com.tomo.tomolive.models.GuestUserRoot;
import com.tomo.tomolive.models.HostEmojiRoot;
import com.tomo.tomolive.models.NotificationRoot;
import com.tomo.tomolive.models.OriginalMessageRoot;
import com.tomo.tomolive.models.PaperRoot;
import com.tomo.tomolive.models.PlanRoot;
import com.tomo.tomolive.models.ProductKRoot;
import com.tomo.tomolive.models.RechargeHistoryRoot;
import com.tomo.tomolive.models.RestResponse;
import com.tomo.tomolive.models.StikerRoot;
import com.tomo.tomolive.models.UserRoot;
import com.tomo.tomolive.models.ViewUserRoot;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {

    @POST("/user/signup")
    Call<UserRoot> signUpUser(@Header("key") String devkey, @Body JsonObject object);

    @Multipart
    @POST("user/edit_profile")
    Call<UserRoot> updateUser(@Header("key") String token,
                              @PartMap Map<String, RequestBody> partMap,
                              @Part MultipartBody.Part requestBody);


    @Multipart
    @POST("user/edit_profile")
    Call<UserRoot> updateUser(@Header("key") String token,
                              @PartMap Map<String, RequestBody> partMap);


    @POST("user/check_username")
    Call<RestResponse> checkUserName(@Header("key") String devkey, @Body JsonObject object);

    @GET("/user/profile")
    Call<UserRoot> getProfile(@Header("key") String devkey, @Query("user_id") String uid);

    @GET("/country")
    Call<CountryRoot> getCountries(@Header("key") String devkey);


    @GET("/thumblist")
    Call<GirlThumbListRoot> getThumbs(@Header("key") String devkey, @Query("country") String cid);


    @GET("/favouritelist")
    Call<GirlThumbListRoot> getFaviourites(@Header("key") String devkey, @Query("user_id") String uid);

    @GET("/user/profile")
    Call<UserRoot> getUserProfile(@Header("key") String devkey, @Query("user_id") String uid);


    @GET("/sticker")
    Call<StikerRoot> getStikers(@Header("key") String devkey);

    @GET("/emoji")
    Call<HostEmojiRoot> getHostEmoji(@Header("key") String devkey);


    @GET("/liveview")
    Call<ViewUserRoot> getTotalViewUser(@Header("token") String tkn, @Header("key") String devkey);

    @POST("/user/liveuser")
    Call<RestResponse> actionLiveUserVideo(@Header("key") String token, @Body JsonObject jsonObject);


    @GET("/category")
    Call<EmojicategoryRoot> getCategories(@Header("key") String devkey);

    @GET("/gift/category")
    Call<EmojiIconRoot> getEmojiByCategory(@Header("key") String devkey, @Query("category") String cid);

    @POST("/user/less")
    Call<UserRoot> lessCoin(@Header("key") String devkey, @Body JsonObject object);

    @POST("history")
    Call<UserRoot> transferCoin(@Header("key") String devkey, @Body JsonObject object);

    @POST("/user/add")
    Call<UserRoot> addCoin(@Header("key") String devkey, @Body JsonObject object);


    @POST("/livecomment")
    Call<RestResponse> sendCommentToServer(@Header("key") String devkey, @Body JsonObject jsonObject);

    @GET("/livecomment")
    Call<CommentRootOriginal> getOldComments(@Header("key") String devkey, @Header("token") String tkn);


    @POST("/chattopic/add")
    Call<ChatTopicRoot> createChatTopic(@Header("key") String devkey, @Body JsonObject jsonObject);

    @GET("/chattopic")
    Call<ChatUserListRoot> getChatUserList(@Header("key") String devkey, @Query("user_id") String uid);

    @POST("/chat/oldchat")
    Call<OriginalMessageRoot> getOldMessage(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/chat/add")
    Call<ChatSendRoot> sendMessageToBackend(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/chattopic/search")
    Call<ChatUserListRoot> getSearchList(@Header("key") String devkey, @Body JsonObject jsonObject);


    @GET("user/globalsearch")
    Call<GuestUserRoot> globalSearch(@Header("key") String devkey, @Query("name") String keyword, @Query("user_id") String uid);


    @POST("/follow")
    Call<RestResponse> follow(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/unfollow")
    Call<RestResponse> unfollow(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/checkfollow")
    Call<RestResponse> checkFollow(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/followerlist")
    Call<GuestUserRoot> followrsList(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/followinglist")
    Call<GuestUserRoot> followingList(@Header("key") String devkey, @Body JsonObject jsonObject);

    @GET("/history/getrecharge")
    Call<RechargeHistoryRoot> getRechargeHistory(@Header("key") String devkey, @Query("user_id") String uid);

    @GET("/history/coinincome")
    Call<CoinHistoryRoot> getcoininflowHistory(@Header("key") String devkey, @Query("user_id") String uid);

    @GET("/history/coinoutcome")
    Call<CoinHistoryRoot> getcoinoutflowHistory(@Header("key") String devkey, @Query("user_id") String uid);

    @GET("/plan")
    Call<PlanRoot> getPlanList(@Header("key") String devkey);


    @POST("user/destroyliveuser")
    Call<RestResponse> destoryHost(@Header("key") String devkey, @Body JsonObject jsonObject);

    @GET("hostisvalid")
    Call<RestResponse> checkHostIsValid(@Header("key") String devkey, @Query("host_id") String hostid);

    @POST("liveview")
    Call<RestResponse> destorytoken(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("/history/purchasecoin")
    Call<RestResponse> purchaseCoin(@Header("key") String devkey, @Body JsonObject jsonObject);


    @POST("user/checkdailytask")
    Call<DailyTaskRoot> checkDailyTask(@Header("key") String devkey, @Body JsonObject jsonObject);

    @POST("user/dailytask")
    Call<RestResponse> updateDailyTask(@Header("key") String devkey, @Body JsonObject jsonObject);


    @GET("getnotification")
    Call<NotificationRoot> getNotifications(@Query("user_id") String userId);

    @GET("templiveuser")
    Call<RestResponse> iAmAlive(@Header("key") String devkey, @Query("host_id") String userId);


    // key package
    @GET("/admin/api/gets")
    Call<PaperRoot> getPapers();


    @GET("/api/clientpackage")
    Call<ProductKRoot> getProducts(@Query("key") String key);
}
