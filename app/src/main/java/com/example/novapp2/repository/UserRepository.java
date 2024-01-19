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

public class UserRepository {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public Task<Void> insertUser(User user) {
        return mDatabase.child("users").child("1").setValue(user);
    }

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
}
