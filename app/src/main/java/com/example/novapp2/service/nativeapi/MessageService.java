package com.example.novapp2.service.nativeapi;

import com.example.novapp2.entity.chat.message.Message;
import com.example.novapp2.repository.message.MessageRepositoryImpl;
import com.example.novapp2.ui.home.HomeFragment;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessageService {
    private static MessageRepositoryImpl messagesRepositoryImpl = new MessageRepositoryImpl();

    public static Task<Void> createMessage(String content, String author, String groupID) {
        Date date = new Date();
        long timestamp = date.getTime();
        return messagesRepositoryImpl.insertMessage(new Message("", content, author, timestamp), groupID);
    }

    public static Task<List<Message>> getMessages() {
        return messagesRepositoryImpl.getAllMessage();
    }

    public static Task<Message> getMessageById(String userID) {
        return messagesRepositoryImpl.getMessageById(userID);
    }

    public static Task<Void> updateMessageById(String groupId, Message updatedMessage){
        return messagesRepositoryImpl.updateMessageById(groupId, updatedMessage);
    }

}
