package com.novapp.bclub.repository.user;

import static com.novapp.bclub.utils.Constants.DB_SAVEDPOSTS;
import static com.novapp.bclub.utils.Constants.DB_USERS;
import static com.novapp.bclub.utils.Constants.DB_USER_POSTS;
import static com.novapp.bclub.utils.Utils.getChildCategory;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.entity.user.User;
import com.novapp.bclub.entity.post.Post;
import com.novapp.bclub.sources.UserSource;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepositoryImpl implements IUserRepository{

    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = UserRepositoryImpl.class.getSimpleName();

    @Override
    public Task<Void> insertUser(User user){
        return mDatabase.child("users").child(user.getID()).setValue(user);
    }

    @Override
    public Task<List<User>> getAllUsers() {
        TaskCompletionSource<List<User>> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                taskCompletionSource.setResult(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<User> getUserByEmail(String email) {

        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                taskCompletionSource.setResult(users.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<User> getUserById(String userId) {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("users").orderByChild("id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = new User();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }
                taskCompletionSource.setResult(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Void> updateUserById(String userId, User updatedUser) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The user with the given userId exists, update the user
                    userRef.setValue(updatedUser)
                            .addOnSuccessListener(aVoid -> taskCompletionSource.setResult(null))
                            .addOnFailureListener(taskCompletionSource::setException);
                } else {
                    // User with the given userId does not exist
                    taskCompletionSource.setException(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any database error
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
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
                        taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
                    } else {

                        List<Post> postList = new ArrayList<>();

                        // genericPosts
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            String id = ds.getKey();
                            int cat = ds.getValue(Integer.class);
                            String mainChild = getChildCategory(cat);

                            // fetching single post
                            Task<DataSnapshot> innerTask = mDatabase.child(mainChild).child(Objects.requireNonNull(id)).get();
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

    @Override
    public Task<List<Post>> getUserPosts(String user) {

        TaskCompletionSource<List<Post>> taskCompletionSource = new TaskCompletionSource<>();
        List<Task<DataSnapshot>> tasks = new ArrayList<>();
        mDatabase.child(DB_USERS).child(user).child(DB_USER_POSTS)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "fail");
                        taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
                    } else {

                        List<Post> postList = new ArrayList<>();

                        // genericPosts
                        for (DataSnapshot ds : task.getResult().getChildren()) {
                            String id = ds.getKey();
                            int cat = ds.getValue(Integer.class);
                            String mainChild = getChildCategory(cat);

                            // fetching single post
                            assert id != null;
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



    public void setCurrentUser(User user) {
        UserSource.setUser(user);
    }

    public User getCurrentUser() {
        return UserSource.getUser();
    }

    public void setLocalFavorite(Post post) {
        UserSource.getUser().favourites.add(post);
    }

    public List<Post> getLocalFavorite() {
        return UserSource.getUser().favourites;
    }

    public void removeLocalFavorite(Post post) {
        UserSource.getUser().favourites.remove(post);
    }

    public void setRemoveSaved() {
        ArrayList<Post> posts = (ArrayList<Post>) UserSource.getUser().getFavourites();
        String userId = UserSource.getUser().getID();

        mDatabase.child(DB_USERS).child(userId).child(DB_SAVEDPOSTS).removeValue();

        for (Post post : posts) {
            mDatabase.child(DB_USERS).child(userId).child(DB_SAVEDPOSTS).child(post.getDbId()).setValue(post.getCategory());
        }
    }

}
