package com.zoho.mohammadrajabi.socialnetwork.ui.activity.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignInResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private ApiService apiService;
    private MutableLiveData result;
    private Disposable disposable;

    @Inject
    public LoginViewModel(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resources<SignInResponse>> login(String username, String password) {

        if (result == null)
            result = new MutableLiveData<Resources<SignInResponse>>();

        result.setValue(Resources.onLoading());

        apiService.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SignInResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(SignInResponse signInResponse) {
                        if (signInResponse.isStatus())
                            result.setValue(Resources.onSuccess(signInResponse));
                        if (!signInResponse.isStatus())
                            result.setValue(Resources.onError("کاربری با این مشخصات یافت نشد"));
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
        if (disposable != null && !disposable.isDisposed())
            disposable.isDisposed();
    }
}
