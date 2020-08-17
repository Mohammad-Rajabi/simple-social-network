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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditRepository {

    private ApiService apiService;
    private ProfileMemoryCache<MutableLiveData<Resources<UserProfileResponse>>> profileMemoryCache;
    private MutableLiveData result;

    @Inject
    public ProfileEditRepository(ApiService apiService, ProfileMemoryCache<MutableLiveData<Resources<UserProfileResponse>>> profileMemoryCache) {
        this.apiService = apiService;
        this.profileMemoryCache = profileMemoryCache;
    }

    public MutableLiveData<Resources<checkUsernameResponse>> checkUsername(String username) {
        MutableLiveData result = new MutableLiveData<Resources<checkUsernameResponse>>();

        apiService.checkUsername(username).enqueue(new Callback<checkUsernameResponse>() {
            @Override
            public void onResponse(Call<checkUsernameResponse> call, Response<checkUsernameResponse> response) {
                if (response.isSuccessful()) result.postValue(Resources.onSuccess(response.body()));
            }

            @Override
            public void onFailure(Call<checkUsernameResponse> call, Throwable t) {
                if (t instanceof ConnectivityException || t instanceof java.net.ConnectException)
                    result.postValue(Resources.onConnectivity());
                else result.postValue(Resources.onError(""));
            }
        });
        return result;
    }

    public MutableLiveData<Resources<UpdateUserResponse>> editProfile(int userId, String username, String password, String phone, MultipartBody.Part multipartBody) {

        result = new MutableLiveData<Resources<UpdateUserResponse>>();
        result.setValue(Resources.onLoading());

        apiService.editProfile(userId, username, password, phone, multipartBody).enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                if (response.isSuccessful()) {
                    result.postValue(Resources.onSuccess(response.body()));
                    profileMemoryCache.setInsertedData(false);
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                if (t instanceof ConnectivityException || t instanceof java.net.ConnectException)
                    result.postValue(Resources.onConnectivity());
                else result.postValue(Resources.onError(""));
            }
        });

        return result;
    }
}
