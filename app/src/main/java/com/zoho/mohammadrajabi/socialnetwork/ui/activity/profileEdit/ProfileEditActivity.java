package com.zoho.mohammadrajabi.socialnetwork.ui.activity.profileEdit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityProfileEditBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.EditingContinueDialog;
import com.zoho.mohammadrajabi.socialnetwork.util.Constants;
import com.zoho.mohammadrajabi.socialnetwork.util.ImageUtil;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;
    @Inject
    ViewModelInjector viewModelInjector;

    private ProfileEditViewModel viewModel;
    private User user;
    private boolean verifiedIdentityName = true;
    private File file;
    private MultipartBody.Part multipartBody;
    private boolean fileUploaded;
    private EditingContinueDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AndroidInjection.inject(this);
        viewModel = new ViewModelProvider(this, viewModelInjector).get(ProfileEditViewModel.class);
        user = getIntent().getParcelableExtra(Constants.EXTRA_KEY_USER);

        binding.editTextProfileEditActivityPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Glide.with(this)
                .load(user.getProfileImage())
                .placeholder(getResources().getDrawable(R.drawable.shape_placeholder))
                .error(getResources().getDrawable(R.drawable.ic_user))
                .fallback(getResources().getDrawable(R.drawable.ic_user))
                .into(binding.imageViewProfileEditActivityUserProfileImage);

        binding.editTextProfileEditActivityUsername.setText(user.getUsername());
        binding.editTextProfileEditActivityPassword.setText(user.getPassword());
        binding.editTextProfileEditActivityPhone.setText(user.getPhone());


        binding.checkboxProfileEditActivityPasswordToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.editTextProfileEditActivityPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    binding.editTextProfileEditActivityPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        binding.editTextProfileEditActivityUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editTextProfileEditActivityUsername.setBackground(getResources().getDrawable(R.drawable.shape_edit_text));
                binding.textViewProfileEditActivityCheckUsername.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

                viewModel.checkUsername(s.toString()).observe(ProfileEditActivity.this, resources -> {

                    switch (resources.status) {

                        case NETWORK_CONNECTIVITY:
                            Snackbar.make(binding.coordinatorProfileEditActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            break;

                        case SUCCESS:
                            binding.textViewProfileEditActivityCheckUsername.setVisibility(View.VISIBLE);
                            if (!resources.data.getStatus()) {
                                binding.textViewProfileEditActivityCheckUsername.setText("این شناسه قابل انتخاب هست");
                                binding.textViewProfileEditActivityCheckUsername.setTextColor(getResources().getColor(R.color.green));
                                verifiedIdentityName = true;

                            } else if (resources.data.getStatus()) {
                                binding.textViewProfileEditActivityCheckUsername.setText("این شناسه قابل انتخاب نیست!");
                                binding.textViewProfileEditActivityCheckUsername.setTextColor(getResources().getColor(R.color.red));
                                verifiedIdentityName = false;
                            }
                            break;

                        case ERROR:
                            Snackbar.make(binding.coordinatorProfileEditActivity, getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                            break;
                    }
                });
            }
        });

        binding.btnProfileEditActivityEdit.setOnClickListener(v -> {

            if (checkInputs()) {

                if (verifiedIdentityName) {

                    if (fileUploaded) {

                        viewModel.editProfile(Integer.parseInt(TokenContainer.getUserId()),
                                binding.editTextProfileEditActivityUsername.getText().toString(),
                                binding.editTextProfileEditActivityPassword.getText().toString(),
                                binding.editTextProfileEditActivityPhone.getText().toString(),
                                multipartBody).observe(ProfileEditActivity.this, resources -> {

                            switch (resources.status) {

                                case NETWORK_CONNECTIVITY:
                                    Snackbar.make(binding.coordinatorProfileEditActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                                    setViewVisibility(View.VISIBLE, View.GONE);
                                    break;

                                case LOADING:
                                    setViewVisibility(View.GONE, View.VISIBLE);
                                    break;

                                case SUCCESS:
                                    setViewVisibility(View.VISIBLE, View.GONE);
                                    if (resources.data.getStatus()) {
                                        finish();
                                    }
                                    break;

                                case ERROR:
                                    setViewVisibility(View.VISIBLE, View.GONE);
                                    Snackbar.make(binding.coordinatorProfileEditActivity, getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                                    break;

                            }
                        });

                    } else {
                        Snackbar.make(binding.coordinatorProfileEditActivity, "عکسی انتخاب نشده است", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    binding.editTextProfileEditActivityUsername.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_error));
                    binding.editTextProfileEditActivityUsername.setError("نام کاربری الزامی است و باید معتبر باشد");
                }

            }
        });

        binding.textViewProfileEditActivityImageEdit.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setOutputCompressQuality(50)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .start(this);
        });

        binding.btnBack.setOnClickListener(v -> {
            if (dialog == null) {
                dialog = new EditingContinueDialog();
            }
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), null);
        });
    }

    private void setViewVisibility(int btnProfileVisibility, int progressBarProfileVisibility) {
        binding.btnProfileEditActivityEdit.setVisibility(btnProfileVisibility);
        binding.progressBarProfileEditActivityLoading.setVisibility(progressBarProfileVisibility);
    }

    private boolean checkInputs() {
        if (binding.editTextProfileEditActivityUsername.getText().toString().trim().isEmpty()) {
            binding.editTextProfileEditActivityUsername.setError("نام کاربری الزامی است");
            return false;
        }
        if (binding.editTextProfileEditActivityPassword.getText().toString().trim().isEmpty()) {
            binding.editTextProfileEditActivityPassword.setError("کلمه عبور الزامی است!");
            return false;
        }
        if (!binding.editTextProfileEditActivityPhone.getText().toString().matches(Tools.regexp)) {
            binding.editTextProfileEditActivityPhone.setError("شماره همراه الزامی است و باید معتبر باشد");
            return false;
        }
        return true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                String imagePath = result.getUri().getPath();
                String imageName = user.getUsername() + "_" + new Date().getTime() + ".jpg";
                file = new File(imagePath);
                file = ImageUtil.saveBitmapToFile(file);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                multipartBody = MultipartBody.Part.createFormData("photo", imageName, requestFile);
                fileUploaded = true;
                Glide.with(this)
                        .load(file)
                        .into(binding.imageViewProfileEditActivityUserProfileImage);

            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Snackbar.make(binding.coordinatorProfileEditActivity, "خطا در بارگذاری تصویر...", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        if (dialog == null) {
            dialog = new EditingContinueDialog();
        }
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), null);
    }
}
