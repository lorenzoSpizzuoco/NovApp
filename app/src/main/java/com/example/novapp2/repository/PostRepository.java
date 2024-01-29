package com.example.novapp2.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.novapp2.database.PostDao;
import com.example.novapp2.database.PostRoomDatabase;
import com.example.novapp2.entity.User;
import com.example.novapp2.entity.post.GenericPost;
import com.example.novapp2.entity.post.Post;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostRepository {

    private String TAG = PostRepository.class.getSimpleName();
    private PostDao postDao;
    private LiveData<List<Post>> allPosts;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public PostRepository(Application application) {
        //AdRoomDatabase db = AdRoomDatabase.getDatabase(application);
        //AdRoomDatabase.getDatabase(application).clearAllTables();
        PostRoomDatabase db = PostRoomDatabase.getDatabase(application);

        db = PostRoomDatabase.getDatabase(application);
        postDao = db.postDao();
        allPosts = postDao.getAllPosts();
    }

    public LiveData<List<Post>> getAllPost(Boolean local) {

        if (local) {
            return allPosts;
        }

        MutableLiveData<List<Post>> posts = new MutableLiveData<>();

        // remote fetching
        mDatabase.child("posts").orderByChild("timestamp").limitToFirst(20).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

            }
            else {
                List<Post> postList = new ArrayList<>();
                for(DataSnapshot ds: task.getResult().getChildren()) {
                    Post post = ds.getValue(Post.class);
                    postList.add(post);
                }

                posts.setValue(postList);
            }
        });



        return posts;
    }

    public LiveData<List<Post>> getRemotePosts(Post post) {
        Log.d(TAG, "vengo chiamato");
        MutableLiveData<List<Post>> posts = new MutableLiveData<>();
        mDatabase.child("posts")
                //.limitToFirst(10)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {

                        Log.d(TAG, "fail");
                    }
                    else {

                        List<Post> postList = new ArrayList<>();
                        long childrenCount = task.getResult().getChildrenCount();
                        int completed = 0;

                        // genericPosts
                        for(DataSnapshot ds: task.getResult().getChildren()) {
                            GenericPost postInfos = ds.getValue(GenericPost.class);
                            String mainChild = "events";
                            switch(postInfos.getCategoria()) {
                                case 1:
                                    mainChild = "events";
                                    break;
                                case 2:
                                    mainChild = "infos";
                                    break;
                                case 3:
                                    mainChild = "repetitions";
                                    break;
                                case 4:
                                    mainChild = "studyGroups";
                                    break;
                            }
                            Log.d(TAG, mainChild);
                            // fetching single post
                            mDatabase.child(mainChild).child(postInfos.getId()).get().addOnCompleteListener(
                                    taskInner -> {
                                        Log.d(TAG, taskInner.getResult().toString());
                                        Post p = taskInner.getResult().getValue(Post.class);
                                        Log.d(TAG, p.toString());
                                        postList.add(p);
                                        posts.setValue(postList);
                                    }
                            );

                        }

                    }
                });

        return posts;
    }




    public void insert(Post post) {

        Log.d("PostRepository", "sono qui");
        String id = mDatabase.child("posts").push().getKey();


        Date date = new Date();
        long time = date.getTime();
        mDatabase.child("posts").child(id).setValue(new GenericPost(id, time, post.getCategory()));

        switch (post.getCategory()) {
            case 1:
                mDatabase.child("event").child(id).setValue(post);
                break;
            case 2:
                mDatabase.child("infos").child(id).setValue(post);
                break;
            case 3:
                mDatabase.child("repetetions").child(id).setValue(post);
                break;
            case 4:
                mDatabase.child("studyGroups").child(id).setValue(post);
                break;
        }

        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insert(post);
        });





    }

    public LiveData<List<Post>> setFavorite(long id, int fav) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.setFavorite(id, fav);
        });
        return getAllPost(false);
    }
}