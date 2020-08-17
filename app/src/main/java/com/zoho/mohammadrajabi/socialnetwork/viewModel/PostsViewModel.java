package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.PostsRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class PostsViewModel extends ViewModel {

    private PostsRepository postsRepository;

    @Inject
    public PostsViewModel(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public Single<LikeResponse> likePost(int postId) {
        return postsRepository.likePost(postId);
    }
}
