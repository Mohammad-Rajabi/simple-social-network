package com.zoho.mohammadrajabi.socialnetwork.ui.activity.posts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.comment.CommentActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.adapter.PostAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityPostsBinding;
import com.zoho.mohammadrajabi.socialnetwork.util.Constants;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;


public class PostsActivity extends AppCompatActivity implements PostAdapter.onPostClick {

    private ActivityPostsBinding binding;

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    ViewModelInjector viewModelInjector;
    private PostsViewModel viewModel;
    private int postPosition;
    private PostAdapter postAdapter;
    private List<Post> posts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        viewModel = new ViewModelProvider(this, viewModelInjector).get(PostsViewModel.class);
        binding = ActivityPostsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        posts = getIntent().getParcelableArrayListExtra(Constants.EXTRA_KEY_POSTS);
        postPosition = getIntent().getIntExtra(Constants.EXTRA_KEY_POST_POSITION, 0);
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
                .observe(this, likeResponseResources -> {
                    switch (likeResponseResources.status) {
                        case NETWORK_CONNECTIVITY:
                            Snackbar.make(binding.coordinatorPostsActivity, PostsActivity.this.getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            break;

                        case SUCCESS:
                            postAdapter.likedPost(position, Integer.parseInt(likeResponseResources.data.getLikeCount()));
                            break;
                    }
                });

    }

    @Override
    public void onCommentClick(String postId) {
        Intent intent = new Intent(PostsActivity.this, CommentActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_POST_ID, postId);
        startActivity(intent);
    }

}
