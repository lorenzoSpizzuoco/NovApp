package com.example.novapp2.entity.chat.group;


import com.example.novapp2.entity.chat.message.Message;

import java.util.List;
import java.util.Map;

public class GroupChat {

    private String id;
    private String title;
    private String author;
    private String image;
    private List<Message> messages;


    public GroupChat() {
        this(null, null, null, null);
    }

    public GroupChat(String ID, String title, String author, String image) {
        this.id = ID;
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
        return id;
    }

    public String getImage() {
        return image;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
