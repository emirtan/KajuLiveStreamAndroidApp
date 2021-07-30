
package com.tomo.tomolive.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class EmojiIconRoot {

    @Expose
    private List<Datum> data;
    @Expose
    private String message;
    @Expose
    private boolean status;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @SuppressWarnings("unused")
    public static class Datum {

        @SerializedName("__v")
        private Long _V;
        @Expose
        private String _id;
        @Expose
        private String category;
        @Expose
        private Long coin;
        @Expose
        private String createdAt;
        @Expose
        private String icon;
        @Expose
        private String updatedAt;

        public Long get_V() {
            return _V;
        }

        public void set_V(Long _V) {
            this._V = _V;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Long getCoin() {
            return coin;
        }

        public void setCoin(Long coin) {
            this.coin = coin;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
