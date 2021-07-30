package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

public class DailyTaskRoot {

    @SerializedName("number")
    private int number;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public int getNumber() {
        return number;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}