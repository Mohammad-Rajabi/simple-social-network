package com.zoho.mohammadrajabi.socialnetwork.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.adapter.CommentAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Comment;
import com.zoho.mohammadrajabi.socialnetwork.data.model.CommentsResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendCommentResponse;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityCommentBinding;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.CommentViewModel;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ViewModelInjector;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentActivity extends AppCompatActivity {

    private CommentViewModel viewModel;

    @Inject
    ViewModelInjector viewModelInjector;
    @Inject
    LinearLayoutManager verticalLinearLayoutManager;
    private ActivityCommentBinding binding;
    private CommentAdapter commentAdapter;
    private CompositeDisposable compositeDisposable;
    private int postId;
    public static final String EXTRA_KEY_POST_ID = "postId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        postId = getIntent().getIntExtra(CommentActivity.EXTRA_KEY_POST_ID, 0);
        viewModel = new ViewModelProvider(this, viewModelInjector).get(CommentViewModel.class);
        compositeDisposable = new CompositeDisposable();


    }

    @Override
    protected void onStart() {
        super.onStart();

        Disposable disposable = viewModel.getProgressBarVisibilitySubject().subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shouldShowProgressBar -> binding.progressBarCommentActivityLoading.setVisibility(shouldShowProgressBar ? View.VISIBLE : View.GONE));
        compositeDisposable.add(disposable);

        getComments();

        binding.imageViewCommentActivitySubmit.setOnClickListener(v -> {

            Tools.hideKeyboard(CommentActivity.this, CommentActivity.this.getCurrentFocus());

            viewModel.sendComment(binding.editTextCommentActivity.getText().toString().trim(), postId, TokenContainer.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<SendCommentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onSuccess(SendCommentResponse sendCommentResponse) {
                            binding.editTextCommentActivity.setText("");
                            setViewsVisibility(View.VISIBLE, View.GONE);
                            setUpRecyclerView(sendCommentResponse.getComments());
                        }


                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof ConnectivityException) {
                                Snackbar.make(binding.coordinatorCommentActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

        binding.imageViewCommentActivityBack.setOnClickListener(v -> finish());

    }

    private void getComments() {

        viewModel.getComments(postId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CommentsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(CommentsResponse commentsResponse) {
                        binding.layoutCommentActivityNoConnection.setVisibility(View.GONE);
                        if (commentsResponse.getComments().isEmpty() || commentsResponse.getComments() == null) {
                            setViewsVisibility(View.GONE, View.VISIBLE);
                        } else {
                            setViewsVisibility(View.VISIBLE, View.GONE);
                            setUpRecyclerView(commentsResponse.getComments());
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ConnectivityException) {
                            if (commentAdapter == null) {
                                binding.layoutCommentActivityNoConnection.setVisibility(View.VISIBLE);
                            } else {
                                Snackbar.make(binding.coordinatorCommentActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setViewsVisibility(int recyclerViewVisibility, int commentEmptyStateVisibility) {
        binding.recyclerViewCommentActivityComments.setVisibility(recyclerViewVisibility);
        binding.layoutCommentActivityEmptyState.setVisibility(commentEmptyStateVisibility);
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

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}
