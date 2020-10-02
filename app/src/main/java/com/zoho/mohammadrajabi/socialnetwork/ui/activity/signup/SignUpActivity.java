package com.zoho.mohammadrajabi.socialnetwork.ui.activity.signup;

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

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.local.SessionManager;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.local.UserInfo;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivitySignupBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.MainActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.login.LoginActivity;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SignUpActivity extends AppCompatActivity {

    private SignUpViewModel signUpViewModel;
    @Inject
    SessionManager sessionManager;
    @Inject
    UserInfo userInfo;
    @Inject
    ViewModelInjector viewModelInjector;
    private ActivitySignupBinding binding;
    private boolean verifiedIdentityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        signUpViewModel = new ViewModelProvider(this, viewModelInjector).get(SignUpViewModel.class);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editTextSignUpActivityPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.checkboxSignUpActivityPasswordToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.editTextSignUpActivityPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    binding.editTextSignUpActivityPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        binding.editTextSignUpActivityUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.editTextSignUpActivityUsername.setBackground(getResources().getDrawable(R.drawable.shape_edit_text));
                binding.textViewSignUpActivityCheckIdentity.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

                signUpViewModel.checkUsername(s.toString()).observe(SignUpActivity.this, resources -> {

                    switch (resources.status) {

                        case NETWORK_CONNECTIVITY:

                            Snackbar.make(binding.coordinatorSignUpActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            break;

                        case SUCCESS:

                            binding.textViewSignUpActivityCheckIdentity.setVisibility(View.VISIBLE);

                            if (!resources.data.getStatus()) {
                                binding.textViewSignUpActivityCheckIdentity.setText("این شناسه قابل انتخاب هست");
                                binding.textViewSignUpActivityCheckIdentity.setTextColor(getResources().getColor(R.color.green));
                                verifiedIdentityName = true;

                            } else if (resources.data.getStatus()) {
                                binding.textViewSignUpActivityCheckIdentity.setText("این شناسه قابل انتخاب نیست!");
                                binding.textViewSignUpActivityCheckIdentity.setTextColor(getResources().getColor(R.color.red));
                                verifiedIdentityName = false;
                            }
                            break;

                        case ERROR:

                            Snackbar.make(binding.coordinatorSignUpActivity, getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                            break;

                    }
                });
            }
        });

        binding.textViewSignUpActivityLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnSignUpActivitySignUp.setOnClickListener(v -> {

            Tools.hideKeyboard(SignUpActivity.this, SignUpActivity.this.getCurrentFocus());

            if (checkInputs()) {

                if (verifiedIdentityName) {

                    binding.textViewSignUpActivityLogin.setEnabled(false);

                    signUpViewModel.signUp(binding.editTextSignUpActivityUsername.getText().toString(),
                            binding.editTextSignUpActivityPassword.getText().toString(),
                            binding.editTextSignUpActivityPhone.getText().toString()).observe(SignUpActivity.this, resources -> {

                        switch (resources.status) {

                            case NETWORK_CONNECTIVITY:
                                Snackbar.make(binding.coordinatorSignUpActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                                setViewsVisibility(View.GONE, View.VISIBLE);
                                break;
                            case LOADING:
                                setViewsVisibility(View.VISIBLE, View.GONE);
                                break;
                            case ERROR:
                                setViewsVisibility(View.GONE, View.VISIBLE);
                                Snackbar.make(binding.coordinatorSignUpActivity, getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                                break;
                            case SUCCESS:
                                setViewsVisibility(View.GONE, View.VISIBLE);
                                if (resources.data.getStatus()) {
                                    userInfo.setUserId(String.valueOf(resources.data.getUser().getId()));
                                    TokenContainer.updateUserId(String.valueOf(resources.data.getUser().getId()));
                                    sessionManager.setLogin(true);
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    finish();
                                }
                                break;
                        }

                    });
                    binding.textViewSignUpActivityLogin.setEnabled(true);


                } else {
                    binding.editTextSignUpActivityUsername.setBackground(getResources().getDrawable(R.drawable.shape_edit_text_error));
                }

            }

        });
    }

    private void setViewsVisibility(int progressVisibility, int loginButtonVisibility) {
        binding.progressBarSignUpActivityWaiting.setVisibility(progressVisibility);
        binding.btnSignUpActivitySignUp.setVisibility(loginButtonVisibility);
    }

    private boolean checkInputs() {
        if (binding.editTextSignUpActivityUsername.getText().toString().trim().isEmpty()) {
            binding.editTextSignUpActivityUsername.setError("نام کاربری الزامی است!");
            return false;
        }
        if (binding.editTextSignUpActivityPassword.getText().toString().trim().isEmpty()) {
            binding.editTextSignUpActivityPassword.setError("کلمه عبور الزامی است!");
            return false;
        }
        if (!binding.editTextSignUpActivityPhone.getText().toString().matches(Tools.regexp)) {
            binding.editTextSignUpActivityPhone.setError("شماره همراه باید معتبر باشد و الزامی است");
            return false;
        } else return true;

    }
}

