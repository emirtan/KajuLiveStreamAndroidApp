package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

public class Thumb {
    boolean isFake = false;

    public Thumb(boolean isFake, String countryName, String image, String username, String name, String channel, int view, String type, String countryId, int coin, String token, String hostId) {
        this.isFake = isFake;
        this.countryName = countryName;
        this.image = image;
        this.username = username;
        this.name = name;
        this.channel = channel;
        this.view = view;
        this.type = type;
        this.countryId = countryId;
        this.coin = coin;
        this.token = token;
        this.hostId = hostId;
    }

    public Thumb() {
    }

    public boolean isFake() {
        return isFake;
    }

    public void setFake(boolean fake) {
        isFake = fake;
    }

    @SerializedName("country_name")
    private String countryName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @SerializedName("image")
    private String image;

    @SerializedName("rate")
    private int rate = 0;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String name;

    @SerializedName("channel")
    private String channel;

    @SerializedName("view")
    private int view;

    @SerializedName("type")
    private String type;

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("coin")
    private int coin;

    @SerializedName("token")
    private String token;

    @SerializedName("host_id")
    private String hostId;

    public String getUsername() {
        return username;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public String getCountryId() {
        return countryId;
    }

    public int getCoin() {
        return coin;
    }

    public String getToken() {
        return token;
    }

    public String getHostId() {
        return hostId;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}