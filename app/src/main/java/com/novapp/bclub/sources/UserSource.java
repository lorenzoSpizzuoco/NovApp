package com.novapp.bclub.sources;

import com.novapp.bclub.entity.user.User;

public class UserSource {

    private static User user;

    public static void setUser(User u) {
        user = u;
    }

    public static User getUser() {
        return user;
    }

}
