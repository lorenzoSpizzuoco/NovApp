package com.novapp.bclub.repository.user;

import com.novapp.bclub.entity.user.User;
import com.novapp.bclub.entity.post.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface IUserRepository {

    Task<Void> insertUser(User user);

    Task<List<User>> getAllUsers();

    Task<User> getUserById(String userId);

    Task<Void> updateUserById(String userId, User updatedUser);

    Task<User> getUserByEmail(String email);

    Task<Void> insertSaved(String user, String postId, int category);

    Task<Void> removeSaved(String user, String postId);

    Task<DataSnapshot> getIsSaved(String user, String postId);

    Task<List<Post>> getSavedPosts(String user);

    Task<List<Post>> getUserPosts(String user);
}
