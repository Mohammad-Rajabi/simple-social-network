package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.ProfileRepository;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Call;

public class ProfileViewModel extends ViewModel {

    private ProfileRepository profileRepository;

    @Inject
    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Single<UserProfileResponse> getProfile(String userId) {
        return profileRepository.getProfile(userId);
    }

}
