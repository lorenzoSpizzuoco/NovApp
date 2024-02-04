package com.novapp.bclub.entity.chat.message;

import androidx.annotation.NonNull;

public class Message {

    private String ID;
    private String content;
    private String author;
    private long timestamp;

    public Message() {
        this(null, null, null, -1);
    }

    public Message(String ID, String content, String author, long timestamp) {
        this.ID = ID;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "ID='" + ID + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
