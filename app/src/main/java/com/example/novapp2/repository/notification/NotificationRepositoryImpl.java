package com.example.novapp2.repository.notification;

import com.example.novapp2.entity.Notification;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepositoryImpl implements INotificationRepository{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public Task<Void> insertNotification(Notification notification) {
        return mDatabase.child("notifications").child(notification.getID()).setValue(notification);
    }

    @Override
    public Task<List<Notification>> getAllNotifications() {
        TaskCompletionSource<List<Notification>> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("notifications").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Notification> notifications = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    notifications.add(notification);
                }
                taskCompletionSource.setResult(notifications);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Notification> getNotificationById(String notificationId) {
        TaskCompletionSource<Notification> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("notifications").orderByChild("id").equalTo(notificationId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notification notification = new Notification();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    notification = snapshot.getValue(Notification.class);
                }
                taskCompletionSource.setResult(notification);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Void> updateNotificationById(String notificationId, Notification updatedNotification) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference notificationRef = mDatabase.child("notifications").child(notificationId);

        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    notificationRef.setValue(updatedNotification)
                            .addOnSuccessListener(aVoid -> taskCompletionSource.setResult(null))
                            .addOnFailureListener(e -> taskCompletionSource.setException(e));
                } else {
                    taskCompletionSource.setException(new Exception("Notification not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }
}
