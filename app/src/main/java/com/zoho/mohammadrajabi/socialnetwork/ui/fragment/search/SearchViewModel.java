package com.zoho.mohammadrajabi.socialnetwork.ui.fragment.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zoho.mohammadrajabi.socialnetwork.data.Resources;
import com.zoho.mohammadrajabi.socialnetwork.data.local.SqLiteHelper;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private SqLiteHelper sqLiteHelper;
    private MutableLiveData<Resources<List<User>>> result;
    private Disposable disposable;

    @Inject
    public SearchViewModel(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public LiveData<Resources<List<User>>> search(String keyword) {


        result = new MutableLiveData<>();

        result.setValue(Resources.onLoading());

        sqLiteHelper.getUsers(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(List<User> users) {
                        result.setValue(Resources.onSuccess(users));
                    }

                    @Override
                    public void onError(Throwable e) {
                        result.setValue(Resources.onError(""));
                    }
                });

        return result;
    }

    @Override
    protected void onCleared() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
        super.onCleared();
    }
}
