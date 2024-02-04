package com.novapp.bclub.utils;

public class Constants {
    public static final int DATABASE_VERSION = 1;

    public static final int POST_DATABASE_VERSION = 4;
    public static final String AD_DATABASE_NAME = "ad_db";

    public static final String POST_DATABASE_NAME = "post_db";

    public static final String PROFANITY_API_BASE_URL = "https://commentanalyzer.googleapis.com/";

    public static final String CHECK_PROFANITY_ENDPOINT = "v1alpha1/comments:analyze";

    public static final String API_KEY = "AIzaSyAOsGrRsKeqkYvvV8TYQ-8xjf7eXU3Rqtg";

    public static final String DB_POSTS = "posts";

    public static final String DB_RIPET = "repetitions";

    public static final String DB_GS  = "studyGroups";

    public static final String DB_EVENTS = "events";

    public static final String DB_INFOS = "infos";

    public static final String DB_USERS = "users";

    public static final String DB_SAVEDPOSTS = "savedPosts";

    public static final String DB_USERS_IMAGES = "userImages";

    public static final String DB_USER_POSTS = "userPosts";

    // User data on local
    public static final String USER_LOCAL_FILE = "user_local_variables";
    public static final String USER_LOCAL_PASSWORD = "user_local_password";
    public static final String USER_LOCAL_MAIL = "user_local_mail";

    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";

    // Onboarding
    public static final int NUM_PAGES = 3;

    // Text input fields
    public static final int MAX_NUM_CHAR_SMALL_TEXT = 25;
    public static final int MAX_NUM_CHAR_LONG_TEXT = 250;

}