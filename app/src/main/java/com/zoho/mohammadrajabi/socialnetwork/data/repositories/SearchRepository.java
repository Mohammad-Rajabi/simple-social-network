package com.zoho.mohammadrajabi.socialnetwork.data.repositories;

import com.zoho.mohammadrajabi.socialnetwork.data.local.SqLiteHelper;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SearchRepository {

    private SqLiteHelper sqLiteHelper;

    @Inject
    public SearchRepository(SqLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public Observable<List<User>> search(String keyword) {
        return sqLiteHelper.getUsers(keyword);
    }

}
