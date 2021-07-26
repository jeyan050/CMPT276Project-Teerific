package com.example.models;

public class Hole {
    private int yardage;
    private int par;
    private int handicap;
    private int holeNumber;

    public int getYardage() {
        return this.yardage;
    }

    public void setYardage(int f) {
        this.yardage = f;
    }

    public int getPar() { 
        return this.par; 
    }

    public void setPar(int f) {
        this.par = f;
    }

    public int getHandicap() {
        return this.handicap;
    }

    public void setHandicap(int f) { 
        this.handicap = f; 
    }

    public int getHoleNumber() {
        return this.holeNumber;
    }

    public void setHoleNumber(int f) {
        this.holeNumber = f;
    }
}
