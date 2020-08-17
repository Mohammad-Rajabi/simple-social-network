package com.zoho.mohammadrajabi.socialnetwork.data;

public class TokenContainer {

    private static String Token;
    private static String userId;

    public static void updateToken(String token) {
        TokenContainer.Token = token;
    }

    public static String getToken() {
        return TokenContainer.Token;
    }

    public static void updateUserId(String userId) {
        TokenContainer.userId = userId;
    }

    public static String getUserId() {
        return TokenContainer.userId;
    }
}
