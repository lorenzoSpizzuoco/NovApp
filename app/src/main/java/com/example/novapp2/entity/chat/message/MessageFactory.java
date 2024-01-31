package com.example.novapp2.entity.chat.message;


import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    private static List<Message> list;

    public static List<Message> createMessages() {
        list = new ArrayList<>();
        list.add(new Message("1", "bello1", "giorgio"));

        return list;
    }
}
