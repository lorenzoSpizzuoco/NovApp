package com.example.novapp2.entity.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.novapp2.entity.post.Post;
import com.example.novapp2.service.nativeapi.UserService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {

    private static final String TAG = UserViewModel.class.getSimpleName();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    MutableLiveData<User> currentUser;
    MutableLiveData<List<Post>> savedPosts = null;

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
                        if(user.getFavourites() == null) {
                            user.favourites = new ArrayList<Post>();
                        }
                        userService.setCurrentUser(task.getResult());
                        currentUser.postValue(task.getResult());

                    }
                });

            }

        }

        return currentUser;
    }

    public MutableLiveData<List<Post>> getSavedPosts() {

        // remote fetch
        if (savedPosts == null) {
            Log.d(TAG, "REMOTE FETCH USER SAVED POSTS");
            savedPosts = new MutableLiveData<>();

            userService.getSavedPost(userService.getCurrentUser().getID()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    savedPosts.postValue(task.getResult());
                    userService.getCurrentUser().favourites = task.getResult();
                }
            });
        }
        else {
            savedPosts.setValue(userService.getLocalFavorite());
        }



        return savedPosts;

    }


    public boolean isSaved(Post post) {
        return userService.isFavorite(post);
    }

    public void setFavorite(Post post) {
        userService.setLocalFavorite(post);
    }

    public void removeFavorite(Post post) {
        userService.removeLocalFavorite(post);
    }
}
