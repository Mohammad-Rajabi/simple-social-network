package com.zoho.mohammadrajabi.socialnetwork.ui.activity.comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.CommentsResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendCommentResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;


import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<Resources<CommentsResponse>> result;
    private MutableLiveData<Resources<SendCommentResponse>> sendCommentResult;
    private ApiService apiService;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CommentViewModel(ApiService apiService) {
        this.apiService = apiService;
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resources<CommentsResponse>> getComments(int postId) {

        if (result == null)
            result = new MutableLiveData<>();

        result.setValue(Resources.onLoading());

        apiService.getComments(postId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CommentsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(CommentsResponse commentsResponse) {
                        if (commentsResponse.isStatus()) {
                            result.setValue(Resources.onSuccess(commentsResponse));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException) {
                            result.setValue(Resources.onConnectivity());
                        } else {
                            result.setValue(Resources.onError(""));
                        }
                    }
                });

        return result;
    }

    public LiveData<Resources<SendCommentResponse>> sendComment(String comment, int postId, String userId) {

        if (sendCommentResult == null)
            sendCommentResult = new MutableLiveData<>();

        apiService.sendComment(comment, postId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SendCommentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(SendCommentResponse sendCommentResponse) {
                        sendCommentResult.setValue(Resources.onSuccess(sendCommentResponse));
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof ConnectivityException) {
                            sendCommentResult.setValue(Resources.onConnectivity());
                        }
                    }
                });

        return sendCommentResult;
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
