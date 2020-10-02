package com.zoho.mohammadrajabi.socialnetwork.data;

public class Resources<T> {

    public Status status;
    public T data;
    public String message;

    private Resources (Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resources<T> onConnectivity() {
        return new Resources(Status.NETWORK_CONNECTIVITY, null ,null);
    }

    public static <T> Resources<T> onLoading() {
        return new Resources(Status.LOADING, null ,null);
    }

    public static <T> Resources<T> onError(String message) {
        return new Resources(Status.ERROR, null ,message);
    }

    public static <T> Resources onSuccess(T data) {
        return new Resources(Status.SUCCESS, data ,null);
    }

    public enum Status {
        NETWORK_CONNECTIVITY, LOADING, ERROR, SUCCESS;
    }
}
