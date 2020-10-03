package com.zoho.mohammadrajabi.socialnetwork.ui.fragment.search;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.zoho.mohammadrajabi.socialnetwork.ui.adapter.SearchAdapter;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.databinding.FragmentSearchBinding;
import com.zoho.mohammadrajabi.socialnetwork.util.Tools;
import com.zoho.mohammadrajabi.socialnetwork.di.ViewModelInjector;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends DaggerFragment {

    @Inject
    ViewModelInjector viewModelInjector;

    @Inject
    SearchViewModel searchViewModel;

    @Inject
    LinearLayoutManager linearLayoutManager;

    private FragmentSearchBinding binding;
    private SearchAdapter searchAdapter;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this, viewModelInjector).get(SearchViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = FragmentSearchBinding.inflate(inflater, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.layoutSearchFragmentToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();


        binding.btnClear.setOnClickListener(v -> {
            binding.etSearch.setText("");
            if (searchAdapter != null) {
                searchAdapter.clearUsers();
                searchAdapter.notifyDataSetChanged();
            }
            if (binding.layoutSearchFragmentEmptyState.getVisibility() == View.VISIBLE) {
                binding.layoutSearchFragmentEmptyState.setVisibility(View.GONE);
            }
        });

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

        searchViewModel.search(binding.etSearch.getText().toString().trim())
                .observe(getViewLifecycleOwner(), listResources -> {

                    switch (listResources.status) {
                        case LOADING:
                            binding.progressBarSearchFragmentLoading.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            binding.progressBarSearchFragmentLoading.setVisibility(View.GONE);
                            if (listResources.data == null || listResources.data.isEmpty()) {
                                setViewsVisibility(View.GONE, View.VISIBLE, View.GONE);
                            } else {
                                setViewsVisibility(View.VISIBLE, View.GONE, View.GONE);
                                setRecyclerView(listResources.data);
                            }
                            break;
                    }
                });

    }

    private void setViewsVisibility(int recyclerViewVisibility, int searchEmptyStateVisibility, int noConnectionVisibility) {
        binding.layoutSearchFragmentEmptyState.setVisibility(searchEmptyStateVisibility);
        binding.layoutSearchFragmentNoConnection.setVisibility(noConnectionVisibility);
        binding.recyclerViewSearchFragment.setVisibility(recyclerViewVisibility);
    }


}
