package com.novapp.bclub.repository.post;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.entity.post.Post;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface IPostRepository {

    Task<Void> insert(Post post, Uri image);

    Task<List<Post>> getAllPost();

    //public MutableLiveData<List<Post>> getRoomSaved();

    public void insertLocal(Post post);

    public void removeSaved(Post post);

    public void deleteAll();

    public MutableLiveData<List<Post>> getAllPostRoom(boolean refresh);


}
