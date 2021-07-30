package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RechargeHistoryRoot {

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<DataItem> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class DataItem {

        @SerializedName("rupee")
        private int rupee;

        @SerializedName("date")
        private String date;

        @SerializedName("coin")
        private int coin;

        public int getRupee() {
            return rupee;
        }

        public String getDate() {
            return date;
        }

        public int getCoin() {
            return coin;
        }
    }
}