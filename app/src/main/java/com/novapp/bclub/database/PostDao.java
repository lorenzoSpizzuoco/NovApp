package com.novapp.bclub.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.novapp.bclub.entity.post.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM post_table")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT * FROM post_table WHERE dbId = :id")
    LiveData<Post> getPost(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Delete
    void delete(Post post);

    @Query("UPDATE post_table SET favorite=:fav WHERE dbId= :id")
    void setFavorite(long id, int fav);

    @Query("DELETE FROM post_table")
    void deleteAll();


    @Query("SELECT * FROM post_table WHERE author = :author")
    LiveData<List<Post>> getUserPosts(String author);

}
