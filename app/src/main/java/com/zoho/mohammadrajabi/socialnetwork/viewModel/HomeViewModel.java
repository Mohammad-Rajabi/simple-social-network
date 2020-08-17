package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.data.model.PostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.HomeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class HomeViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private BehaviorSubject<Boolean> progressBarVisibilitySubject = BehaviorSubject.create();

    @Inject
    public HomeViewModel(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    public Single<PostResponse> getPosts(int currentPage) {
        if (currentPage == 1) {
            progressBarVisibilitySubject.onNext(true);
            return homeRepository.getPosts(currentPage).doOnEvent((postResponse, throwable) -> progressBarVisibilitySubject.onNext(false));
        } else
            return homeRepository.getPosts(currentPage);
    }

    public Single<LikeResponse> likePost(int postId) {
        return homeRepository.likePost(postId);
    }

    public BehaviorSubject<Boolean> getProgressBarVisibilitySubject() {
        return progressBarVisibilitySubject;
    }
}
