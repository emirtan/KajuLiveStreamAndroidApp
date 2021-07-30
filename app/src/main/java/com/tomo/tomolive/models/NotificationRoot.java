package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationRoot {

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

        @SerializedName("image")
        private String image;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("time")
        private String time;
        @SerializedName("__v")
        private int V;
        @SerializedName("description")
        private String description;
        @SerializedName("_id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("type")
        private String type;
        @SerializedName("updatedAt")
        private String updatedAt;

        public String getTime() {
            return time;
        }

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUserId() {
            return userId;
        }

        public int getV() {
            return V;
        }

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}