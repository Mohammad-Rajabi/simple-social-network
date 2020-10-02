
package com.zoho.mohammadrajabi.socialnetwork.data.model;


import com.google.gson.annotations.SerializedName;


public class Comment {

    @SerializedName("commentContent")
    private String commentContent;
    @SerializedName("commentId")
    private String commentId;
    @SerializedName("commentOwnerProfileImage")
    private String commentOwnerProfileImage;
    @SerializedName("commentOwnerUsername")
    private String commentOwnerUsername;


    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentOwnerProfileImage() {
        return commentOwnerProfileImage;
    }

    public void setCommentOwnerProfileImage(String commentOwnerProfileImage) {
        this.commentOwnerProfileImage = commentOwnerProfileImage;
    }

    public String getCommentOwnerUsername() {
        return commentOwnerUsername;
    }

    public void setCommentOwnerUsername(String commentOwnerUsername) {
        this.commentOwnerUsername = commentOwnerUsername;
    }


}
