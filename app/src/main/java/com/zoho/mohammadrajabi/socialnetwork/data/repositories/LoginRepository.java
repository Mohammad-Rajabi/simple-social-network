package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignInResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginRepository {

    private ApiService apiService;

    @Inject
    public LoginRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public MutableLiveData<Resources<SignInResponse>> login(String username, String password) {

        MutableLiveData result = new MutableLiveData<Resources<SignInResponse>>();

        result.setValue(Resources.onLoading());

        apiService.login(username, password).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.isSuccessful()) result.postValue(Resources.onSuccess(response.body()));
                if(!response.isSuccessful()) result.postValue(Resources.onError("کاربری با این مشخصات یافت نشد"));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (t instanceof ConnectivityException)
                    result.postValue(Resources.onConnectivity());
                else result.postValue(Resources.onError(""));
            }
        });
        return result;
    }
}
