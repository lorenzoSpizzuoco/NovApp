package com.novapp.bclub.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.novapp.bclub.entity.post.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM post_table ORDER BY date DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM post_table WHERE id = :id")
    Post getPost(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Query("UPDATE post_table SET favorite=:fav WHERE id= :id")
    void setFavorite(long id, int fav);

    // just for testing

    @Query("DELETE FROM post_table")
    void deleteAll();
}
