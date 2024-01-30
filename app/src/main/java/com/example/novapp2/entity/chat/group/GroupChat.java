package com.example.novapp2.entity.chat.group;

import com.example.novapp2.entity.chat.message.Message;

import java.util.List;

public class GroupChat {

    private String ID;
    private String title;
    private String author;
    private String image;
    private List<Message> messages;


    public GroupChat() {
        this(null, null, null, null);
    }

    public GroupChat(String ID, String title, String author, String image) {
        this.ID = ID;
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

    public String getID() {
        return ID;
    }

    public String getImage() {
        return image;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
