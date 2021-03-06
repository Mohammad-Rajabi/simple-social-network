package com.zoho.mohammadrajabi.socialnetwork.di.modules;


import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.di.keys.ViewModelKey;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.comment.CommentViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.home.HomeViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.login.LoginViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.posts.PostsViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.profileEdit.ProfileEditViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.profile.ProfileViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.search.SearchViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.sendPost.SendPostViewModel;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.signup.SignUpViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @ViewModelKey(LoginViewModel.class)
    @IntoMap
    abstract ViewModel provideLoginViewModel(LoginViewModel loginViewModel);


    @Binds
    @ViewModelKey(CommentViewModel.class)
    @IntoMap
    abstract ViewModel provideCommentViewModel(CommentViewModel commentViewModel);

    @Binds
    @ViewModelKey(SignUpViewModel.class)
    @IntoMap
    abstract ViewModel provideSignUpViewModel(SignUpViewModel signUpViewModel);

    @Binds
    @ViewModelKey(HomeViewModel.class)
    @IntoMap
    abstract ViewModel provideHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @ViewModelKey(SearchViewModel.class)
    @IntoMap
    abstract ViewModel provideSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @ViewModelKey(ProfileViewModel.class)
    @IntoMap
    abstract ViewModel provideProfileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @ViewModelKey(ProfileEditViewModel.class)
    @IntoMap
    abstract ViewModel provideProfileEditViewModel(ProfileEditViewModel profileEditViewModel);

    @Binds
    @ViewModelKey(PostsViewModel.class)
    @IntoMap
    abstract ViewModel providePostsViewModel(PostsViewModel postsViewModel);

    @Binds
    @ViewModelKey(SendPostViewModel.class)
    @IntoMap
    abstract ViewModel provideSendPostViewModel(SendPostViewModel sendPostViewModel);

}
