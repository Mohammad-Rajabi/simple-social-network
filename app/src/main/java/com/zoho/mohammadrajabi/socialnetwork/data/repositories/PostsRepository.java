package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;

public class PostsRepository {

    private ApiService apiService;

    @Inject
    public PostsRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<LikeResponse> likePost(int postId) {
        return apiService.likePost(postId);
    }
}
