package com.zoho.mohammadrajabi.socialnetwork.di.keys;


import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dagger.MapKey;

@Documented
@MapKey
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelKey{
    Class<? extends ViewModel> value();
}
