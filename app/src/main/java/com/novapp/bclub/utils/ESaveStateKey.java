package com.novapp.bclub.utils;

public enum ESaveStateKey {

    USER_PROFILE_STATE("user_profile_state"),
    TEST2("test2");


    private final String key;

    ESaveStateKey(String key) {
        this.key = key;
    }

    public String getKey(){
        return key;
    }
}
