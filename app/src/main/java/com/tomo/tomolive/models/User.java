package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("image")
    private String image;

    @SerializedName("bio")
    private String bio;

    @SerializedName("rate")
    private int rate;

    @SerializedName("createdAt")
    private String createdAt;

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

    @SerializedName("block")
    private boolean block;

    @SerializedName("_id")
    private String id;

    @SerializedName("country")
    private String country;

    @SerializedName("coin")
    private int coin;

    @SerializedName("username")
    private String username;

    @SerializedName("updatedAt")
    private String updatedAt;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public String getBio() {
        return bio;
    }

    public String getCreatedAt() {
        return createdAt;
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

    public boolean isBlock() {
        return block;
    }

    public String getId() {
        return id;
    }

    public int getCoin() {
        return coin;
    }

    public String getUsername() {
        return username;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}