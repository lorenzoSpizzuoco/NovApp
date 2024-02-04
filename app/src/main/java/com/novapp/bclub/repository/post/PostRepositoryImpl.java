package com.novapp.bclub.repository.post;

import static com.novapp.bclub.utils.Constants.DB_POSTS;
import static com.novapp.bclub.utils.Constants.DB_USERS;
import static com.novapp.bclub.utils.Constants.DB_USER_POSTS;
import static com.novapp.bclub.utils.Utils.getChildCategory;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.database.PostDao;
import com.novapp.bclub.database.PostRoomDatabase;
import com.novapp.bclub.entity.post.GenericPost;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.entity.user.User;
import com.novapp.bclub.service.nativeapi.GroupChatsService;
import com.novapp.bclub.service.nativeapi.UserService;
import com.novapp.bclub.sources.UserSource;
import com.novapp.bclub.utils.Utils;
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

public class PostRepositoryImpl implements IPostRepository{

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private static final String TAG = PostRepositoryImpl.class.getSimpleName();
    private PostDao postDao;
    private UserService userService;

    private MutableLiveData<List<Post>> savedRoomPosts;

    public PostRepositoryImpl(Application application) {

        PostRoomDatabase db = PostRoomDatabase.getDatabase(application);
        postDao = db.postDao();
        userService = new UserService();
        savedRoomPosts = new MutableLiveData<List<Post>>();

        userService.getSavedPost(userService.getCurrentUser().getID()).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        savedRoomPosts.postValue(task.getResult());
                    }
                }
        );

    }

    public Task<Void> insert(Post post, Uri image) {

        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        String id = mDatabase.child(DB_POSTS).push().getKey();
        StorageReference storageRef = mStorage.getReference();
        String userId = UserSource.getUser().getID();
        post.setDbId(id);

        int category = post.getCategory();

        if (category == 1 || category == 4) {

            String mainChild = "postImages";

            // uploading in "groupImages" if it's a group image
            if (category == 4) {
                mainChild = "groupImages";
            }

            Utils.uploadImage(image, mainChild, id).addOnCompleteListener(
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

                                                                mDatabase.child(DB_USERS).child(userId).child(DB_USER_POSTS).child(id).setValue(category).addOnCompleteListener(
                                                                        taskUser -> {
                                                                              if (taskUser.isSuccessful()) {
                                                                                  Log.d(TAG, "SUCCESS TASK");
                                                                                  taskCompletionSource.setResult(null);
                                                                              }
                                                                              else {
                                                                                  Log.e(TAG, taskUser.getException().toString());
                                                                                  taskCompletionSource.setException(taskUser.getException());
                                                                              }
                                                                        }
                                                                );

                                                                if(4 == post.getCategory()) {
                                                                    UserSource.getUser().groupChats.add(id);
                                                                    UserService.updateUserById(UserSource.getUser().userId, UserSource.getUser());
                                                                    GroupChatsService.createGroupChat(id);
                                                                }

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
                                            mDatabase.child(DB_USERS).child(userId).child(DB_USER_POSTS).child(id).setValue(category).addOnCompleteListener(
                                                    taskUser -> {
                                                        if (taskUser.isSuccessful()) {
                                                            Log.d(TAG, "SUCCESS TASK");
                                                            taskCompletionSource.setResult(null);
                                                        }
                                                        else {
                                                            Log.e(TAG, taskUser.getException().toString());
                                                            taskCompletionSource.setException(taskUser.getException());
                                                        }
                                                    }
                                            );
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

    @Override
    public Task<List<Post>> getAllPost() {

        TaskCompletionSource<List<Post>> taskCompletionSource = new TaskCompletionSource<>();
        List<Task<DataSnapshot>> tasks = new ArrayList<>();

        mDatabase.child(DB_POSTS)
                .orderByChild("timestamp")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "fail");
                        Log.e(TAG, task.getException().toString());
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


    public MutableLiveData<List<Post>> getRoomSaved() {
        return savedRoomPosts;
    };

    void insert(Post post) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insert(post);
        });
    }

}
