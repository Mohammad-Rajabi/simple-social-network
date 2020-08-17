package com.zoho.mohammadrajabi.socialnetwork.viewModel;

import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.data.repositories.SearchRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class SearchViewModel extends ViewModel {

    private SearchRepository searchRepository;
    private BehaviorSubject<Boolean> progressBarVisibilityBehavior = BehaviorSubject.create();

    @Inject
    public SearchViewModel(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public Observable<List<User>> search(String keyword) {
        progressBarVisibilityBehavior.onNext(true);
        return searchRepository.search(keyword)
                .doOnNext(users -> progressBarVisibilityBehavior.onNext(false))
                .doOnError(throwable -> progressBarVisibilityBehavior.onNext(false));

    }

    public BehaviorSubject<Boolean> getProgressBarVisibilityBehavior() {
        return progressBarVisibilityBehavior;
    }
}
