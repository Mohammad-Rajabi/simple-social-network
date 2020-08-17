package com.zoho.mohammadrajabi.socialnetwork.util.wifi_network;

import android.content.Context;

import com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.exceptions.ApiNotSupportedException;
import com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.exceptions.NullWifiConfigurationException;
import com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.wifi_proxy_changing_realisations.ProxySettings;

import java.lang.reflect.InvocationTargetException;

public abstract class WifiProxyInfo {

    public static String getHost(Context context)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            ApiNotSupportedException, NoSuchFieldException, NullWifiConfigurationException {
        return CurrentProxyChangerGetter.chooseProxyChangerForCurrentApi(context).getProxyHost();
    }

    public static int getPort(Context context)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            ApiNotSupportedException, NoSuchFieldException, NullWifiConfigurationException {
        return CurrentProxyChangerGetter.chooseProxyChangerForCurrentApi(context).getProxyPort();
    }

    public static ProxySettings getProxySettings(Context context)
            throws ApiNotSupportedException, IllegalAccessException, NoSuchFieldException,
            NullWifiConfigurationException {
        return CurrentProxyChangerGetter.chooseProxyChangerForCurrentApi(context).getProxySettings();
    }

}
