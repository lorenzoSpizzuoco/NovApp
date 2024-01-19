package com.example.novapp2.repository;

import com.example.novapp2.entity.User;
import com.google.android.gms.tasks.Task;
import java.util.List;

public interface IUserRepository {

    public Task<Void> insertUser(User user);

    public Task<List<User>> getAllUsers();

    public Task<User> getUserById(String userId);

    public Task<Void> updateUserById(String userId, User updatedUser);
}
