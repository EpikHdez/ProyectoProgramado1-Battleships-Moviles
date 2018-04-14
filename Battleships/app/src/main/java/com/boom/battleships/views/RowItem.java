package com.boom.battleships.views;

/**
 * Created by Ximena on 29/3/2018.
 */

public class RowItem {
    private String imageId;
    private int match;
    private String name;
    private String turn;


    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public RowItem(String imageId, String name, String turn, int match) {
        this.imageId = imageId;
        this.name = name;

        this.turn = turn;
        this.match= match;


    }


}
