package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentRootOriginal {

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

        @SerializedName("name")
        private String name;

        @SerializedName("comment")
        private String comment;

        @SerializedName("token")
        private String token;

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public String getToken() {
            return token;
        }
    }
}