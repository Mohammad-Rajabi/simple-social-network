package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.model.CommentsResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendCommentResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.CommentRepository;


import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class CommentViewModel extends ViewModel {

    private CommentRepository commentRepository;
    private BehaviorSubject<Boolean> progressBarVisibilitySubject = BehaviorSubject.create();

    @Inject
    public CommentViewModel(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Single<CommentsResponse> getComments(int postId) {
        progressBarVisibilitySubject.onNext(true);
        return commentRepository.getComments(postId).doOnEvent((comments, throwable) -> progressBarVisibilitySubject.onNext(false));
    }

    public Single<SendCommentResponse> sendComment(String comment, int postId, String userId) {
        return commentRepository.sendComment(comment, postId, userId);
    }

    public BehaviorSubject<Boolean> getProgressBarVisibilitySubject() {
        return progressBarVisibilitySubject;
    }
}
