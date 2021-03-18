package com.polytech.polysign.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "fr";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public final static String serverUrl = "http://localhost:9080/auth";
    public final static String realm = "jhipster";
    public final static String clientId = "web_app";
    public final static String clientSecret = "d6eb52d5-fb45-4a19-873d-67426d40d39c";

    public final static String userName = "admin";
    public final static String password = "admin";



    private Constants() {
    }
}
