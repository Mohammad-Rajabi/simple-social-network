package com.zoho.mohammadrajabi.socialnetwork.di.modules;

import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.home.HomeFragment;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.profile.ProfileFragment;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.search.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract HomeFragment homeFragmentInject();

    @ContributesAndroidInjector
    abstract SearchFragment searchFragmentInject();

    @ContributesAndroidInjector
    abstract ProfileFragment profileFragmentInject();
}
