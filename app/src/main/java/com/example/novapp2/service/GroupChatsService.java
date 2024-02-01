package com.example.novapp2.service;

import com.example.novapp2.entity.chat.group.GroupChat;
import com.example.novapp2.repository.groupchats.GroupChatsRepositoryImpl;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class GroupChatsService {

    private static GroupChatsRepositoryImpl groupChatsRepositoryImpl = new GroupChatsRepositoryImpl();

    public static Task<Void> createGroupChat(String ID) {
        return groupChatsRepositoryImpl.insertGroupChat(new GroupChat(ID));
    }

    public static Task<List<GroupChat>> getGroupChats() {
        return groupChatsRepositoryImpl.getAllGroupChat();
    }

    public static Task<GroupChat> getGroupChatById(String userID) {
        return groupChatsRepositoryImpl.getGroupChatById(userID);
    }

    public static Task<Void> updateGroupChatById(String groupId, GroupChat updatedGroupChat){
        return groupChatsRepositoryImpl.updateGroupChatById(groupId, updatedGroupChat);
    }
}
