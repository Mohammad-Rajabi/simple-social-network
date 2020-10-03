package com.zoho.mohammadrajabi.socialnetwork.ui.activity.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignUpResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignUpViewModel extends ViewModel {

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;
    private MutableLiveData<Resources<checkUsernameResponse>> checkUsernameResult;

    @Inject
    public SignUpViewModel(ApiService apiService) {
        this.apiService = apiService;
        this.compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resources<checkUsernameResponse>> checkUsername(String username) {

        checkUsernameResult = new MutableLiveData<>();

        apiService.checkUsername(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<checkUsernameResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(checkUsernameResponse checkUsernameResponse) {
                        checkUsernameResult.setValue(Resources.onSuccess(checkUsernameResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException || e instanceof java.net.ConnectException)
                            checkUsernameResult.setValue(Resources.onConnectivity());
                        else checkUsernameResult.setValue(Resources.onError(""));
                    }
                });
        return checkUsernameResult;
    }

    public MutableLiveData<Resources<SignUpResponse>> signUp(String username, String password, String phone) {

        MutableLiveData result = new MutableLiveData<Resources<SignUpResponse>>();
        result.setValue(Resources.onLoading());

        apiService.singUp(username, password, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignUpResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(SignUpResponse signUpResponse) {
                        result.setValue(Resources.onSuccess(signUpResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException)
                            result.setValue(Resources.onConnectivity());
                        else result.setValue(Resources.onError(""));
                    }
                });

        return result;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.isDisposed();
    }
}
