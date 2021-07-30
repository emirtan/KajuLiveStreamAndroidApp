package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewUserRoot {

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

        @SerializedName("image")
        private String image;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("name")
        private String name;

        @SerializedName("token")
        private String token;

        public String getImage() {
            return image;
        }

        public String getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getToken() {
            return token;
        }
    }
}