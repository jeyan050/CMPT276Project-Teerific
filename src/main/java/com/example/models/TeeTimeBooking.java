package com.example.models;

public class TeeTimeBooking {
    private String username;
    private String date; //will be input in dd/MM/yyyy format https://www.w3schools.com/java/java_date.asp
    private int numPlayers;
    private String time;
    private String optionalNote;
    private int numRentalBalls;
    private int numRentalCarts;
    private int numRentalClubs;

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String f) {
        this.username = f;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String f) { this.date = f; }

    public int getNumPlayers() {
        return this.numPlayers;
    }
    public void setNumPlayers(int f) {
        this.numPlayers = f;
    }

    public String getTime() {
        return this.time;
    }
    public void setTime(String f) {
        this.time = f;
    }

    public String getOptionalNote() {
        return this.optionalNote;
    }
    public void setOptionalNote(String f) {
        this.optionalNote = f;
    }

    public int getNumRentalBalls() { return this.numRentalBalls; }
    public void setNumRentalBalls(int f) {
        this.numRentalBalls = f;
    }

    public int getNumRentalCarts() { return this.numRentalCarts; }
    public void setNumRentalCarts(int f) {
        this.numRentalCarts = f;
    }

    public int getNumRentalClubs() {
        return this.numRentalClubs;
    }
    public void setNumRentalClubs(int f) {
        this.numRentalClubs = f;
    }
}
