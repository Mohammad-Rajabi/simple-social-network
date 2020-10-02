package com.zoho.mohammadrajabi.socialnetwork.ui.activity.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PostsViewModel extends ViewModel {

    private ApiService apiService;
    private Disposable disposable;
    private MutableLiveData<Resources<LikeResponse>> likeResultLiveData;

    @Inject
    public PostsViewModel(ApiService apiService) {
        this.apiService = apiService;
        likeResultLiveData = new MutableLiveData<>();
    }

    public LiveData<Resources<LikeResponse>> likePost(int postId) {

        apiService.likePost(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LikeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(LikeResponse likeResponse) {
                        if (likeResponse.isStatus()) {
                            likeResultLiveData.setValue(Resources.onSuccess(likeResponse));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException) {
                            likeResultLiveData.setValue(Resources.onConnectivity());
                        }
                    }
                });
        return likeResultLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
