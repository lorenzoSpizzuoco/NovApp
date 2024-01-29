package com.example.novapp2.entity.chat.group;

import com.example.novapp2.entity.chat.message.MessageModel;

import java.util.List;

public class GroupModel {

    private int groupId;
    private String titolo;
    private String authorId;
    private String image;
    private List<MessageModel> messages;

    public GroupModel(int groupId, String titolo, String authorId, String image) {
        this.groupId = groupId;
        this.titolo = titolo;
        this.authorId = authorId;
        this.image = image;
        this.messages = null;
    }
}
