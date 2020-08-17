package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SuccessResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UpdateUserResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.checkUsernameResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.ProfileEditRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.MultipartBody;
import retrofit2.Call;

public class ProfileEditViewModel extends ViewModel {


    private ProfileEditRepository profileEditRepository;
    private BehaviorSubject<Boolean> progressDialogVisibility = BehaviorSubject.create();

    @Inject
    public ProfileEditViewModel(ProfileEditRepository profileEditRepository) {
        this.profileEditRepository = profileEditRepository;
    }

    public MutableLiveData<Resources<checkUsernameResponse>> checkUsername(String username) {
        return profileEditRepository.checkUsername(username);
    }

    public MutableLiveData<Resources<UpdateUserResponse>> editProfile(int userId, String username, String password, String phone, MultipartBody.Part multipartBody) {
//        progressDialogVisibility.onNext(true);
//        profileEditRepository.editProfile(userId, username, password, phone, multipartBody).
//        return profileEditRepository.editProfile(userId, username, password, phone, multipartBody)
//                .doOnEvent((successResponse, throwable) -> progressDialogVisibility.onNext(false));
        return profileEditRepository.editProfile(userId,username,password, phone, multipartBody);
    }


    public BehaviorSubject<Boolean> getProgressDialogVisibility() {
        return progressDialogVisibility;
    }
}
