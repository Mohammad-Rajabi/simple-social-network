package com.zoho.mohammadrajabi.socialnetwork.di.component;

import android.content.Context;

import com.zoho.mohammadrajabi.socialnetwork.App;
import com.zoho.mohammadrajabi.socialnetwork.di.modules.ActivityModule;
import com.zoho.mohammadrajabi.socialnetwork.di.modules.FragmentModule;
import com.zoho.mohammadrajabi.socialnetwork.di.modules.LinearLayoutManagerModule;
import com.zoho.mohammadrajabi.socialnetwork.di.modules.NetworkModule;
import com.zoho.mohammadrajabi.socialnetwork.di.modules.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class,
        ActivityModule.class,
        NetworkModule.class,
        ViewModelModule.class,
        LinearLayoutManagerModule.class,
        FragmentModule.class})
public interface ApplicationComponent extends AndroidInjector<App> {

    @Component.Factory
    interface Factory {

        ApplicationComponent create(@BindsInstance Context context);
    }
}
