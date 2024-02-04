package com.novapp.bclub.entity.chat.group;


import androidx.annotation.NonNull;

import com.novapp.bclub.entity.chat.message.Message;

import java.util.List;

public class GroupChat {

    private String id;
    private String title;
    private String author;
    private String image;
    private List<Message> messages;

    public GroupChat() {
        this(null);
    }

    public GroupChat(String ID) {
        this.id = ID;
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

    @NonNull
    @Override
    public String toString() {
        return "GroupChat{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", image='" + image + '\'' +
                ", messages=" + messages +
                '}';
    }
}
