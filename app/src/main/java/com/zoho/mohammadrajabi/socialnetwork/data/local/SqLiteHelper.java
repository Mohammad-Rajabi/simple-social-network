package com.zoho.mohammadrajabi.socialnetwork.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;


public class SqLiteHelper extends SQLiteAssetHelper {

    public static final String DBNAME = "user.db";
    public static final String TBL_USER = "user";

    SQLiteAssetHelper sqLiteAssetHelper;

    @Inject
    public SqLiteHelper(Context context) {
        super(context, SqLiteHelper.DBNAME, null, 1);
        sqLiteAssetHelper = this;
    }

    public Observable<List<User>> getUsers(String keyword) {

        return Observable.create(emitter -> {

            try {

                if (!emitter.isDisposed()) {

                    SQLiteDatabase db = sqLiteAssetHelper.getReadableDatabase();
                    List<User> users = new ArrayList<>();
                    Cursor cursor = db.rawQuery("SELECT * FROM " + SqLiteHelper.TBL_USER + " WHERE username LIKE '%" + keyword + "%';", null);
                    int i = 0;

                    cursor.moveToFirst();

                    if (cursor.getCount() > 0) {

                        while (!cursor.isAfterLast()) {
                            User user = new User();
                            user.setId(i++);
                            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                            users.add(user);
                            cursor.moveToNext();
                        }

                    }
                    cursor.close();
                    db.close();

                    if (!emitter.isDisposed()) {
                        emitter.onNext(users);
                        emitter.onComplete();
                    }

                }

            } catch (Exception e) {
                if (!emitter.isDisposed()) emitter.onError(e);
            }

        });
    }
}
