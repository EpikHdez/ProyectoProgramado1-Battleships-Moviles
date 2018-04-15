package com.boom.battleships.model;

import java.util.Date;

public class Message {
    private int userId;
    private String text;
    private String date;

    public Message(int userId, String text, String date) {
        this.userId = userId;
        this.text = text;
        this.date = date;
    }

    public Message(int userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
