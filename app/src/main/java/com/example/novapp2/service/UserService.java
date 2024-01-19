package com.example.novapp2.service;

import android.util.Log;

import com.example.novapp2.entity.User;
import com.example.novapp2.repository.UserRepository;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class UserService {

    private static UserRepository userRepository = new UserRepository();
    public static Task<Void> createUser() {
        // Static values for the user
        String name = "John";
        String email = "john@example.com";
        String surname = "Doe";
        String bio = "Hello, I'm John Doe!";
        List<String> groupChats = Arrays.asList("GroupChat1", "GroupChat2");
        List<String> favourites = Arrays.asList("Favorite1", "Favorite2");
        Boolean isBiccoccaUser = true;
        String profileImg = "profile_image_url";
        String password = "securepassword";
        List<String> notifications = Arrays.asList("Notification1", "Notification2");

        return userRepository.insertUser(new User(name, email, surname, bio, groupChats, favourites, isBiccoccaUser, profileImg, password, notifications));
    }

    public static Task<List<User>> getUsers() {
        return userRepository.getAllUsers();
    }

// UserService.getUsers().addOnCompleteListener(task -> {
//     if (task.isSuccessful()) {
//         List<User> users = task.getResult();
//         for (User user : users) {
//             Log.i("User Info", "Name: " + user.getName() + ", Email: " + user.getEmail());
//             // Aggiungi ulteriori informazioni sugli utenti secondo necessità
//         }
//     } else {
//         Exception exception = task.getException();
//         Log.e("User Retrieval Error", exception.getMessage());
//     }
// });

}
