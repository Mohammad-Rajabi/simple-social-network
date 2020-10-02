package com.zoho.mohammadrajabi.socialnetwork.ui.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.local.SessionManager;
import com.zoho.mohammadrajabi.socialnetwork.data.TokenContainer;
import com.zoho.mohammadrajabi.socialnetwork.data.local.UserInfo;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityLoginBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.MainActivity;
import com.zoho.mohammadrajabi.socialnetwork.ui.activity.signup.SignUpActivity;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class LoginActivity extends AppCompatActivity {

    @Inject
    SessionManager sessionManager;
    @Inject
    ViewModelInjector viewModelInjector;
    @Inject
    UserInfo userInfo;
    private LoginViewModel viewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, viewModelInjector).get(LoginViewModel.class);
        binding.editTextLoginActivityPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.checkboxLoginActivityPasswordToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.editTextLoginActivityPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    binding.editTextLoginActivityPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        binding.textViewLoginActivitySignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });

        binding.btnLoginActivityLogin.setOnClickListener(v -> {

            Tools.hideKeyboard(LoginActivity.this, LoginActivity.this.getCurrentFocus());

            if (checkInput()) {

                viewModel.login(binding.editTextLoginActivityUsername.getText().toString().trim(),
                        binding.editTextLoginActivityPassword.getText().toString().trim()).observe(LoginActivity.this, resources -> {

                    switch (resources.status) {

                        case NETWORK_CONNECTIVITY:
                            Snackbar.make(binding.coordinatorLoginActivity, getResources().getString(R.string.connectivityToInternet), Snackbar.LENGTH_SHORT).show();
                            setViewsVisibility(View.GONE, View.VISIBLE);
                            break;
                        case LOADING:
                            setViewsVisibility(View.VISIBLE, View.GONE);
                            break;
                        case ERROR:
                            setViewsVisibility(View.GONE, View.VISIBLE);
                            if (!resources.message.isEmpty()) {
                                Snackbar.make(binding.coordinatorLoginActivity, resources.message, Snackbar.LENGTH_SHORT).show();
                            } else
                                Snackbar.make(binding.coordinatorLoginActivity, getResources().getString(R.string.unknownError), Snackbar.LENGTH_SHORT).show();
                            break;
                        case SUCCESS:
                            setViewsVisibility(View.GONE, View.VISIBLE);
                            if (resources.data.isStatus()) {
                                sessionManager.setLogin(true);
                                userInfo.setUserId(String.valueOf(resources.data.getUser().getId()));
                                TokenContainer.updateUserId(String.valueOf(resources.data.getUser().getId()));
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Snackbar.make(binding.coordinatorLoginActivity, resources.data.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }

                            break;
                    }

                });

            }

        });

    }

    private void setViewsVisibility(int progressVisibility, int loginButtonVisibility) {
        binding.progressBarLoginActivityLoading.setVisibility(progressVisibility);
        binding.btnLoginActivityLogin.setVisibility(loginButtonVisibility);
    }

    private boolean checkInput() {
        if (binding.editTextLoginActivityUsername.getText().toString().trim().isEmpty()) {
            binding.editTextLoginActivityUsername.setError("نام کاربری وارد نشده است!");
            return false;
        }
        if (binding.editTextLoginActivityPassword.getText().toString().trim().isEmpty()) {
            binding.editTextLoginActivityPassword.setError("کلمه عبور وارد نشده است!");
            return false;
        }
        return true;
    }
}
