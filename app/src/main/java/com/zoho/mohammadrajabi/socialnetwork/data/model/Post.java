package com.zoho.mohammadrajabi.socialnetwork.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Post implements Parcelable {

    @SerializedName("id")
    private String postId;
    private String postImage;
    private String postContent;
    @SerializedName("user_id")
    private String postOwnerUsername;
    private String postOwnerProfileImage;
    private boolean isLiked;
    private int likeCount;
    private int commentCount;
    private String commentCountStr;

    public Post(){}

    protected Post(Parcel in) {
        postId = in.readString();
        postImage = in.readString();
        postContent = in.readString();
        postOwnerUsername = in.readString();
        postOwnerProfileImage = in.readString();
        isLiked = in.readByte() != 0;
        likeCount = in.readInt();
        commentCount = in.readInt();
        commentCountStr = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostOwnerUsername() {
        return postOwnerUsername;
    }

    public void setPostOwnerUsername(String postOwnerUsername) {
        this.postOwnerUsername = postOwnerUsername;
    }

    public String getPostOwnerProfileImage() {
        return postOwnerProfileImage;
    }

    public void setPostOwnerProfileImage(String postOwnerProfileImage) {
        this.postOwnerProfileImage = postOwnerProfileImage;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentCountStr() {
        return commentCount + " نظر";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(postImage);
        dest.writeString(postContent);
        dest.writeString(postOwnerUsername);
        dest.writeString(postOwnerProfileImage);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeInt(likeCount);
        dest.writeInt(commentCount);
        dest.writeString(commentCountStr);
    }
}
