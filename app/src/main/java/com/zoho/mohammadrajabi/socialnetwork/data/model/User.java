package com.zoho.mohammadrajabi.socialnetwork.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.Glide;
import com.zoho.mohammadrajabi.socialnetwork.R;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class User implements Parcelable {

    private int id;
    private String username;
    private String password;
    private String profileImage;
    private String phone;

    public User(){}

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        profileImage = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(profileImage);
        dest.writeString(phone);
    }
}
