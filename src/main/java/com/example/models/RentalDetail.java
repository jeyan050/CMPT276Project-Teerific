package com.example.models;

public class RentalDetail {        // using this for admin view of rentals - Justin
    private String id;
    private String courseName;
    private String username;
    private String dateCheckout;
    private int numBalls;
    private int numCarts;
    private int numClubs;

    public String getID() {
        return this.id;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDateCheckout() {
        return this.dateCheckout;
    }
    
    public int getNumClubs() {
        return this.numClubs;
    }

    public int getNumCarts() {
        return this.numCarts;
    }

    public int getNumBalls() {
        return this.numBalls;
    }

    public void setID(String i) {
        this.id = i;
    }

    public void setCourseName(String c) {
        this.courseName = c;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public void setDateCheckout(String d) {
        this.dateCheckout = d;
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
}