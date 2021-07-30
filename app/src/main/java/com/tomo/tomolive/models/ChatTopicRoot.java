package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

public class ChatTopicRoot {

    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }

    public static class Data {

        @SerializedName("createdAt")
        private String createdAt;

        @SerializedName("receiver_id")
        private String receiverId;

        @SerializedName("__v")
        private int V;

        @SerializedName("_id")
        private String id;

        @SerializedName("sender_id")
        private String senderId;

        @SerializedName("updatedAt")
        private String updatedAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public int getV() {
            return V;
        }

        public String getId() {
            return id;
        }

        public String getSenderId() {
            return senderId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}