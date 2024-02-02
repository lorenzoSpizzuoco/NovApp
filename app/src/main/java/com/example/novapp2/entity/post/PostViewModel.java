package com.example.novapp2.entity.post;

import static java.util.Collections.reverse;

import android.app.Application;
import android.net.Uri;
import android.util.Log;


import com.example.novapp2.service.PostService;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.home.HomeFragment;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    static final private String TAG = PostViewModel.class.getSimpleName();

    private static PostService postService = new PostService();

    private MutableLiveData<Integer> isFavorite = null;
    private MutableLiveData<Boolean> doneLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> savedPosts = null;
    //private final MutableLiveData<List<Post>> allPost = null;

    private MutableLiveData<List<Post>> allPost;
    private boolean calling = false;

    public PostViewModel (Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getIsFavorite(String user, String id) {

        if (isFavorite == null) {
            isFavorite = new MutableLiveData<>();
            postService.getIsSaved(user, id).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()){
                            isFavorite.postValue(task.getResult());
                        }
                    }
            );

            return isFavorite;
        }
        else {
            return isFavorite;
        }

    }

    public  void setFavorite(String id, int fav, int category) {

        if (fav == 1) {
            Log.d(TAG, "calling with fav 1");
            postService.insertSavedPost(HomeFragment.getActiveUser().getID(), id, category).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            isFavorite.setValue(fav);
                        }
                    }
            );
        }
        else {
            postService.removeSavedPost(HomeFragment.getActiveUser().getID(), id).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            isFavorite.setValue(fav);
                        }
                    }
            );
        }

    }

    public LiveData<Boolean> getDoneLoading() {
        return doneLoading;
    }

    public void refresh() {

        postService.getAllPost().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               List<Post> p = task.getResult();
               reverse(p);
               allPost.postValue(task.getResult());
           }
        });

    }


    public MutableLiveData<List<Post>> getAllPost() {

        if (allPost == null) {

            allPost = new MutableLiveData<>();
            postService.getAllPost().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Post> p = task.getResult();
                    reverse(p);
                    allPost.postValue(task.getResult());
                }
            });

        }

        return allPost;

    }

    public MutableLiveData<List<Post>> getFavoritePosts() {

        MutableLiveData<List<Post>> posts = new MutableLiveData<>();

        postService.getSavedPost(HomeFragment.getActiveUser().getID()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                posts.postValue(task.getResult());
            }
        });

        return posts;

    }


    public void insert(Post post, Uri image) {

        if (!calling) {
            calling = true;
            postService.insert(post, image).addOnCompleteListener(
                    t -> {
                        doneLoading.setValue(true);
                        calling = false;
                    }
            );
        }

    }

    public MutableLiveData<String> getAuthorImage(String email) {

        MutableLiveData<String> authorImage = new MutableLiveData<>();

        UserService.getUserByEmail(email).addOnCompleteListener(
          task -> {
              if (task.isSuccessful()) {
                  authorImage.postValue(task.getResult().getProfileImg());
              }
          }
        );

        return authorImage;
    }

}