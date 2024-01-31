package com.example.novapp2.repository.post;

import android.net.Uri;

import com.example.novapp2.entity.post.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public interface IPostRepository {

    public Task<Void> insert(Post post, Uri image);

    public Task<List<Post>> getAllPost();

    public Task<Void> insertSaved(String user, String postId);

    public Task<Void> removeSaved(String user, String postId);

    public Task<DataSnapshot> getFavoritePosts(String user);

    public Task<DataSnapshot> getIsSaved(String user, String postId);

}
