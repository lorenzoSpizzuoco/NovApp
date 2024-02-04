package com.novapp.bclub.repository.message;

import androidx.annotation.NonNull;

import com.novapp.bclub.entity.chat.message.Message;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements IMessageRepository{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public Task<Void> insertMessage(Message message, String groupID) {
        String id = mDatabase.child("groupChats").child(groupID).child("messages").push().getKey();
        message.setID(id);
        return mDatabase.child("groupChats").child(groupID).child("messages").child(id).setValue(message);
    }

    @Override
    public Task<List<Message>> getAllMessage() {
        TaskCompletionSource<List<Message>> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messages.add(message);
                }
                taskCompletionSource.setResult(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Message> getMessageById(String messageId) {
        TaskCompletionSource<Message> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("messages").orderByChild("id").equalTo(messageId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Message message = new Message();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    message = snapshot.getValue(Message.class);
                }
                taskCompletionSource.setResult(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Void> updateMessageById(String messageId, Message updatedMessage) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference messageRef = mDatabase.child("messages").child(messageId);

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    messageRef.setValue(updatedMessage)
                            .addOnSuccessListener(aVoid -> taskCompletionSource.setResult(null))
                            .addOnFailureListener(taskCompletionSource::setException);
                } else {
                    taskCompletionSource.setException(new Exception("Message not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }
}
