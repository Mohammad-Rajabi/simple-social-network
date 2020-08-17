package com.zoho.mohammadrajabi.socialnetwork.data.local.memoryCache;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProfileMemoryCache<T> {

    @Inject
    public ProfileMemoryCache() {
    }

    private T data;
    private boolean insertedData;

    public boolean isInsertedData() {
        return insertedData;
    }

    public void setInsertedData(boolean insertedData) {
        this.insertedData = insertedData;
    }

    public void cacheInMemory(T data) {
        this.data = data;
    }

    public MutableLiveData<T> getData() {
        return (MutableLiveData<T>) data;
    }


}
