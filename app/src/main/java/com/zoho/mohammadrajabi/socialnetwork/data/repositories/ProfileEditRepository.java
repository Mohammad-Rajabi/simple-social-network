package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.local.memoryCache.ProfileMemoryCache;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UpdateUserResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditRepository {

    private ApiService apiService;
    private ProfileMemoryCache<MutableLiveData<Resources<UserProfileResponse>>> profileMemoryCache;

    @Inject
    public ProfileEditRepository(ApiService apiService, ProfileMemoryCache<MutableLiveData<Resources<UserProfileResponse>>> profileMemoryCache) {
        this.apiService = apiService;
        this.profileMemoryCache = profileMemoryCache;
    }


    public Single<UpdateUserResponse> editProfile(int userId, String username, String password, String phone, MultipartBody.Part multipartBody) {

        return apiService.editProfile(userId, username, password, phone, multipartBody)
                .doOnSuccess(updateUserResponse -> {
                    profileMemoryCache.setInsertedData(false);
                });
    }

}
