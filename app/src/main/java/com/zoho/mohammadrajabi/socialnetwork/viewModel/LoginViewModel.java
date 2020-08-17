package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignInResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.LoginRepository;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private LoginRepository loginRepository;

    @Inject
    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public MutableLiveData<Resources<SignInResponse>> login(String username, String password) {
        return loginRepository.login(username, password);
    }
}
