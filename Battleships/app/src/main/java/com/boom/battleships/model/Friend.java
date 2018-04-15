package com.boom.battleships.model;

public class Friend {
    private int friendshipId;
    private int id;
    private String name;
    private String picture;

    public Friend(int friendshipId, int id, String name, String picture) {
        this.friendshipId = friendshipId;
        this.id = id;
        this.name = name;
        this.picture = picture;
    }

    public int getFriendshipId() {
        return friendshipId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }
}
