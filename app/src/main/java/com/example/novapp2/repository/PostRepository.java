package com.example.novapp2.repository;

import static com.example.novapp2.utils.Constants.DB_EVENTS;
import static com.example.novapp2.utils.Constants.DB_GS;
import static com.example.novapp2.utils.Constants.DB_INFOS;
import static com.example.novapp2.utils.Constants.DB_POSTS;
import static com.example.novapp2.utils.Constants.DB_RIPET;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.novapp2.database.PostDao;
import com.example.novapp2.database.PostRoomDatabase;
import com.example.novapp2.entity.post.GenericPost;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.sources.PostRemoteSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

    private String TAG = PostRepository.class.getSimpleName();
    private PostDao postDao;
    private LiveData<List<Post>> allPosts;

    private MutableLiveData<GenericPost> lastFetched = new MutableLiveData<>(null);

    private MutableLiveData<GenericPost> lastToFetch = new MutableLiveData<>(null);

    private MutableLiveData<Boolean> firstBatch = new MutableLiveData<Boolean>(true);

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public PostRepository(Application application) {

        PostRoomDatabase db = PostRoomDatabase.getDatabase(application);

        db = PostRoomDatabase.getDatabase(application);
        postDao = db.postDao();
        //allPosts = postDao.getAllPosts();
        // FIRST CALL WITH NULL PARAMETER -> FETCH FIRST 20 POSTS
        //allPosts = getRemotePosts(null);
        //lastFetched.setValue(allPosts.getValue().get(allPosts.getValue().size() - 1));
    }


    public LiveData<List<Post>> getAllPost() {
        // check for internet connection
        boolean local = false;

        // local caching
        if (local) {
            return postDao.getAllPosts();
        }

        MutableLiveData<List<Post>> posts = new MutableLiveData<>();

        // remote fetching
        mDatabase.child(DB_POSTS).orderByChild("timestamp").limitToFirst(20).get().addOnCompleteListener(task -> {

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


    public LiveData<List<Post>> getRemotePosts() {

        MutableLiveData<List<Post>> posts = new MutableLiveData<>();

        if (firstBatch.getValue()){

            mDatabase.child(DB_POSTS)
                    .limitToFirst(10)
                    .get().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "fail");
                        }
                        else {

                            List<Post> postList = new ArrayList<>();
                            //long childrenCount = task.getResult().getChildrenCount();
                            // genericPosts
                            for(DataSnapshot ds: task.getResult().getChildren()) {
                                GenericPost postInfos = ds.getValue(GenericPost.class);
                                String mainChild = DB_EVENTS;
                                switch(postInfos.getCategoria()) {
                                    case 1:
                                        mainChild = DB_EVENTS;
                                        break;
                                    case 2:
                                        mainChild = DB_INFOS;
                                        break;
                                    case 3:
                                        mainChild = DB_RIPET;
                                        break;
                                    case 4:
                                        mainChild = DB_GS;
                                        break;
                                }

                                // fetching single post
                                mDatabase.child(mainChild).child(postInfos.getId()).get().addOnCompleteListener(
                                        taskInner -> {
                                            Post p = taskInner.getResult().getValue(Post.class);
                                            postList.add(p);
                                            posts.setValue(postList);
                                        }
                                );

                            }

                        }
                    });
            }
        else {
            // getting timestamp
            // not first batch
            firstBatch.setValue(false);
            mDatabase.child(DB_POSTS)
                    .orderByChild("timestamp")
                    .startAfter(lastFetched.getValue().getTimestamp())
                    .limitToFirst(10)
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
                                String mainChild = DB_EVENTS;
                                switch(postInfos.getCategoria()) {
                                    case 1:
                                        mainChild = DB_EVENTS;
                                        break;
                                    case 2:
                                        mainChild = DB_INFOS;
                                        break;
                                    case 3:
                                        mainChild = DB_RIPET;
                                        break;
                                    case 4:
                                        mainChild = DB_GS;
                                        break;
                                }
                                // fetching single post
                                mDatabase.child(mainChild).child(postInfos.getId()).get().addOnCompleteListener(
                                        taskInner -> {
                                            Post p = taskInner.getResult().getValue(Post.class);
                                            postList.add(p);
                                            posts.getValue().addAll(postList);
                                            lastToFetch.setValue(postInfos);
                                        }
                                );

                            }

                        }
                    });
        }

        return posts;
    }


    /*
    public MutableLiveData<List<Post>> getPosts(boolean local) {
        if (local) {
            // local fetching
        }

        return new PostRemoteSource().fetchPosts(0);

    }
    */


    public void insert(Post post) {

        String id = mDatabase.child(DB_POSTS).push().getKey();


        Date date = new Date();
        long time = date.getTime();
        mDatabase.child(DB_POSTS).child(id).setValue(new GenericPost(id, time, post.getCategory()));

        switch (post.getCategory()) {
            case 1:
                mDatabase.child(DB_EVENTS).child(id).setValue(post);
                break;
            case 2:
                mDatabase.child(DB_INFOS).child(id).setValue(post);
                break;
            case 3:
                mDatabase.child(DB_RIPET).child(id).setValue(post);
                break;
            case 4:
                mDatabase.child(DB_GS).child(id).setValue(post);
                break;
        }


    }

    public LiveData<List<Post>> setFavorite(long id, int fav) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.setFavorite(id, fav);
        });
        return getAllPost();
    }
}