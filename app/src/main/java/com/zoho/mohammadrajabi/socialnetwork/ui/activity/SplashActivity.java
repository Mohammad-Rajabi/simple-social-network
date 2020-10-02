package com.zoho.mohammadrajabi.socialnetwork.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.zoho.mohammadrajabi.socialnetwork.data.local.SessionManager;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.local.UserInfo;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivitySplashBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.login.LoginActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SplashActivity extends AppCompatActivity {

    @Inject
    SessionManager sessionManager;
    @Inject
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!userInfo.getUserId().isEmpty() && userInfo.getUserId() != null) {
            TokenContainer.updateUserId(userInfo.getUserId());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSessionStatus();
            }
        }, 2000);

    }

    public void getSessionStatus() {
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }
}
