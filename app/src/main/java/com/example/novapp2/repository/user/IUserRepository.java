package com.example.novapp2.repository.user;

import com.example.novapp2.entity.User;
import com.example.novapp2.entity.post.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface IUserRepository {

    public Task<Void> insertUser(User user);

    public Task<List<User>> getAllUsers();

    public Task<User> getUserById(String userId);

    public Task<Void> updateUserById(String userId, User updatedUser);

    public Task<User> getUserByEmail(String email);

    public Task<Void> insertSaved(String user, String postId, int category);

    public Task<Void> removeSaved(String user, String postId);

    public Task<DataSnapshot> getIsSaved(String user, String postId);

    public Task<List<Post>> getSavedPosts(String user);


    public Task<List<Post>> getUserPosts(String user);
}
