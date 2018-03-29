package com.boom.battleships.views;

/**
 * Created by Ximena on 29/3/2018.
 */

public class RowItem {
    private int imageId;
    private String name;


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RowItem(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;


    }


}
