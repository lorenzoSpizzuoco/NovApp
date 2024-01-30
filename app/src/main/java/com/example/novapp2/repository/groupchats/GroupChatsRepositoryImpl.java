package com.example.novapp2.repository.groupchats;

import androidx.annotation.NonNull;

import com.example.novapp2.entity.chat.group.GroupChat;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChatsRepositoryImpl implements IGroupChatsRepository {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public Task<Void> insertGroupChat(GroupChat groupChat) {
        return mDatabase.child("groupChats").child(groupChat.getID()).setValue(groupChat);
    }

    @Override
    public Task<List<GroupChat>> getAllGroupChat() {
        TaskCompletionSource<List<GroupChat>> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("groupChats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<GroupChat> groupChats = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GroupChat groupChat = snapshot.getValue(GroupChat.class);
                    groupChats.add(groupChat);
                }
                taskCompletionSource.setResult(groupChats);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<GroupChat> getGroupChatById(String groupChatId) {
        TaskCompletionSource<GroupChat> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child("groupChats").orderByChild("id").equalTo(groupChatId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GroupChat groupChat = new GroupChat();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    groupChat = snapshot.getValue(GroupChat.class);
                }
                taskCompletionSource.setResult(groupChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

    @Override
    public Task<Void> updateGroupChatById(String groupChatId, GroupChat updatedGroupChat) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        DatabaseReference groupChatRef = mDatabase.child("groupChats").child(groupChatId);

        groupChatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    groupChatRef.setValue(updatedGroupChat)
                            .addOnSuccessListener(aVoid -> taskCompletionSource.setResult(null))
                            .addOnFailureListener(taskCompletionSource::setException);
                } else {
                    taskCompletionSource.setException(new Exception("GroupChat not found"));
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
