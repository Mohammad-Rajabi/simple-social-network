package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import com.zoho.mohammadrajabi.socialnetwork.data.local.memoryCache.PostMemoryCache;
import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.PostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;

public class HomeRepository {

    private ApiService apiService;
    private PostMemoryCache<PostResponse> memoryCache;

    @Inject
    public HomeRepository(ApiService apiService, PostMemoryCache<PostResponse> memoryCache) {
        this.apiService = apiService;
        this.memoryCache = memoryCache;
    }

    public Single<PostResponse> getPosts() {
        if (memoryCache.isInsertedData()) {
            return memoryCache.getData();
        } else {
            return apiService.getPosts().doOnSuccess(postResponse -> {
                memoryCache.cacheInMemory(postResponse);
                memoryCache.setInsertedData(true);
            });
        }

    }

    public Single<LikeResponse> likePost(int postId) {
        return apiService.likePost(postId);
    }
}
