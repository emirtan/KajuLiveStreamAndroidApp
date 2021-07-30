package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GirlThumbListRoot {

    @SerializedName("data")
    private List<Thumb> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<Thumb> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }
}