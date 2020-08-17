package com.zoho.mohammadrajabi.socialnetwork.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.adapter.PostAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.model.LikeResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityPostsBinding;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.PostsViewModel;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ViewModelInjector;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PostsActivity extends AppCompatActivity implements PostAdapter.onPostClick {

    private ActivityPostsBinding binding;
    public static final String EXTRA_KEY_POSTS = "posts";
    public static final String EXTRA_KEY_POST_POSITION = "postPosition";
    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    ViewModelInjector viewModelInjector;
    private PostsViewModel viewModel;
    private Disposable disposable;
    private int postPosition;
    private PostAdapter postAdapter;
    private List<Post> posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        viewModel = new ViewModelProvider(this, viewModelInjector).get(PostsViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_posts);
        posts = (List<Post>) getIntent().getSerializableExtra(PostsActivity.EXTRA_KEY_POSTS);
        postPosition = getIntent().getIntExtra(PostsActivity.EXTRA_KEY_POST_POSITION, 0);
        setRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.imageViewPostsActivityBack.setOnClickListener(v -> finish());

    }

    private void setRecyclerView() {
        postAdapter = new PostAdapter(this);
        postAdapter.setPosts(posts);
        binding.recyclerViewPostsActivityPosts.setAdapter(postAdapter);
        binding.recyclerViewPostsActivityPosts.setLayoutManager(linearLayoutManager);
        linearLayoutManager.scrollToPositionWithOffset(postPosition, 0);
    }

    @Override
    public void onLikeClick(int position, String postId) {
        viewModel.likePost(Integer.parseInt(postId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LikeResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
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
                            Snackbar.make(binding.coordinatorPostsActivity, PostsActivity.this.getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onCommentClick(String postId) {
        Intent intent = new Intent(PostsActivity.this, CommentActivity.class);
        intent.putExtra(CommentActivity.EXTRA_KEY_POST_ID, postId);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
