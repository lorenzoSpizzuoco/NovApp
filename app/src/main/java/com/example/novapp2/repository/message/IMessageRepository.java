package com.example.novapp2.repository.message;

import com.example.novapp2.entity.chat.message.Message;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface IMessageRepository {

    Task<Void> insertMessage(Message message);

    Task<List<Message>> getAllMessage();

    Task<Message> getMessageById(String messageId);

    Task<Void> updateMessageById(String messageId, Message updatedMessage);
    
}
