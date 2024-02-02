package com.example.novapp2.service;

import com.example.novapp2.entity.User;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.repository.user.UserRepositoryImpl;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class UserService {

    private static UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl();

    public static Task<Void> createUser(String UID, String email) {
        // Static values for the user
        String name = "";
        String surname = "";
        String bio = "";
        List<String> groupChats = null;
        List<String> favourites = null;
        Boolean isBiccoccaUser = false; // Non è un utente di Biccocca
        String profileImg = "";
        List<String> notifications = null;

        return userRepositoryImpl.insertUser(new User(UID, name, email, surname, bio, groupChats, favourites, isBiccoccaUser, profileImg, notifications));
    }

    public static Task<List<User>> getUsers() {
        return userRepositoryImpl.getAllUsers();
    }

    public static Task<User> getUserById(String userID) {
        return userRepositoryImpl.getUserById(userID);
    }

    public static Task<Void> updateUserById(String userId, User updatedUser){
        return userRepositoryImpl.updateUserById(userId, updatedUser);
    }

    public static Task<User> getUserByEmail(String email) {
        return userRepositoryImpl.getUserByEmail(email);
    }

/*        UserService.getUserById("0bd5a35f-032c-4821-b251-f8592b0c0294").addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            User user = task.getResult();
            Log.i("User Info", "Name: " + user.getName() + ", Email: " + user.getEmail());
        } else {
            Exception exception = task.getException();
            Log.e("User Retrieval Error", exception.getMessage());}
    });*/


    public Task<Void> insertSavedPost(String user, String postId, int category) { return userRepositoryImpl.insertSaved(user, postId, category); }

    public Task<Void> removeSavedPost(String user, String postId) { return userRepositoryImpl.removeSaved(user, postId); }

    //public Task<DataSnapshot> getSavedPosts(String user) { return userRepositoryImpl.getFavoritePosts(user); }

    public Task<DataSnapshot> getIsSaved(String user, String id) {
        return userRepositoryImpl.getIsSaved(user, id);
    }


    public Task<List<Post>> getSavedPost(String user) {
        return userRepositoryImpl.getSavedPosts(user);
    }

    public Task<List<Post>> getUserPosts(String user) {
        return userRepositoryImpl.getUserPosts(user);
    }



}
