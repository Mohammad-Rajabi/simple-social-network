package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.model.SendPostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SuccessResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.SendPostRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.MultipartBody;

public class SendPostViewModel extends ViewModel {

    private SendPostRepository sendPostRepository;
    private BehaviorSubject<Boolean> progressBarVisibilitySubject = BehaviorSubject.create();

    @Inject
    public SendPostViewModel(SendPostRepository sendPostRepository) {
        this.sendPostRepository = sendPostRepository;
    }

    public Single<SendPostResponse> sendPost(int userId, MultipartBody.Part postImage, String postContent) {
        progressBarVisibilitySubject.onNext(true);
        return sendPostRepository.sendPost(userId,postImage, postContent).doOnEvent(new BiConsumer<SendPostResponse, Throwable>() {
            @Override
            public void accept(SendPostResponse successResponse, Throwable throwable) throws Exception {
                progressBarVisibilitySubject.onNext(false);
            }
        });
    }

    public BehaviorSubject<Boolean> getProgressBarVisibilitySubject() {
        return progressBarVisibilitySubject;
    }
}
