package com.zoho.mohammadrajabi.socialnetwork.ui.fragment.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.local.UserInfo;
import com.zoho.mohammadrajabi.socialnetwork.ui.adapter.PostImageAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.local.SessionManager;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.databinding.FragmentProfileBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.sendPost.SendPostActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.posts.PostsActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.profileEdit.ProfileEditActivity;
import com.zoho.mohammadrajabi.socialnetwork.util.Constants;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

public class ProfileFragment extends DaggerFragment implements PostImageAdapter.onItemClick {

    private FragmentProfileBinding binding;
    @Inject
    ProfileViewModel profileViewModel;
    @Inject
    ViewModelInjector viewModelInjector;
    private CompositeDisposable compositeDisposable;
    private PostImageAdapter postImageAdapter;
    private UserProfileResponse userProfileResponse;

    @Inject
    SessionManager sessionManager;
    @Inject
    UserInfo userInfo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this, viewModelInjector).get(ProfileViewModel.class);
        compositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void setRecyclerView(List<Post> posts) {
        if (postImageAdapter == null) {
            postImageAdapter = new PostImageAdapter(this);
        }
        binding.recyclerViewProfileFragmentPosts.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
        binding.recyclerViewProfileFragmentPosts.setAdapter(postImageAdapter);
        postImageAdapter.setPosts(posts);
    }

    @Override
    public void onStart() {
        super.onStart();

        observe();

        binding.imageViewProfileFragmentNav.setOnClickListener(v -> binding.drawerProfileFragment.openDrawer(Gravity.LEFT));

        setupNavigationView();

        binding.imageViewProfileFragmentEdit.setOnClickListener(v -> {

            if (userProfileResponse.getUser().getUsername() != null) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_USER, userProfileResponse.getUser());
                startActivity(intent);
            }

        });

        binding.btnProfileFragmentAddPost.setOnClickListener(v -> {

            if (userProfileResponse.getUser().getUsername() != null) {
                Intent intent = new Intent(getContext(), SendPostActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_USERNAME, userProfileResponse.getUser().getUsername());
                startActivity(intent);
            }

        });


    }

    private void setupNavigationView() {
        binding.navigationViewProfileFragment.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.exit:
                    sessionManager.setLogin(false);
                    TokenContainer.updateUserId("");
                    getActivity().finish();
                    System.exit(0);
            }
            return true;
        });

    }

    public void observe() {

        profileViewModel.getProfile(TokenContainer.getUserId())
                .observe(getViewLifecycleOwner(), userProfileResponseResources -> {
                    switch (userProfileResponseResources.status) {
                        case NETWORK_CONNECTIVITY:
                            Snackbar.make(binding.coordinatorProfileFragment, getContext().getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            break;
                        case ERROR:
                            Snackbar.make(binding.coordinatorProfileFragment, getContext().getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                            break;

                        case SUCCESS:
                            ProfileFragment.this.userProfileResponse = userProfileResponseResources.data;

                            binding.textViewProfileFragmentUsername.setText(userProfileResponse.getUser().getUsername());
                            userInfo.setUserProfileImage(userProfileResponseResources.data.getUser().getProfileImage());
                            Glide.with(getContext())
                                    .load(userProfileResponse.getUser().getProfileImage())
                                    .placeholder(getContext().getResources().getDrawable(R.drawable.ic_baseline_image_24))
                                    .error(getContext().getResources().getDrawable(R.drawable.ic_photo))
                                    .into(binding.imageViewProfileFragmentProfileImage);

                            if (userProfileResponse.getPosts().isEmpty() || userProfileResponse.getPosts() == null) {
                                setViewsVisibility(View.GONE, View.VISIBLE);
                            } else {
                                setViewsVisibility(View.VISIBLE, View.GONE);
                                setRecyclerView(userProfileResponse.getPosts());
                            }
                            break;
                    }
                });

    }

    private void setViewsVisibility(int recyclerViewVisibility, int emptyStateVisibility) {
        binding.layoutProfileFragmentEmptyState.setVisibility(emptyStateVisibility);
        binding.recyclerViewProfileFragmentPosts.setVisibility(recyclerViewVisibility);
    }


    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), PostsActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_POST_POSITION, position);
        intent.putParcelableArrayListExtra(Constants.EXTRA_KEY_POSTS, (ArrayList<? extends Parcelable>) userProfileResponse.getPosts());
        startActivity(intent);
    }

}
