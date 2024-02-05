package com.novapp.bclub.service.nativeapi;

import com.novapp.bclub.entity.chat.message.Message;
import com.novapp.bclub.repository.message.MessageRepositoryImpl;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;

public class MessageService {
    private static final MessageRepositoryImpl messagesRepositoryImpl = new MessageRepositoryImpl();

    public static void createMessage(String content, String author, String groupID) {
        Date date = new Date();
        long timestamp = date.getTime();
        messagesRepositoryImpl.insertMessage(new Message("", content, author, timestamp), groupID);
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
