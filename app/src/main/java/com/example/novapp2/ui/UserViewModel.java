package com.example.novapp2.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.novapp2.entity.User;
import com.example.novapp2.service.UserService;
import com.example.novapp2.sources.UserLogged;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserViewModel extends ViewModel {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MutableLiveData<User> currentUser;

    private final UserService userService = new UserService();

    public LiveData<User> getUser() {

        if (currentUser == null) {

            currentUser = new MutableLiveData<>();

            if (mAuth.getCurrentUser() != null) {

                Task<User> activeUserTask = UserService.getUserById(mAuth.getCurrentUser().getUid());
                activeUserTask.addOnCompleteListener(task -> {

                    if (activeUserTask.isSuccessful()) {
                        User user = activeUserTask.getResult();
                        if(user.getGroupChats() == null) {
                            user.groupChats = new ArrayList<>();
                        }
                        userService.setCurrentUser(task.getResult());
                        currentUser.postValue(task.getResult());

                    }
                });

            }

        }

        return currentUser;
    }
}
