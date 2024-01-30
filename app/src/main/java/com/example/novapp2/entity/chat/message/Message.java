package com.example.novapp2.entity.chat.message;

public class Message {
    private String content;
    private String author;

    public Message() {
        this(null, null);
    }

    public Message(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
