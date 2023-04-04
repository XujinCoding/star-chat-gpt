package com.star.session;

/**
 * @author XuJ
 */
public class WebAppSession extends AppSession {


    private static final String USER_NAME = "user_name";
    private static final String API_KEY = "api_key";

    public static String getUserName() {
        return getSessionAttribute().getStringValue(USER_NAME);
    }

    public static void setUserName(String userName) {
        getSessionAttribute().addValue(USER_NAME, userName);
    }

    public static String getApiKey() {
        return getSessionAttribute().getStringValue(API_KEY);
    }

    public static void setApiKey(String apiKey) {
        getSessionAttribute().addValue(API_KEY, apiKey);
    }
}
