package com.example.novapp2.entity.chat.message;


import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    private static List<Message> list;

    public static List<Message> createMessages() {
        list = new ArrayList<>();
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "leone@leone.com"));
        list.add(new Message("bello1", "leone@leone.com"));
        list.add(new Message("bello2", "leone@leone.com"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "piero"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "giorgio"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "piero"));
        list.add(new Message("bello2", "giorgio"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "giorgio"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "piero"));
        list.add(new Message("bello2", "piero"));
        list.add(new Message("bello2", "giorgio"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "giorgio"));
        list.add(new Message("bello2", "piero"));
        list.add(new Message("bello2", "leone@leone.com"));
        list.add(new Message("bello2", "leone@leone.com"));
        list.add(new Message("bello2", "leone@leone.com"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "giorgio"));
        list.add(new Message("bello1", "giorgio"));
        list.add(new Message("bello2", "giorgio"));

        return list;
    }
}
