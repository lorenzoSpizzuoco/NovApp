package com.example.novapp2.repository.groupchats;

import com.example.novapp2.entity.chat.group.GroupChat;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface IGroupChatsRepository {

    Task<Void> insertGroupChat(GroupChat groupChat);

    Task<List<GroupChat>> getAllGroupChat();

    Task<GroupChat> getGroupChatById(String groupChatId);

    Task<Void> updateGroupChatById(String groupChatId, GroupChat updatedGroupChat);

}
