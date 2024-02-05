package com.novapp.bclub.entity.post;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.repository.post.PostRepositoryImpl;
import com.novapp.bclub.service.nativeapi.PostService;

import java.util.List;

public class SavedPostsViewModel extends AndroidViewModel {

    private PostService postService;
    MutableLiveData<List<Post>> savedPosts;

    public SavedPostsViewModel(@NonNull Application application) {
        super(application);
        postService = new PostService(application);
        savedPosts = postService.getAllPostRoom(false);
    }

    public MutableLiveData<List<Post>> getSavedPosts() {
        return savedPosts;
    }

    public void savePost(Post p) {
        postService.insertSaved(p);
    }


    public void removeSaved(Post p) {
        postService.removeSaved(p);
    }



}
