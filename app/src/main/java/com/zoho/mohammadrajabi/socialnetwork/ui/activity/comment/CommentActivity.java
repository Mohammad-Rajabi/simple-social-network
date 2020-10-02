package com.zoho.mohammadrajabi.socialnetwork.ui.activity.comment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.local.UserInfo;
import com.zoho.mohammadrajabi.socialnetwork.ui.adapter.CommentAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Comment;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityCommentBinding;
import com.zoho.mohammadrajabi.socialnetwork.util.Constants;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class CommentActivity extends AppCompatActivity {

    private CommentViewModel viewModel;

    @Inject
    ViewModelInjector viewModelInjector;
    @Inject
    LinearLayoutManager verticalLinearLayoutManager;
    @Inject
    UserInfo userInfo;
    private ActivityCommentBinding binding;
    private CommentAdapter commentAdapter;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postId = getIntent().getIntExtra(Constants.EXTRA_KEY_POST_ID, 0);
        viewModel = new ViewModelProvider(this, viewModelInjector).get(CommentViewModel.class);


    }

    @Override
    protected void onStart() {
        super.onStart();

        Glide.with(this).load(userInfo.getImageUrl())
                .placeholder(getResources().getDrawable(R.drawable.shape_placeholder))
                .error(getResources().getDrawable(R.drawable.ic_user))
                .fallback(getResources().getDrawable(R.drawable.ic_user))
                .into(binding.imageViewCommentActivityUserProfileImage);

        getComments();

        binding.imageViewCommentActivitySubmit.setOnClickListener(v -> {

            Tools.hideKeyboard(CommentActivity.this, CommentActivity.this.getCurrentFocus());

            viewModel.sendComment(binding.editTextCommentActivity.getText().toString().trim(), postId, TokenContainer.getUserId())
                    .observe(this, sendCommentResponseResources -> {
                        switch (sendCommentResponseResources.status) {
                            case NETWORK_CONNECTIVITY:
                                Snackbar.make(binding.coordinatorCommentActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                                break;

                            case SUCCESS:
                                binding.editTextCommentActivity.setText("");
                                setViewsVisibility(View.VISIBLE, View.GONE, View.GONE);
                                setUpRecyclerView(sendCommentResponseResources.data.getComments());
                                break;
                        }
                    });
        });

        binding.imageViewCommentActivityBack.setOnClickListener(v -> finish());

    }

    private void getComments() {

        viewModel.getComments(postId).observe(this, resources -> {

            switch (resources.status) {

                case NETWORK_CONNECTIVITY:
                    if (commentAdapter == null) {
                        binding.layoutCommentActivityNoConnection.setVisibility(View.VISIBLE);
                    } else {
                        Snackbar.make(binding.coordinatorCommentActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                    }
                    break;

                case LOADING:
                    setViewsVisibility(View.GONE, View.GONE, View.VISIBLE);
                    break;

                case SUCCESS:
                    binding.layoutCommentActivityNoConnection.setVisibility(View.GONE);
                    if (resources.data.getComments().isEmpty() || resources.data.getComments() == null) {
                        setViewsVisibility(View.GONE, View.VISIBLE, View.GONE);
                    } else {
                        setViewsVisibility(View.VISIBLE, View.GONE, View.GONE);
                        setUpRecyclerView(resources.data.getComments());
                    }
                    break;
            }

        });

    }

    private void setViewsVisibility(int recyclerViewVisibility, int commentEmptyStateVisibility, int loadingVisibility) {
        binding.recyclerViewCommentActivityComments.setVisibility(recyclerViewVisibility);
        binding.layoutCommentActivityEmptyState.setVisibility(commentEmptyStateVisibility);
        binding.progressBarCommentActivityLoading.setVisibility(loadingVisibility);
    }

    private void setUpRecyclerView(List<Comment> comments) {
        if (commentAdapter == null) {
            commentAdapter = new CommentAdapter(comments);
            binding.recyclerViewCommentActivityComments.setLayoutManager(verticalLinearLayoutManager);
            binding.recyclerViewCommentActivityComments.setAdapter(commentAdapter);
        } else {
            commentAdapter.setComments(comments);
        }

    }

}
