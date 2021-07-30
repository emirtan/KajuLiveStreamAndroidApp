package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuestUserRoot {

    @SerializedName("data")
    private List<GuestUser> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<GuestUser> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}