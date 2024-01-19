package com.example.novapp2.repository;

import com.example.novapp2.entity.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements IUserRepository{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public Task<Void> insertUser(User user){
        return mDatabase.child("users").child(user.getID()).setValue(user);
    }

    @Override
    public Task<List<User>> getAllUsers() {
        TaskCompletionSource<List<User>> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }
                taskCompletionSource.setResult(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestire eventuali errori di lettura dal database
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }
                taskCompletionSource.setResult(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestire eventuali errori di lettura dal database
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Void> updateUserById(String userId, User updatedUser) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        // Assuming mDatabase is your DatabaseReference instance
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The user with the given userId exists, update the user
                    userRef.setValue(updatedUser)
                            .addOnSuccessListener(aVoid -> taskCompletionSource.setResult(null))
                            .addOnFailureListener(e -> taskCompletionSource.setException(e));
                } else {
                    // User with the given userId does not exist
                    taskCompletionSource.setException(new Exception("User not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any database error
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

}
