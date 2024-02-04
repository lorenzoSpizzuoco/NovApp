package com.novapp.bclub.entity.post;

public class GenericPost {

    public GenericPost() {}

    private String id;
    private long timestamp;

    private int categoria;

    public GenericPost(String id, long timestamp, int categoria) {
        this.id = id;
        this.timestamp = timestamp;
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
}
