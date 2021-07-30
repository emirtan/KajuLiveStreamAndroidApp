package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanRoot {

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

        @SerializedName("googleProductId")
        private String googleProductId;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("__v")
        private int V;

        @SerializedName("_id")
        private String id;

        @SerializedName("coin")
        private int coin;

        @SerializedName("updatedAt")
        private String updatedAt;

        public int getRupee() {
            return rupee;
        }

        public String getGoogleProductId() {
            return googleProductId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getV() {
            return V;
        }

        public String getId() {
            return id;
        }

        public int getCoin() {
            return coin;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}