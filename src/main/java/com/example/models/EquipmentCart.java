package com.example.models;

public class EquipmentCart {

    private int numClubs;
    private int numCarts;
    private int numBalls;

    public int getNumClubs() {
        return this.numClubs;
    }

    public int getNumCarts() {
        return this.numCarts;
    }

    public int getNumBalls() {
        return this.numBalls;
    }

    public void setNumClubs(int n) {
        this.numClubs = n;
    }

    public void setNumCarts(int n) {
        this.numCarts = n;
    }

    public void setNumBalls(int n) {
        this.numBalls = n;
    }

    public void printfields() {
        System.out.println(this.numBalls);
        System.out.println(this.numCarts);
        System.out.println(this.numClubs);
    }
}
