package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import com.zoho.mohammadrajabi.socialnetwork.data.model.Comment;
import com.zoho.mohammadrajabi.socialnetwork.data.model.CommentsResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendCommentResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;


public class CommentRepository {


    private ApiService apiService;

    @Inject
    public CommentRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<CommentsResponse> getComments(int postId) {

        return apiService.getComments(postId);
    }

    public Single<SendCommentResponse> sendComment(String comment, int postId, String userId) {

        return apiService.sendComment(comment, postId, userId);
    }
}
