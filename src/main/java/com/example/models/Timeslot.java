package com.example.models;
import java.util.ArrayList;

public class Timeslot {
    
    private String time;
    private ArrayList<Integer> partySizes;

    public void setTime(String n) {
        this.time = n;
    }

    public String getTime() {
        return this.time;
    }

    public void setPartySizes(ArrayList<Integer> l) {
        this.partySizes = l;
    }

    public ArrayList<Integer> getPartySizes() {
        return this.partySizes;
    }
}
