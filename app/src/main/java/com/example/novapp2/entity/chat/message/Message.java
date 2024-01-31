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

    @Override
    public String toString() {
        return "Message{" +
                "ID='" + ID + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
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
}
