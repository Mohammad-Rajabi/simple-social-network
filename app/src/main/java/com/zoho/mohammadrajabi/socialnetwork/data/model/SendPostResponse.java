package com.zoho.mohammadrajabi.socialnetwork.data.model;

import com.google.gson.annotations.SerializedName;

public class SendPostResponse {

    @SerializedName("status")
    private boolean status;
    @SerializedName("messgae")
    private String message;
    @SerializedName("postdata")
    private Post post;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
