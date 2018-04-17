package com.boom.battleships.model;

/**
 * Created by Ximena on 17/4/2018.
 */

public class User_Top {
    private int id;
    private String name;
    private String picture;
    private int score;

    public User_Top(int id, String name, String picture, int score) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
