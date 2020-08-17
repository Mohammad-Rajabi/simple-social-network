
package com.zoho.mohammadrajabi.socialnetwork.data.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SendCommentResponse {

    @SerializedName("comments")
    private List<Comment> comments;
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private boolean status;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

}
