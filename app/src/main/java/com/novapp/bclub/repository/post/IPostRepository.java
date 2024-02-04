package com.novapp.bclub.repository.post;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.entity.post.Post;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface IPostRepository {

    public Task<Void> insert(Post post, Uri image);

    public Task<List<Post>> getAllPost();

    public MutableLiveData<List<Post>> getRoomSaved();

    public void insertLocal(Post p);

    public void removeSaved(Post p);

    public LiveData<Post> isFavorite(Post p);

    public void deleteAll();

}
