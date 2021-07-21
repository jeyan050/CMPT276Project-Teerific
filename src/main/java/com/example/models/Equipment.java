package com.example.models;

// Used in viewInventory()
public class Equipment {
    private String itemName;
    private int stock;

    public String getItemName() {
        return this.itemName;
    }

    public int getStock() {
        return this.stock;
    }

    public void setItemName(String n) {
        this.itemName = n;
    }

    public void setStock(int n) {
        this.stock = n;
    }
}
