
package com.tomo.tomolive.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class EmojicategoryRoot {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private boolean mStatus;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public boolean getStatus() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }

    @SuppressWarnings("unused")
    public static class Datum {

        @SerializedName("createdAt")
        private String mCreatedAt;
        @SerializedName("icon")
        private String mIcon;
        @SerializedName("isTop")
        private Boolean mIsTop;
        @SerializedName("name")
        private String mName;
        @SerializedName("updatedAt")
        private String mUpdatedAt;
        @SerializedName("__v")
        private Long m_V;
        @SerializedName("_id")
        private String m_id;

        public String getCreatedAt() {
            return mCreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
        }

        public String getIcon() {
            return mIcon;
        }

        public void setIcon(String icon) {
            mIcon = icon;
        }

        public Boolean getIsTop() {
            return mIsTop;
        }

        public void setIsTop(Boolean isTop) {
            mIsTop = isTop;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getUpdatedAt() {
            return mUpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            mUpdatedAt = updatedAt;
        }

        public Long get_V() {
            return m_V;
        }

        public void set_V(Long _V) {
            m_V = _V;
        }

        public String get_id() {
            return m_id;
        }

        public void set_id(String _id) {
            m_id = _id;
        }

    }
}
