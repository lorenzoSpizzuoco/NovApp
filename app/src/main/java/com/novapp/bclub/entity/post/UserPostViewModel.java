package com.novapp.bclub.entity.post;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.service.nativeapi.PostService;
import com.novapp.bclub.service.nativeapi.UserService;

import java.util.List;

public class UserPostViewModel extends AndroidViewModel {

    PostService postService;
    UserService userService;
    private final LiveData<List<Post>> userPosts;

    public UserPostViewModel(@NonNull Application application) {
        super(application);
        userService = new UserService();
        postService = new PostService(application);
        userPosts = postService.getUserPosts(userService.getCurrentUser().getEmail());
    }

    public LiveData<List<Post>> getUserPosts() {
       return userPosts;
    }
}
