package com.novapp.bclub.ui.home.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.novapp.bclub.entity.chat.group.GroupChat;
import com.novapp.bclub.entity.chat.message.Message;
import com.novapp.bclub.service.nativeapi.GroupChatsService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Objects;

public class OpenChatViewModel extends AndroidViewModel {

    private MutableLiveData<Message> lastMessage;

    public OpenChatViewModel (Application application) {
        super(application);
    }

    public MutableLiveData<Message> getLastMessage(String groupId) {

        if (lastMessage == null) {

            lastMessage = new MutableLiveData<>();

            Task<GroupChat> getGroupByIdTask = GroupChatsService.getGroupChatById(groupId);
            getGroupByIdTask.addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    GroupChat activeGroup = task.getResult();
                    setUpOnDataChangeListener(activeGroup.getID());
                }
            });

        }

        return lastMessage;
    }

    private void setUpOnDataChangeListener(String groupId) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                .getReference("groupChats")
                .child(groupId)
                .child("messages");

        mDatabase.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                Message message = new Message(dataSnapshot.getKey(), (String) Objects.requireNonNull(value).get("content"), (String) value.get("author"), (Long) value.get("timestamp"));
                lastMessage.setValue(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OpenChat", "Failed to read value.", error.toException());
            }
        });
    }
}
