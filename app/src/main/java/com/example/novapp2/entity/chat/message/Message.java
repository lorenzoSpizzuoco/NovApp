package com.example.novapp2.entity.chat.message;

public class Message {

    private String ID;
    private String content;
    private String author;

    public Message() {
        this(null, null, null);
    }

    public Message(String ID, String content, String author) {
        this.ID = ID;
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getID() {
        return ID;
    }
}
