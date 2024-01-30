package com.example.novapp2.entity.post;

import android.app.Application;
import android.net.Uri;


import com.example.novapp2.service.PostService;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    static final private String TAG = PostViewModel.class.getSimpleName();

    private static PostService postService = new PostService();
    private long lastelement;


    private MutableLiveData<Integer> isFavorite = new MutableLiveData<>();
    private MutableLiveData<Boolean> doneLoading = new MutableLiveData<>();
    private final MutableLiveData<List<Post>> allPost = null;

    public PostViewModel (Application application) {
        super(application);

        isFavorite.setValue(0);
        lastelement = 0;
    }

    public MutableLiveData<Integer> getIsFavorite() {
        return isFavorite;
    }

    public  void setFavorite(long id, int fav) {
        isFavorite.setValue(fav);
        //postRepository.setFavorite(id, fav);
    }

    public LiveData<Boolean> getDoneLoading() {
        return doneLoading;
    }

    public MutableLiveData<List<Post>> getAllPost() {

        MutableLiveData<List<Post>> posts = new MutableLiveData<>();

        postService.getAllPost().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                posts.postValue(task.getResult());
            }
        });

        return posts;
    }

    public void insert(Post post, Uri image) {
        postService.insert(post, image).addOnCompleteListener(
                t -> {
                    doneLoading.setValue(true);
                }
        );

    }
}