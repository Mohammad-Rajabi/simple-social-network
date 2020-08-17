package com.zoho.mohammadrajabi.socialnetwork.data.repositories;


import com.zoho.mohammadrajabi.socialnetwork.data.local.memoryCache.ProfileMemoryCache;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;


public class ProfileRepository {

    private ApiService apiService;
    private ProfileMemoryCache<UserProfileResponse> profileMemoryCache;

    @Inject
    public ProfileRepository(ApiService apiService, ProfileMemoryCache<UserProfileResponse> profileMemoryCache) {
        this.apiService = apiService;
        this.profileMemoryCache = profileMemoryCache;
    }

    public Single<UserProfileResponse> getProfile(String userId) {
        return apiService.getProfile(userId).doOnSuccess(userProfileResponse -> {
            profileMemoryCache.cacheInMemory(userProfileResponse);
            profileMemoryCache.setInsertedData(true);
        });
    }

}
