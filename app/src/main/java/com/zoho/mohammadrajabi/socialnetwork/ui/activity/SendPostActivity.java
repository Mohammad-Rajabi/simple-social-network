package com.zoho.mohammadrajabi.socialnetwork.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.ConnectivityException;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SendPostResponse;
import com.zoho.mohammadrajabi.socialnetwork.data.model.SuccessResponse;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityAddPostBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.ExitDialog;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.ProfileFragment;
import com.zoho.mohammadrajabi.socialnetwork.util.ImageUtil;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.SendPostViewModel;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ViewModelInjector;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SendPostActivity extends AppCompatActivity {

    private ActivityAddPostBinding binding;
    @Inject
    ViewModelInjector viewModelInjector;
    private SendPostViewModel viewModel;
    private Disposable disposable;
    private MultipartBody.Part multipartBody;
    private boolean fileUploaded;
    private File file;
    private String username;
    public static final String EXTRA_KEY_USERNAME = "username";
    private ExitDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        viewModel = new ViewModelProvider(this, viewModelInjector).get(SendPostViewModel.class);
        username = getIntent().getStringExtra(SendPostActivity.EXTRA_KEY_USERNAME);
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.layoutAddPostActivityImageUpload.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setOutputCompressQuality(50)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .start(this);
        });

        binding.btnCreatePost.setVisibility(fileUploaded ? View.VISIBLE : View.GONE);

        binding.btnCreatePost.setOnClickListener(v -> {

            viewModel.sendPost(Integer.parseInt(TokenContainer.getUserId()), multipartBody, binding.editTextAddPostActivityPostTitle.getText().toString().trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<SendPostResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onSuccess(SendPostResponse sendPostResponse) {
                            if(sendPostResponse.isStatus()) {
                                binding.editTextAddPostActivityPostTitle.setText("");
                                binding.imageViewAddPostActivityPostImage.setImageResource(android.R.color.transparent);
                                finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof ConnectivityException) {
                                Snackbar.make(binding.coordinatorAddPostActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

        binding.imageViewAddPostActivityBack.setOnClickListener(v -> {
            if (dialog == null) {
                dialog = new ExitDialog();
            }
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), null);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                String imagePath = result.getUri().getPath();
                String imageName = username + "_" + new Date().getTime() + ".jpg";
                file = new File(imagePath);
                file = ImageUtil.saveBitmapToFile(file);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multipartBody = MultipartBody.Part.createFormData("photo", imageName, requestFile);

                fileUploaded = true;

                Glide.with(this)
                        .load(file)
                        .into(binding.imageViewAddPostActivityPostImage);

            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Snackbar.make(binding.coordinatorAddPostActivity, "خطا در بارگذاری تصویر...", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void onBackPressed() {
        if (dialog == null) {
            dialog = new ExitDialog();
        }
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), null);
    }
}
