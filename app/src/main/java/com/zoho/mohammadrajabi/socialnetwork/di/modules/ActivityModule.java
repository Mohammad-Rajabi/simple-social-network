package com.zoho.mohammadrajabi.socialnetwork.di.modules;


import com.zoho.mohammadrajabi.socialnetwork.ui.activity.SendPostActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.CommentActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.LoginActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.MainActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.PostsActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.ProfileEditActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.SignUpActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract SplashActivity splashActivityInject();

    @ContributesAndroidInjector
    abstract LoginActivity loginActivityInject();

    @ContributesAndroidInjector
    abstract SignUpActivity signUpActivityInject();

    @ContributesAndroidInjector
    abstract CommentActivity commentActivityInject();

    @ContributesAndroidInjector
    abstract MainActivity mainActivityInject();

    @ContributesAndroidInjector
    abstract PostsActivity postsActivityInject();

    @ContributesAndroidInjector
    abstract ProfileEditActivity profileEditActivityInject();

    @ContributesAndroidInjector
    abstract SendPostActivity addPostActivityInject();
}
