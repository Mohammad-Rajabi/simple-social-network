package com.zoho.mohammadrajabi.socialnetwork.ui.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.data.model.PostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.HomeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class HomeViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private MutableLiveData<Resources<PostResponse>> postResult;
    private MutableLiveData<Resources<LikeResponse>> likeResult;
    private CompositeDisposable compositeDisposable;

    @Inject
    public HomeViewModel(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
        postResult = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();

        getPosts();
    }

    private void getPosts() {

        postResult.setValue(Resources.onLoading());
        homeRepository.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PostResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PostResponse postResponse) {
                        postResult.setValue(Resources.onSuccess(postResponse));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    public LiveData<Resources<PostResponse>> observePosts() {
        return postResult;
    }

    public LiveData<Resources<LikeResponse>> likePost(int postId) {
        likeResult = new MutableLiveData<>();

        homeRepository.likePost(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LikeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LikeResponse likeResponse) {
                        likeResult.setValue(Resources.onSuccess(likeResponse));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        return likeResult;
    }

    @Override
    protected void onCleared() {
        if (!compositeDisposable.isDisposed())
            compositeDisposable.isDisposed();
        super.onCleared();
    }
}
