package com.novapp.bclub.repository.message;

import com.novapp.bclub.entity.chat.message.Message;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface IMessageRepository {

    Task<Void> insertMessage(Message message, String groupID);

    Task<List<Message>> getAllMessage();

    Task<Message> getMessageById(String messageId);

    Task<Void> updateMessageById(String messageId, Message updatedMessage);
    
}
