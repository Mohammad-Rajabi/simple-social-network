package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SignUpResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.SignUpRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class SignUpViewModel extends ViewModel {

    private SignUpRepository signUpRepository;

    @Inject
    public SignUpViewModel(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    public MutableLiveData<Resources<checkUsernameResponse>> checkUsername(String username) {
        return signUpRepository.checkUsername(username);
    }

    public MutableLiveData<Resources<SignUpResponse>> signUp(String username, String password, String phone) {
        return signUpRepository.signUp(username, password, phone);
    }
}
