package com.example.novapp2.repository.groupchats;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.entity.chat.message.Message;
import com.example.novapp2.utils.Constants;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupChatsRepositoryImpl implements IGroupChatsRepository {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // TODO Non serve
    @Override
    public Task<Void> insertGroupChat(GroupChat groupChat) {
        return mDatabase.child("groupChats").child(groupChat.getID()).setValue(groupChat);
    }

    // TODO Non serve
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
        GroupChat groupChat = new GroupChat();
        List<Message> messages = new ArrayList<>();

        TaskCompletionSource<GroupChat> taskCompletionSource = new TaskCompletionSource<>();

        mDatabase.child(Constants.DB_GS).orderByChild("dbId").equalTo(groupChatId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();

                    groupChat.setId(snapshot.child("dbId").getValue(String.class));
                    groupChat.setImage("");
                    //groupChat.setImage(snapshot.child("image").getValue(String.class));
                    groupChat.setAuthor(snapshot.child("author").getValue(String.class));
                    groupChat.setTitle(snapshot.child("title").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                taskCompletionSource.setException(databaseError.toException());
            }
        });

        mDatabase.child("groupChats").orderByChild("id").equalTo(groupChatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate over the children of the "messages" node
                for (DataSnapshot messageSnapshot : dataSnapshot.child("messages").getChildren()) {
                    Map<String, Object> messageData = (Map<String, Object>) messageSnapshot.getValue();
                    Message message = new Message();
                    message.setAuthor(messageSnapshot.child("author").getValue(String.class));
                    message.setContent(messageSnapshot.child("content").getValue(String.class));
                    messages.add(message);
                }
                groupChat.setMessages(messages);
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
