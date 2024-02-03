package com.example.novapp2.sources;

import com.example.novapp2.entity.User;

public class UserLogged {

    private static User user;

    public static void setUser(User u) {
        user = u;
    }

    public static User getUser() {
        return user;
    }

}
