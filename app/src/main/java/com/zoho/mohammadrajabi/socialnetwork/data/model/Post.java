package com.zoho.mohammadrajabi.socialnetwork.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.zoho.mohammadrajabi.socialnetwork.R;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post implements Serializable {

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

    @BindingAdapter("profileImage")
    public static void loadProfileImage(CircleImageView imageView, String imageUrl) {
//        if (imageUrl != null || !imageUrl.isEmpty()) {
//            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
//        } else {
//            Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.photo_male_3)).into(imageView);
//        }
        Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.photo_male_3)).into(imageView);
    }

    @BindingAdapter("postImage")
    public static void loadPostImage(ImageView imageView, String imageUrl) {
//        if (imageUrl != null || !imageUrl.isEmpty()) {
//            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
//        } else {
//            Glide.with(imageView.getContext()).load(ContextCompat.getDrawable(imageView.getContext(), R.drawable.spring_in_shiraz)).into(imageView);
//        }
        Glide.with(imageView.getContext()).load(ContextCompat.getDrawable(imageView.getContext(), R.drawable.spring_in_shiraz)).into(imageView);

    }

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

}
