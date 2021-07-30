package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

public class GuestUser {

    @SerializedName("country")
    private String country;

    @SerializedName("image")
    private String image;

    @SerializedName("isFollowing")
    private boolean isFollowing;

    @SerializedName("bio")
    private Object bio;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("rate")
    private int rate;

    @SerializedName("following_count")
    private int followingCount;

    @SerializedName("identity")
    private String identity;

    @SerializedName("followers_count")
    private int followersCount;

    @SerializedName("__v")
    private int V;

    @SerializedName("name")
    private String name;

    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("coin")
    private int coin;

    @SerializedName("updatedAt")
    private String updatedAt;

    public String getCountry() {
        return country;
    }

    public String getImage() {
        return image;
    }

    public boolean isIsFollowing() {
        return isFollowing;
    }

    public Object getBio() {
        return bio;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getRate() {
        return rate;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getIdentity() {
        return identity;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getV() {
        return V;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getCoin() {
        return coin;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}