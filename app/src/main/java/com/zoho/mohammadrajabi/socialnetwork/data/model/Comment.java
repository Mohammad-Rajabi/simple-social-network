
package com.zoho.mohammadrajabi.socialnetwork.data.model;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;
import com.zoho.mohammadrajabi.socialnetwork.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment {

    @SerializedName("commentContent")
    private String commentContent;
    @SerializedName("commentId")
    private String commentId;
    @SerializedName("commentOwnerProfileImage")
    private String commentOwnerProfileImage;
    @SerializedName("commentOwnerUsername")
    private String commentOwnerUsername;


    @BindingAdapter("profileImg")
    public static void loadProfileImage(CircleImageView imageView, String imageUrl) {

//        if (imageUrl != null || !imageUrl.isEmpty()) {
//            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
//        } else
//            Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.photo_male_2)).into(imageView);

        Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.photo_male_2)).into(imageView);

    }

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
