package com.novapp.bclub.sources;

import static com.novapp.bclub.utils.Constants.DB_EVENTS;
import static com.novapp.bclub.utils.Constants.DB_GS;
import static com.novapp.bclub.utils.Constants.DB_INFOS;
import static com.novapp.bclub.utils.Constants.DB_POSTS;
import static com.novapp.bclub.utils.Constants.DB_RIPET;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.entity.post.GenericPost;
import com.novapp.bclub.entity.post.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostRemoteSource {

    private final static String TAG = PostRemoteSource.class.getSimpleName();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public MutableLiveData<List<Post>> fetchPosts(int page) {

        MutableLiveData<List<Post>> posts = new MutableLiveData<>(null);

        mDatabase.child(DB_POSTS)
                .limitToFirst(10)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "fail");
                    }
                    else {
                        ArrayList<Post> postList = new ArrayList<>();

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

        return posts;
    }

}
