package com.example.novapp2.entity.chat.group;

import java.util.ArrayList;
import java.util.List;

public class GroupChatFactory {

    private static List<GroupChat> list;

    public static List<GroupChat> createGroupChats() {
        list = new ArrayList<>();
        list.add(new GroupChat("bello", "giorgio"));
        list.add(new GroupChat("bello", "giorgio"));
        list.add(new GroupChat("bello", "giorgio"));
        list.add(new GroupChat("bello", "giorgio"));
        return list;
    }
}
