package com.zoho.mohammadrajabi.socialnetwork.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionManager {

    private static final String SHARED_NAME = "sessionManager";
    private static final String IS_LOGGED_IN = "isLogin";
    private SharedPreferences preferences;

    @Inject
    public SessionManager(Context context) {
        this.preferences = context.getSharedPreferences(SessionManager.SHARED_NAME, Context.MODE_PRIVATE);
    }


    public void setLogin(boolean isLogin) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putBoolean(SessionManager.IS_LOGGED_IN, isLogin);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(SessionManager.IS_LOGGED_IN, false);
    }
}
