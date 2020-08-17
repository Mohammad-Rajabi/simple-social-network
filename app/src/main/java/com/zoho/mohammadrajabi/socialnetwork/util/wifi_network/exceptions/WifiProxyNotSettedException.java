package com.zoho.mohammadrajabi.socialnetwork.util.wifi_network.exceptions;


public class WifiProxyNotSettedException extends IllegalStateException {

    public WifiProxyNotSettedException() {
        super("Wifi proxy not setted for current WifiConfiguration");
    }

}

