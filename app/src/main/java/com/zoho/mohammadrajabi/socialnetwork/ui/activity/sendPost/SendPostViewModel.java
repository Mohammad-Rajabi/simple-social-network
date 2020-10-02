package com.zoho.mohammadrajabi.socialnetwork.ui.activity.sendPost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendPostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SuccessResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.SendPostRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.MultipartBody;

public class SendPostViewModel extends ViewModel {

    private SendPostRepository sendPostRepository;
    private MutableLiveData<Resources<SendPostResponse>> result;
    private Disposable disposable;

    @Inject
    public SendPostViewModel(SendPostRepository sendPostRepository) {
        this.sendPostRepository = sendPostRepository;
        result = new MutableLiveData<>();
    }

    public LiveData<Resources<SendPostResponse>> sendPost(int userId, MultipartBody.Part postImage, String postContent) {

        result.setValue(Resources.onLoading());

        sendPostRepository.sendPost(userId, postImage, postContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SendPostResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(SendPostResponse sendPostResponse) {
                        result.setValue(Resources.onSuccess(sendPostResponse));
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
