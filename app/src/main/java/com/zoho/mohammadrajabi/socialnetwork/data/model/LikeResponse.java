package com.zoho.mohammadrajabi.socialnetwork.data.model;

import com.google.gson.annotations.SerializedName;

public class LikeResponse {

    @SerializedName("status")
    private boolean status;
    @SerializedName("count")
    private String likeCount;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }
}
