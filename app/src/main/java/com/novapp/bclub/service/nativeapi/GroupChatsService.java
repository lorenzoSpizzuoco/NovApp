package com.novapp.bclub.service.nativeapi;

import com.novapp.bclub.entity.chat.group.GroupChat;
import com.novapp.bclub.repository.groupchats.GroupChatsRepositoryImpl;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class GroupChatsService {

    private static final GroupChatsRepositoryImpl groupChatsRepositoryImpl = new GroupChatsRepositoryImpl();

    public static void createGroupChat(String ID) {
        groupChatsRepositoryImpl.insertGroupChat(new GroupChat(ID));
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
