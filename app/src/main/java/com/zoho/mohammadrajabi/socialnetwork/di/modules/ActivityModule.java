package com.zoho.mohammadrajabi.socialnetwork.di.modules;


import com.zoho.mohammadrajabi.socialnetwork.ui.activity.sendPost.SendPostActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.comment.CommentActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.login.LoginActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.MainActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.posts.PostsActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.profileEdit.ProfileEditActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.signup.SignUpActivity;
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
