package com.example.models;

public class StatusTeeTime {
    private String status;
    private String teeTime;
    // Add Date for URL later

    public void setStatus(String s) {
        this.status = s;
    }

    public String getStatus() {
        return this.status;
    }

    public void setTeeTime(String t) {
        this.teeTime = t;
    }

    public String getTeeTime() {
        return this.teeTime;
    }
}
