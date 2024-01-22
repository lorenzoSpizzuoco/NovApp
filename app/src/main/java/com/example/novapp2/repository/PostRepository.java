package com.example.novapp2.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.novapp2.database.AdDao;
import com.example.novapp2.database.AdRoomDatabase;
import com.example.novapp2.database.PostDao;
import com.example.novapp2.database.PostRoomDatabase;
import com.example.novapp2.ui.ad.Ad;
import com.example.novapp2.ui.post.Post;

import java.util.List;

public class PostRepository {

    private String TAG = AdRepository.class.getSimpleName();
    private PostDao postDao;
    private LiveData<List<Post>> allPosts;

    public PostRepository(Application application) {
        //AdRoomDatabase db = AdRoomDatabase.getDatabase(application);
        //AdRoomDatabase.getDatabase(application).clearAllTables();
        PostRoomDatabase db = PostRoomDatabase.getDatabase(application);

        db = PostRoomDatabase.getDatabase(application);
        postDao = db.postDao();
        allPosts = postDao.getAllPosts();
    }

    public LiveData<List<Post>> getAllPost() {
        return allPosts;
    }

    public void insert(Post post) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insert(post);
        });
    }

    public void setFavorite(String title, int fav) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.setFavorite(title, fav);
        });
    }
}