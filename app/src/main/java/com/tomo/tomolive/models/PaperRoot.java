package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaperRoot {

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

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("image")
        private String image;

        @SerializedName("password")
        private String password;

        @SerializedName("flag")
        private boolean flag;

        @SerializedName("package")
        private Object jsonMemberPackage;

        @SerializedName("__v")
        private int V;

        @SerializedName("name")
        private String name;

        @SerializedName("_id")
        private String id;

        @SerializedName("key")
        private String key;

        @SerializedName("email")
        private String email;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public String getImage() {
            return image;
        }

        public String getPassword() {
            return password;
        }

        public boolean isFlag() {
            return flag;
        }

        public Object getJsonMemberPackage() {
            return jsonMemberPackage;
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

        public String getKey() {
            return key;
        }

        public String getEmail() {
            return email;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}