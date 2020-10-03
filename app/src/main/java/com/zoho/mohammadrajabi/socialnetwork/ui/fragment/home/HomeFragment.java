package com.zoho.mohammadrajabi.socialnetwork.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.ui.adapter.PostAdapter;
import com.zoho.mohammadrajabi.socialnetwork.databinding.FragmentHomeBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.comment.CommentActivity;
import com.zoho.mohammadrajabi.socialnetwork.util.Constants;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;


import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment implements PostAdapter.onPostClick {

    private FragmentHomeBinding binding;
    @Inject
    ViewModelInjector viewModelInjector;

    @Inject
    Provider<LinearLayoutManager> linearLayoutManager;

    private HomeViewModel homeViewModel;
    private PostAdapter postAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this, viewModelInjector).get(HomeViewModel.class);
        postAdapter = new PostAdapter(HomeFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = FragmentHomeBinding.inflate(inflater, container, false);
        setUpRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        observe();
        binding.layoutHomeFragmentNoConnection.setOnClickListener(v -> {
            observe();
        });
    }

    private void observe() {

        homeViewModel.getPosts()
                .observe(getViewLifecycleOwner(), postResponseResources -> {

                    switch (postResponseResources.status) {
                        case LOADING:
                            setViewsVisibility(View.GONE, View.GONE, View.VISIBLE);
                            break;

                        case NETWORK_CONNECTIVITY:
                            setViewsVisibility(View.GONE, View.VISIBLE, View.GONE);
                            break;

                        case SUCCESS:
                            setViewsVisibility(View.VISIBLE, View.GONE, View.GONE);
                            postAdapter.setPosts(postResponseResources.data.getPosts());
                            break;
                    }


                });

    }

    private void setViewsVisibility(int recyclerViewVisibility, int noConnectionVisibility, int loadingVisibility) {
        binding.recyclerViewHomeFragmentPosts.setVisibility(recyclerViewVisibility);
        binding.layoutHomeFragmentNoConnection.setVisibility(noConnectionVisibility);
        binding.progressBarHomeFragmentLoading.setVisibility(loadingVisibility);
    }

    private void setUpRecyclerView() {
        binding.recyclerViewHomeFragmentPosts.setLayoutManager(linearLayoutManager.get());
        binding.recyclerViewHomeFragmentPosts.setAdapter(postAdapter);
    }

    @Override
    public void onLikeClick(int position, String postId) {

        int id = Integer.parseInt(postId);
        homeViewModel.likePost(id)
                .observe(getViewLifecycleOwner(), likeResponseResources -> {

                    switch (likeResponseResources.status) {
                        case SUCCESS:
                            if(likeResponseResources.data.isStatus())
                            postAdapter.likedPost(position, Integer.parseInt(likeResponseResources.data.getLikeCount()));
                            break;
                        case NETWORK_CONNECTIVITY:
                            Snackbar.make(binding.coordinatorHomeFragment, getContext().getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    @Override
    public void onCommentClick(String postId) {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_POST_ID, Integer.parseInt(postId));
        startActivity(intent);
    }


}
