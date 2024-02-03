package com.example.novapp2.entity.post;

import static java.util.Collections.reverse;

import android.app.Application;
import android.net.Uri;
import android.util.Log;


import com.example.novapp2.service.PostService;
import com.example.novapp2.service.UserService;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    static final private String TAG = PostViewModel.class.getSimpleName();

    private static final PostService postService = new PostService();

    private static final UserService userService = new UserService();

    private MutableLiveData<Integer> isFavorite = null;
    private final MutableLiveData<Boolean> doneLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> savedPosts = null;
    private MutableLiveData<List<Post>> userPosts;

    private MutableLiveData<List<Post>> allPost;
    private boolean calling = false;

    public PostViewModel (Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getIsFavorite(String user, String id) {
        
        isFavorite = new MutableLiveData<Integer>();

        userService.getIsSaved(user, id).addOnCompleteListener(

                task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "task success " + task.getResult().getKey().toString());

                         if (task.getResult().getKey().equals(id)) {
                             isFavorite.postValue(1);
                         }
                         else {
                             isFavorite.postValue(0);
                         }

                    }
                    else {
                        Log.e(TAG, task.getException().toString());
                    }
                }
        );
        return isFavorite;

    }

    public  void setFavorite(String id, int fav, int category) {

        if (fav == 1) {
            Log.d(TAG, "calling with fav 1");
            userService.insertSavedPost(userService.getCurrentUser().getID(), id, category).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            isFavorite.setValue(fav);
                        }
                    }
            );
        }
        else {
            userService.removeSavedPost(userService.getCurrentUser().getID(), id).addOnCompleteListener(
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

        userService.getSavedPost(userService.getCurrentUser().getID()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                posts.postValue(task.getResult());
            }
        });

        return posts;

    }


    public void insert(Post post, Uri image) {

        if (!calling) {
            calling = true;
            Log.d(TAG, "doing insert " + post.getTitle());
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

    public MutableLiveData<List<Post>> getUserPosts() {
        String user = userService.getCurrentUser().getID();
        if (userPosts == null) {
            userPosts = new MutableLiveData<List<Post>>();
            userService.getUserPosts(user).addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            userPosts.postValue(task.getResult());
                        }
                    }
            );
        }
        return userPosts;
    }
}