package com.boom.battleships.model;

/**
 * Created by Ximena on 16/4/2018.
 */

public class ElementInventory {
    private int id;
    private int quantity;
    private int idItem;
    private String name;
    private String picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
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

    public ElementInventory(int id, int quantity, int idItem, String name, String picture) {
        this.id = id;
        this.quantity = quantity;
        this.idItem = idItem;
        this.name = name;
        this.picture = picture;
    }
}
