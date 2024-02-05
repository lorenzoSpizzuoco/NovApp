package com.novapp.bclub.repository.post;

import android.net.Uri;

import com.novapp.bclub.entity.post.Post;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface IPostRepository {

    Task<Void> insert(Post post, Uri image);

    Task<List<Post>> getAllPost();
}
