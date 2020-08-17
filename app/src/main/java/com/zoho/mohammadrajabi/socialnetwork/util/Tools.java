package com.zoho.mohammadrajabi.socialnetwork.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Tools {

    public final static String regexp = "^09[0-9]{9}$";

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
