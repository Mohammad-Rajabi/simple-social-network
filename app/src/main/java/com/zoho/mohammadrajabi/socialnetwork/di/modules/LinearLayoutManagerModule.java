package com.zoho.mohammadrajabi.socialnetwork.di.modules;


import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.Module;
import dagger.Provides;

@Module
public class LinearLayoutManagerModule {

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
    }
}
