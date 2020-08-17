package com.zoho.mohammadrajabi.socialnetwork.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateUserResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;
    @SerializedName("user")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
