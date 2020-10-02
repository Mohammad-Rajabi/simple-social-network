package com.zoho.mohammadrajabi.socialnetwork.ui.activity.profileEdit;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UpdateUserResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;


public class ProfileEditViewModel extends ViewModel {


    private ApiService apiService;
    private MutableLiveData<Resources<checkUsernameResponse>> checkUsernameResult;
    private MutableLiveData<Resources<UpdateUserResponse>> updateResult;
    private CompositeDisposable compositeDisposable;

    @Inject
    public ProfileEditViewModel(ApiService apiService) {
        this.apiService = apiService;
        checkUsernameResult = new MutableLiveData<>();
        updateResult = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resources<checkUsernameResponse>> checkUsername(String username) {

        checkUsernameResult = new MutableLiveData<>();

        apiService.checkUsername(username)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<checkUsernameResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(checkUsernameResponse checkUsernameResponse) {
                        checkUsernameResult.postValue(Resources.onSuccess(checkUsernameResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException || e instanceof java.net.ConnectException)
                            checkUsernameResult.postValue(Resources.onConnectivity());
                        else checkUsernameResult.postValue(Resources.onError(""));
                    }
                });
        return checkUsernameResult;
    }

    public LiveData<Resources<UpdateUserResponse>> editProfile(int userId, String username, String password, String phone, MultipartBody.Part multipartBody) {

        updateResult.setValue(Resources.onLoading());

        apiService.editProfile(userId, username, password, phone, multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UpdateUserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UpdateUserResponse updateUserResponse) {
                        updateResult.setValue(Resources.onSuccess(updateUserResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException || e instanceof java.net.ConnectException)
                            updateResult.setValue(Resources.onConnectivity());
                        else updateResult.setValue(Resources.onError(""));
                    }
                });
        return updateResult;
    }


    @Override
    protected void onCleared() {
        if (!compositeDisposable.isDisposed()) compositeDisposable.isDisposed();
        super.onCleared();
    }
}
