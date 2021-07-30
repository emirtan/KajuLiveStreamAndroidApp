package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatUserListRoot {

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

    public boolean getStatus() {
        return status;
    }

    public static class DataItem {
        @SerializedName("country_name")
        private String countryName;

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        @SerializedName("name")
        private String name;

        @SerializedName("image")
        private String image;

        @SerializedName("topic")
        private String topic;

        @SerializedName("_id")
        private String id;

        @SerializedName("time")
        private String time;

        @SerializedName("message")
        private String message;

        public String getname() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getTopic() {
            return topic;
        }

        public String getId() {
            return id;
        }

        public String getTime() {
            return time;
        }

        public String getMessage() {
            return message;
        }
    }
}