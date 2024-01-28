package com.example.novapp2.service;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.novapp2.entity.User;
import com.example.novapp2.repository.user.UserRepositoryImpl;
import com.example.novapp2.ui.register.FullRegisterActivity;
import com.google.android.gms.tasks.Task;

import java.util.List;


public class UserService {

    private static UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();

    public static Task<Void> createDefaultUser(String UID, String email) {
        // Static values for the user
        String name = "";
        String surname = "";
        String bio = "";
        List<String> groupChats = null;
        List<String> favourites = null;
        Boolean isBicoccaUser = false;
        String profileImg = "";
        List<String> notifications = null;

        return userRepositoryImpl.insertUser(new User(UID, name, email, surname, bio, groupChats, favourites, isBicoccaUser, profileImg, notifications));
    }


    public static Task<List<User>> getUsers() {
        return userRepositoryImpl.getAllUsers();
    }

    public static Task<User> getUserById(String userID) {
        return userRepositoryImpl.getUserById(userID);
    }

    public Task<Void> updateUserById(String userId, User updatedUser){
        return userRepositoryImpl.updateUserById(userId, updatedUser);
    }


/*        UserService.getUserById("0bd5a35f-032c-4821-b251-f8592b0c0294").addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            User user = task.getResult();
            Log.i("User Info", "Name: " + user.getName() + ", Email: " + user.getEmail());
        } else {
            Exception exception = task.getException();
            Log.e("User Retrieval Error", exception.getMessage());}
    });*/


}
