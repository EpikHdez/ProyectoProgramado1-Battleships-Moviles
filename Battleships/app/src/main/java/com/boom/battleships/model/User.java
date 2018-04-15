package com.boom.battleships.model;

import java.util.ArrayList;

/**
 * Created by Ximena on 12/4/2018.
 */

public class User {
    private static final User ourInstance = new User();
    private int id;
    private String name;
    private String picture;
    private String email;
    private int money;
    private int currentGame;

    public int getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(int currentGame) {
        this.currentGame = currentGame;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private ArrayList<Friend> friends;
    private ArrayList<Game> games;
    private ArrayList<Game> finishedGames;
    private ArrayList<Element> elements;

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
        friends=new ArrayList<>();
        games=new ArrayList<>();
        finishedGames=new ArrayList<>();
        elements=new ArrayList<>();

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

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public ArrayList<Game> getFinishedGames() {
        return finishedGames;
    }

    public void setFinishedGames(ArrayList<Game> finishedGames) {
        this.finishedGames = finishedGames;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", email='" + email + '\'' +
                ", friends=" + friends +
                ", games=" + games +
                ", finishedGames=" + finishedGames +
                ", elements=" + elements +
                '}';
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public void setPicture(Object picture) {
    }
}
