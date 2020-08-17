package com.zoho.mohammadrajabi.socialnetwork.data.local.memoryCache;


import javax.inject.Inject;

import io.reactivex.Single;

public class PostMemoryCache<T> {


    @Inject
    public PostMemoryCache() {
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

    public Single<T> getData() {
        return Single.create(emitter -> {
            if(data != null) {
                emitter.onSuccess(data);
            }
        });
    }
}
