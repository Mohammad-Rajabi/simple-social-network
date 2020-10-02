package com.zoho.mohammadrajabi.socialnetwork.ui.fragment.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.ProfileRepository;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

public class ProfileViewModel extends ViewModel {

    private ApiService apiService;
    private Disposable disposable;
    private MutableLiveData<Resources<UserProfileResponse>> result;

    @Inject
    public ProfileViewModel(ApiService apiService) {
        this.apiService = apiService;
        result = new MutableLiveData<>();
    }

    public LiveData<Resources<UserProfileResponse>> getProfile(String userId) {

        apiService.getProfile(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(UserProfileResponse userProfileResponse) {
                        result.setValue(Resources.onSuccess(userProfileResponse));
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
        if (disposable != null && !disposable.isDisposed() )
            disposable.dispose();
    }
}
