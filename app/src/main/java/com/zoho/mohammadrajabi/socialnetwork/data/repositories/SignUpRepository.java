package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignUpResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.rest.ApiService;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepository {

    private ApiService apiService;

    @Inject
    public SignUpRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public MutableLiveData<Resources<SignUpResponse>> signUp(String username, String password, String phone) {

        MutableLiveData result = new MutableLiveData<Resources<SignUpResponse>>();
        result.postValue(Resources.onLoading());

        apiService.singUp(username, password, phone).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful()) result.postValue(Resources.onSuccess(response.body()));
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
}
