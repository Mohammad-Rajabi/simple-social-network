package com.zoho.mohammadrajabi.socialnetwork.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;


@Singleton
public class ViewModelInjector implements ViewModelProvider.Factory {

    private Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelMap;

    @Inject
    public ViewModelInjector(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModelMap) {
        this.viewModelMap = viewModelMap;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) this.viewModelMap.get(modelClass).get();
    }
}
