package com.example.novapp2.service;

import com.example.novapp2.entity.chat.message.Message;
import com.example.novapp2.repository.message.MessageRepositoryImpl;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;

public class MessageService {
    private static MessageRepositoryImpl messagesRepositoryImpl = new MessageRepositoryImpl();

    public static Task<Void> createMessage(String ID, String content, String author, String groupID) {
        Date date = new Date();
        long timestamp = date.getTime();
        return messagesRepositoryImpl.insertMessage(new Message(ID, content, author, timestamp), groupID);
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
