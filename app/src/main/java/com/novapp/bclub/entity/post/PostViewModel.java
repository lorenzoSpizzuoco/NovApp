package com.novapp.bclub.entity.post;

import static java.util.Collections.reverse;

import android.app.Application;
import android.net.Uri;
import android.util.Log;


import com.novapp.bclub.service.nativeapi.PostService;
import com.novapp.bclub.service.nativeapi.UserService;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Objects;

public class PostViewModel extends AndroidViewModel {

    static final private String TAG = PostViewModel.class.getSimpleName();

    private final PostService postService;

    private static final UserService userService = new UserService();

    private MutableLiveData<Integer> isFavorite = null;
    private final MutableLiveData<Boolean> doneLoading = new MutableLiveData<>();
    private MutableLiveData<List<Post>> userPosts;
    private LiveData<List<Post>> allPostRoom;
    private boolean calling = false;
    private MutableLiveData<Boolean> refresh;

    public PostViewModel (Application application) {
        super(application);
        refresh = new MutableLiveData<Boolean>(false);
        postService =  new PostService(application);
        allPostRoom = postService.getAllPostRoom(false);
    }

    public MutableLiveData<Integer> getIsFavorite(String user, String id) {
        
        isFavorite = new MutableLiveData<>();

        userService.getIsSaved(user, id).addOnCompleteListener(

                task -> {
                    if (task.isSuccessful()){
                        Log.d(TAG, "task success " + task.getResult().getKey());

                         if (Objects.equals(task.getResult().getKey(), id)) {
                             isFavorite.postValue(1);
                         }
                         else {
                             isFavorite.postValue(0);
                         }

                    }
                    else {
                        Log.e(TAG, Objects.requireNonNull(task.getException()).toString());
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
        refresh.setValue(true);
    }

    public void insert(Post post, Uri image) {

        if (!calling) {
            calling = true;
            Log.d(TAG, "doing insert " + post.getTitle());
            postService.insert(post, image).addOnCompleteListener(
                    t -> {
                        if (t.isSuccessful()) {
                            doneLoading.setValue(true);
                            calling = false;
                        }
                        else {
                            Log.d(TAG, "FAIL CHIAMATA");
                            doneLoading.setValue(true);
                            calling = false;
                        }

                    }
            );
        }

    }

    public LiveData<List<Post>> getPostsRoom() {
        boolean r = refresh.getValue();
        Log.d(TAG, "valore refresh " + r);
        refresh.setValue(false);
        allPostRoom = postService.getAllPostRoom(r);
        return allPostRoom;
    }

}