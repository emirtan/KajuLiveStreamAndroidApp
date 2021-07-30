package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryRoot {

    @SerializedName("country")
    private List<CountryItem> country;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public List<CountryItem> getCountry() {
        return country;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public static class CountryItem {

        @SerializedName("image")
        private String image;

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("__v")
        private int V;

        @SerializedName("name")
        private String name;

        @SerializedName("_id")
        private String id;

        @SerializedName("updatedAt")
        private String updatedAt;

        public CountryItem(String name) {

            this.name = name;
            this.id = name;
        }

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getV() {
            return V;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}