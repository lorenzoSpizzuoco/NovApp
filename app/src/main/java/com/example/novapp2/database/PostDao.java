package com.example.novapp2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.novapp2.ui.ad.Ad;
import com.example.novapp2.ui.post.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post_table ORDER BY date DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM post_table WHERE id = :id")
    Ad getPost(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    // just for testing

    @Query("DELETE FROM post_table")
    void deleteAll();
}
