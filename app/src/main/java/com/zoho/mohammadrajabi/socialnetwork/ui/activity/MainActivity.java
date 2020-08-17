package com.zoho.mohammadrajabi.socialnetwork.ui.activity;


import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ActivityMainBinding;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.HomeFragment;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.ProfileFragment;
import com.zoho.mohammadrajabi.socialnetwork.ui.fragment.SearchFragment;

import java.util.Stack;


import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private Stack<Integer> bottomNavigationStack = new Stack<>();
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;
    private ActivityMainBinding binding;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        homeFragment = new HomeFragment();
        fragmentTransaction(homeFragment);
        binding.navigationViewMainActivity.getMenu().getItem(0).setChecked(true);
        bottomNavigationStack.add(binding.navigationViewMainActivity.getMenu().getItem(0).getItemId());

    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.navigationViewMainActivity.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    if (bottomNavigationStack.search(item.getItemId()) == -1) {
                        bottomNavigationStack.add(item.getItemId());
                    }
                    if (homeFragment == null) homeFragment = new HomeFragment();
                    fragmentTransaction(homeFragment);
                    break;
                case R.id.search:

                    if (bottomNavigationStack.search(item.getItemId()) == -1) {
                        bottomNavigationStack.add(item.getItemId());
                    }
                    if (searchFragment == null) {
                        searchFragment = new SearchFragment();
                    }
                    fragmentTransaction(searchFragment);
                    break;
                case R.id.profile:
                    if (bottomNavigationStack.search(item.getItemId()) == -1) {
                        bottomNavigationStack.add(item.getItemId());
                    }
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                    }
                    fragmentTransaction(profileFragment);
                    break;

            }
            return true;
        });
    }

    private void popStack() {

        bottomNavigationStack.pop();

        switch (bottomNavigationStack.peek()) {

            case R.id.home:
                binding.navigationViewMainActivity.getMenu().getItem(0).setChecked(true);
                fragmentTransaction(homeFragment);
                break;

            case R.id.search:
                binding.navigationViewMainActivity.getMenu().getItem(1).setChecked(true);
                fragmentTransaction(searchFragment);
                break;

            case R.id.profile:
                binding.navigationViewMainActivity.getMenu().getItem(2).setChecked(true);
                fragmentTransaction(profileFragment);
                break;
        }
    }

    private void fragmentTransaction(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_mainActivity_fragmentContainer, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationStack.size() > 1) {
            popStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                //warning never don't use super.onBackPressed()
                finish();
            } else {
                doubleBackToExitPressedOnce = true;
                Toast.makeText(MainActivity.this, "برای خروج دوباره کلیک کند", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

}