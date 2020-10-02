package com.zoho.mohammadrajabi.socialnetwork.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserInfo {
    private static final String SHARED_NAME = "userInfo";
    private static final String USER_ID = "userId";
    private static final String IMAGE_URL = "imageUrl";
    private SharedPreferences sharedPreferences;

    @Inject
    public UserInfo(Context context) {
        this.sharedPreferences = context.getSharedPreferences(UserInfo.SHARED_NAME, Context.MODE_PRIVATE);
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(UserInfo.USER_ID, userId);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(UserInfo.USER_ID, "");
    }

    public void setUserProfileImage(String imageUrl) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(UserInfo.IMAGE_URL, imageUrl);
        editor.commit();
    }

    public String getImageUrl() {
        return sharedPreferences.getString(UserInfo.IMAGE_URL, "");
    }

}
