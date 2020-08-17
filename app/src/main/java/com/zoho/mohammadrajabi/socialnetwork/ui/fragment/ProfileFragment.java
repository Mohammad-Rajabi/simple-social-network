package com.zoho.mohammadrajabi.socialnetwork.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.adapter.PostImageAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.local.SessionManager;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.data.model.UserProfileResponse;
import com.zoho.mohammadrajabi.socialnetwork.databinding.FragmentProfileBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.SendPostActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.PostsActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.ProfileEditActivity;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ProfileViewModel;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ViewModelInjector;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private static final String TAG = "profile";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(this, viewModelInjector).get(ProfileViewModel.class);
        compositeDisposable = new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

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

        Log.i(TAG, "onStart: ");
        observe();

        binding.imageViewProfileFragmentNav.setOnClickListener(v -> binding.drawerProfileFragment.openDrawer(Gravity.LEFT));

        setupNavigationView();

        binding.imageViewProfileFragmentEdit.setOnClickListener(v -> {

            if (userProfileResponse.getUser().getUsername() != null) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                intent.putExtra(ProfileEditActivity.EXTRA_KEY_USER, userProfileResponse.getUser());
                startActivity(intent);
            }

        });

        binding.btnProfileFragmentAddPost.setOnClickListener(v -> {

            if (userProfileResponse.getUser().getUsername() != null) {
                Intent intent = new Intent(getContext(), SendPostActivity.class);
                intent.putExtra(SendPostActivity.EXTRA_KEY_USERNAME, userProfileResponse.getUser().getUsername());
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserProfileResponse userProfileResponse) {

                        if (userProfileResponse.isStatus()) {

                            ProfileFragment.this.userProfileResponse = userProfileResponse;

                            binding.setUserProfile(userProfileResponse);

                            if (userProfileResponse.getPosts().isEmpty() || userProfileResponse.getPosts() == null) {
                                setViewsVisibility(View.GONE, View.VISIBLE);
                            } else {
                                setViewsVisibility(View.VISIBLE, View.GONE);
                                setRecyclerView(userProfileResponse.getPosts());
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException) {
                            Snackbar.make(binding.coordinatorProfileFragment, getContext().getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                        } else
                            Snackbar.make(binding.coordinatorProfileFragment, getContext().getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void setViewsVisibility(int recyclerViewVisibility, int emptyStateVisibility) {
        binding.layoutProfileFragmentEmptyState.setVisibility(emptyStateVisibility);
        binding.recyclerViewProfileFragmentPosts.setVisibility(recyclerViewVisibility);
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }


    @Override
    public void onPostClick(int position) {
        Intent intent = new Intent(getContext(), PostsActivity.class);
        intent.putExtra(PostsActivity.EXTRA_KEY_POST_POSITION, position);
        intent.putExtra(PostsActivity.EXTRA_KEY_POSTS, (Serializable) userProfileResponse.getPosts());
        startActivity(intent);
    }

}
