package com.example.novapp2.entity.chat.group;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupChatDB {
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static void saveGroupChatToDB(GroupChat groupChat) {
        mDatabase.child("groupChats").push().setValue(groupChat);
        Log.e("chat", "saved");
    }



}
