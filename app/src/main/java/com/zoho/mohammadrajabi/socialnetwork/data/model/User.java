package com.zoho.mohammadrajabi.socialnetwork.data.model;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.util.ImageUtil;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private String profileImage;
    private String phone;

    @BindingAdapter("profileSrc")
    public static void loadProfileSrc(CircleImageView imageView, String imageUrl) {

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
        } else
            Glide.with(imageView.getContext()).load(imageView.getContext().getResources().getDrawable(R.drawable.photo_male_3)).into(imageView);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
