package com.zoho.mohammadrajabi.socialnetwork;


import com.zoho.mohammadrajabi.socialnetwork.di.component.DaggerApplicationComponent;


import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class App extends DaggerApplication {


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.factory().create(this);
    }


}
