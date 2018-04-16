package com.boom.battleships.model;

/**
 * Created by Ximena on 28/3/2018.
 */

public class Element {
    private int id;
    private String image;
    private String name;
    private int price;

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Element(int id, String image, String name, int price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
