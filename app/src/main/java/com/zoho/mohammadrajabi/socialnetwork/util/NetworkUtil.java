package com.zoho.mohammadrajabi.socialnetwork.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;


import javax.inject.Inject;

public class NetworkUtil {

    private Context context;

    @Inject
    public NetworkUtil(Context context) {
        this.context = context;
    }

    public boolean connectivity() {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                result = true;
            else if (connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                result = true;
            else if (connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                result = true;
            else result = false;

        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) result = true;
        }
        return result;
    }
}
