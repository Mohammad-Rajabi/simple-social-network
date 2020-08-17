package com.zoho.mohammadrajabi.socialnetwork.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.adapter.SearchAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.databinding.FragmentSearchBinding;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.WifiProxyChanger;
import com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.exceptions.ApiNotSupportedException;
import com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.exceptions.NullWifiConfigurationException;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.SearchViewModel;
import com.zoho.mohammadrajabi.socialnetwork.viewModel.ViewModelInjector;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends DaggerFragment {

    private static final String TAG = "search";
    @Inject
    ViewModelInjector viewModelInjector;

    @Inject
    SearchViewModel searchViewModel;

    @Inject
    LinearLayoutManager linearLayoutManager;

    private FragmentSearchBinding binding;
    private CompositeDisposable compositeDisposable;
    private SearchAdapter searchAdapter;
    private FirebaseRemoteConfig firebaseRemoteConfig;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this, viewModelInjector).get(SearchViewModel.class);

//        changeProxySettings("52.179.231.206", 80);
//        List<Proxy> proxies = new ProxySelector().select(uri);
//        if (proxies.size() == 1 && proxies.get(0).type().equals(Proxy.Type.DIRECT)) {
//            return "no proxy";
//        }

        //Remote config
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(settings);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        compositeDisposable = new CompositeDisposable();
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.layoutSearchFragmentToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: ");
        firebaseRemoteConfig.fetch(1).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                firebaseRemoteConfig.activate();
            }
//            String str = firebaseRemoteConfig.getString("text_color");
            boolean bool= firebaseRemoteConfig.getBoolean("btn_visibility");
            if(firebaseRemoteConfig.getBoolean("btn_visibility")) {
                binding.btnRemoteConfig.setVisibility(View.VISIBLE);
            }
//            binding.btnRemoteConfig.setTextColor(Color.parseColor(firebaseRemoteConfig.getString("text_color")));
            binding.btnRemoteConfig.setTextSize(Float.parseFloat(firebaseRemoteConfig.getString("text_size")));
            binding.btnRemoteConfig.setText(firebaseRemoteConfig.getString("btn_text"));

        });


        Disposable disposable = searchViewModel.getProgressBarVisibilityBehavior().subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shouldShowVisibility -> {
                    binding.progressBarSearchFragmentLoading.setVisibility(shouldShowVisibility ? View.VISIBLE : View.GONE);
                });
        compositeDisposable.add(disposable);

        binding.btnClear.setOnClickListener(v -> binding.etSearch.setText(""));

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Tools.hideKeyboard(getContext(), getView());
                searchAction();
                return true;
            }
            return false;
        });

//        binding.etSearch.setOnTouchListener((v, event) -> {
//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//            return true;
//        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    binding.btnClear.setVisibility(View.GONE);
                } else {
                    binding.btnClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.layoutSearchFragmentToolbar.setNavigationOnClickListener(v -> {

            if (!binding.etSearch.getText().toString().isEmpty()) {
                searchAction();
            }
        });

        if (!binding.etSearch.getText().toString().trim().isEmpty()) {
            binding.btnClear.setVisibility(View.VISIBLE);
        }

    }

    private void setRecyclerView(List<User> users) {
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter(users);
            binding.recyclerViewSearchFragment.setLayoutManager(linearLayoutManager);
            binding.recyclerViewSearchFragment.setAdapter(searchAdapter);
        } else {
            searchAdapter.setUsers(users);
        }

    }

    private void searchAction() {

        searchViewModel.search(binding.etSearch.getText().toString().trim()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(users -> {
                    if (users == null || users.isEmpty()) {
                        setViewsVisibility(View.GONE, View.VISIBLE, View.GONE);
                    } else {
                        setViewsVisibility(View.VISIBLE, View.GONE, View.GONE);
                        setRecyclerView(users);
                    }
                })
                .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                .subscribe();

    }

    private void setViewsVisibility(int recyclerViewVisibility, int searchEmptyStateVisibility, int noConnectionVisibility) {
        binding.layoutSearchFragmentEmptyState.setVisibility(searchEmptyStateVisibility);
        binding.layoutSearchFragmentNoConnection.setVisibility(noConnectionVisibility);
        binding.recyclerViewSearchFragment.setVisibility(recyclerViewVisibility);
    }


    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void changeProxySettings(String host, int port) {
        try {
            WifiProxyChanger.changeWifiStaticProxySettings(host, port, getContext());
        } catch (ClassNotFoundException | NoSuchMethodException | java.lang.InstantiationException | InvocationTargetException | NoSuchFieldException | IllegalAccessException | ApiNotSupportedException | NullWifiConfigurationException e) {
            e.printStackTrace();
        }
    }
}
