package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinHistoryRoot {

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

        @SerializedName("date")
        private String date;

        @SerializedName("person")
        private String person;

        @SerializedName("coin")
        private int coin;

        public String getDate() {
            return date;
        }

        public String getPerson() {
            return person;
        }

        public int getCoin() {
            return coin;
        }
    }
}