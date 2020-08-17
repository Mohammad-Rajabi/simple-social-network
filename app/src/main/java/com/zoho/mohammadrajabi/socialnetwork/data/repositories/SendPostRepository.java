package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.local.memoryCache.ProfileMemoryCache;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendPostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SuccessResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;
import okhttp3.MultipartBody;

public class SendPostRepository {

    private ApiService apiService;
    private ProfileMemoryCache<UserProfileResponse> profileMemoryCache;

    @Inject
    public SendPostRepository(ApiService apiService, ProfileMemoryCache<UserProfileResponse> profileMemoryCache) {
        this.apiService = apiService;
        this.profileMemoryCache = profileMemoryCache;
    }

    public Single<SendPostResponse> sendPost(int userId, MultipartBody.Part postImage, String postContent) {
        return apiService.sendPost(userId, postContent, postImage).doOnSuccess(sendPostResponse -> profileMemoryCache.setInsertedData(false));
    }
}
