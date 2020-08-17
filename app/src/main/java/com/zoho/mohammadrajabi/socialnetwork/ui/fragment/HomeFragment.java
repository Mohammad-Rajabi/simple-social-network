package com.zoho.mohammadrajabi.socialnetwork.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.adapter.PostAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.PostResponse;
import com.zoho.mohammadrajabi.socialnetwork.databinding.FragmentHomeBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.CommentActivity;
import com.zoho.mohammadrajabi.socialnetwork.util.EndlessRecyclerOnScrollListener;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.HomeViewModel;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ViewModelInjector;


import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.support.DaggerFragment;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends DaggerFragment implements PostAdapter.onPostClick, EndlessRecyclerOnScrollListener.scrollListener {

    private FragmentHomeBinding binding;
    @Inject
    ViewModelInjector viewModelInjector;

    @Inject
    Provider<LinearLayoutManager> linearLayoutManager;

    private HomeViewModel homeViewModel;
    private CompositeDisposable compositeDisposable;
    private PostAdapter postAdapter;
    private int currentPage = 1;
    private static final int ITEM_PER_PAGE = 5;
    private static final String TAG = "home";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this, viewModelInjector).get(HomeViewModel.class);
        compositeDisposable = new CompositeDisposable();
        postAdapter = new PostAdapter(HomeFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        setUpRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        observe(currentPage);
        binding.layoutHomeFragmentNoConnection.setOnClickListener(v -> {
            observe(currentPage);
        });
    }

    private void observe(int currentPage) {

        if (currentPage == 1) {

            Disposable disposable = homeViewModel.getProgressBarVisibilitySubject().subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(shouldShowProgressBar -> {
                        binding.progressBarHomeFragmentLoading.setVisibility(shouldShowProgressBar ? View.VISIBLE : View.GONE);
                    });
            compositeDisposable.add(disposable);
        }


        homeViewModel.getPosts(currentPage).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<PostResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(PostResponse postResponse) {
                        if (postResponse.isStatus()) {
                            setViewsVisibility(View.VISIBLE, View.GONE);
//                        int itemInsertedIndex = HomeFragment.this.posts.size();
//                        HomeFragment.this.posts.addAll(postResponse.getPosts());
//                        postAdapter.notifyItemInserted(itemInsertedIndex);
                            postAdapter.setPosts(postResponse.getPosts());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException) {
                            if (currentPage == 1) {
                                setViewsVisibility(View.GONE, View.VISIBLE);
                            } else {
                                Snackbar.make(binding.coordinatorHomeFragment, getActivity().getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setViewsVisibility(int recyclerViewVisibility, int noConnectionVisibility) {
        binding.recyclerViewHomeFragmentPosts.setVisibility(recyclerViewVisibility);
        binding.layoutHomeFragmentNoConnection.setVisibility(noConnectionVisibility);
    }

    private void setUpRecyclerView() {
        binding.recyclerViewHomeFragmentPosts.setLayoutManager(linearLayoutManager.get());
        binding.recyclerViewHomeFragmentPosts.addOnScrollListener(new EndlessRecyclerOnScrollListener(HomeFragment.ITEM_PER_PAGE, linearLayoutManager.get(), HomeFragment.this));
        binding.recyclerViewHomeFragmentPosts.setAdapter(postAdapter);
    }

    @Override
    public void onLikeClick(int position, String postId) {
        int id = Integer.parseInt(postId);
        homeViewModel.likePost(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LikeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(LikeResponse likeResponse) {
                        if (likeResponse.isStatus()) {
                            postAdapter.likedPost(position, Integer.parseInt(likeResponse.getLikeCount()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException) {
                            Snackbar.make(binding.coordinatorHomeFragment, getContext().getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onCommentClick(String postId) {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra(CommentActivity.EXTRA_KEY_POST_ID, Integer.parseInt(postId));
        startActivity(intent);
    }

    @Override
    public void onLoadMore(int currentPage) {
        this.currentPage = currentPage;
        observe(this.currentPage);
    }


    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

}
