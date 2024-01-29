package com.example.novapp2.entity.chat.group;

import com.example.novapp2.entity.chat.message.MessageModel;

import java.util.List;

public class GroupChat {

    private int groupId;
    private String title;
    private String author;
    private String image;
    private List<MessageModel> messages;

    public GroupChat(String title, String author) {
        this.groupId = 0;
        this.title = title;
        this.author = author;
        this.image = null;
        this.messages = null;
    }

    public GroupChat(int groupId, String title, String author, String image) {
        this.groupId = groupId;
        this.title = title;
        this.author = author;
        this.image = image;
        this.messages = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
