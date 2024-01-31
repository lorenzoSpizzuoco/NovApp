package com.example.novapp2.repository.post;

import static com.example.novapp2.utils.Constants.DB_EVENTS;
import static com.example.novapp2.utils.Constants.DB_GS;
import static com.example.novapp2.utils.Constants.DB_INFOS;
import static com.example.novapp2.utils.Constants.DB_POSTS;
import static com.example.novapp2.utils.Constants.DB_RIPET;
import static com.example.novapp2.utils.Constants.DB_SAVEDPOSTS;
import static com.example.novapp2.utils.Constants.DB_USERS;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.novapp2.entity.post.GenericPost;
import com.example.novapp2.entity.post.Post;
import com.example.novapp2.utils.UploadImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostRepository implements IPostRepository{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private static final String TAG = PostRepository.class.getSimpleName();

    public Task<Void> insert(Post post, Uri image) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        String id = mDatabase.child(DB_POSTS).push().getKey();
        StorageReference storageRef = mStorage.getReference();
        post.setDbId(id);

        int category = post.getCategory();

        if (category == 1 || category == 4) {

            String mainChild = "postImages";

            // uploading in "groupImages" if it's a group image
            if (category == 4) {
                mainChild = "groupImages";
            }

            UploadImage.uploadImage(image, mainChild, id).addOnCompleteListener(
                    new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                post.setPostImage(downloadUri.toString());
                                Date date = new Date();
                                long time = date.getTime();
                                mDatabase.child(DB_POSTS).child(id).setValue(new GenericPost(id, time, post.getCategory()))
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                String child = getChildCategory(post.getCategory());
                                                mDatabase.child(child).child(id).setValue(post)
                                                        .addOnCompleteListener(task2 -> {
                                                            if (task2.isSuccessful()) {
                                                                taskCompletionSource.setResult(null);
                                                            } else {
                                                                taskCompletionSource.setException(task2.getException());
                                                            }
                                                        });
                                            } else {
                                                taskCompletionSource.setException(task1.getException());
                                            }
                                        });
                            } else {
                                taskCompletionSource.setException(task.getException());
                            }
                        }
                    });
        } else {
            Date date = new Date();
            long time = date.getTime();
            post.setPostImage(null);
            mDatabase.child(DB_POSTS).child(id).setValue(new GenericPost(id, time, post.getCategory()))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String child = getChildCategory(post.getCategory());
                            mDatabase.child(child).child(id).setValue(post)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            taskCompletionSource.setResult(null);
                                        } else {
                                            taskCompletionSource.setException(task1.getException());
                                        }
                                    });
                        } else {
                            taskCompletionSource.setException(task.getException());
                        }
                    });
        }

        return taskCompletionSource.getTask();
    }

    private String getChildCategory(int category) {
        switch (category) {
            case 1:
                return DB_EVENTS;
            case 2:
                return DB_INFOS;
            case 3:
                return DB_RIPET;
            case 4:
                return DB_GS;
            default:
                return DB_INFOS;
        }
    }


    @Override
    public Task<List<Post>> getAllPost() {

        TaskCompletionSource<List<Post>> taskCompletionSource = new TaskCompletionSource<>();
        List<Task<DataSnapshot>> tasks = new ArrayList<>();

        mDatabase.child(DB_POSTS)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "fail");
                        taskCompletionSource.setException(task.getException());
                    } else {

                        List<Post> postList = new ArrayList<>();

                        // genericPosts
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            GenericPost postInfos = ds.getValue(GenericPost.class);
                            String mainChild = getChildCategory(postInfos.getCategoria());

                            // fetching single post
                            Task<DataSnapshot> innerTask = mDatabase.child(mainChild).child(postInfos.getId()).get();
                            tasks.add(innerTask);
                        }

                        // Wait for all inner tasks to complete
                        Tasks.whenAllSuccess(tasks).addOnCompleteListener(innerTask -> {
                            for (Task<DataSnapshot> taskInner : tasks) {
                                if (taskInner.isSuccessful()) {
                                    Post p = taskInner.getResult().getValue(Post.class);
                                    postList.add(p);
                                }
                            }
                            taskCompletionSource.setResult(postList);
                        });
                    }
                });

        return taskCompletionSource.getTask();
    }

    public Task<DataSnapshot> getFavoritePosts(String user) {
        return mDatabase.child(DB_USERS).child(user).child(DB_SAVEDPOSTS).get();
    }

    public Task<Void> insertSaved(String user, String postId, int category) {
        return mDatabase.child(DB_USERS).child(user).child(DB_SAVEDPOSTS).child(postId).setValue(category);
    }

    @Override
    public Task<Void> removeSaved(String user, String postId) {
        return mDatabase.child(DB_USERS).child(user).child(DB_SAVEDPOSTS).child(postId).removeValue();
    }

    public Task<DataSnapshot> getIsSaved(String user, String postId) {
        return mDatabase.child(DB_USERS).child(user).child(DB_SAVEDPOSTS).child(postId).get();

    }

    @Override
    public Task<List<Post>> getSavedPosts(String user) {

        TaskCompletionSource<List<Post>> taskCompletionSource = new TaskCompletionSource<>();
        List<Task<DataSnapshot>> tasks = new ArrayList<>();

        mDatabase.child(DB_USERS).child(user).child(DB_SAVEDPOSTS)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "fail");
                        taskCompletionSource.setException(task.getException());
                    } else {

                        List<Post> postList = new ArrayList<>();

                        // genericPosts
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            String id = ds.getKey();
                            int cat = ds.getValue(Integer.class);
                            String mainChild = getChildCategory(cat);

                            // fetching single post
                            Task<DataSnapshot> innerTask = mDatabase.child(mainChild).child(id).get();
                            tasks.add(innerTask);
                        }

                        // Wait for all inner tasks to complete
                        Tasks.whenAllSuccess(tasks).addOnCompleteListener(innerTask -> {
                            for (Task<DataSnapshot> taskInner : tasks) {
                                if (taskInner.isSuccessful()) {
                                    Post p = taskInner.getResult().getValue(Post.class);
                                    postList.add(p);
                                }
                            }
                            taskCompletionSource.setResult(postList);
                        });
                    }
                });

        return taskCompletionSource.getTask();
    }
}
