package com.example;

import java.util.ArrayList;

public class Scorecard {
    private String datePlayed;
    private String coursePlayed;
    private String teesPlayed;
    private String holesPlayed;
    private String formatPlayed;
    private String attestor;
    private ArrayList groupMembers;
    private String gameID;

    public String getDatePlayed() {
        return this.datePlayed;
    }
    public void setDatePlayed(String f) {
        this.datePlayed = f;
    }

    public String getCoursePlayed() {
        return this.coursePlayed;
    }
    public void setCoursePlayed(String f) {
        this.coursePlayed = f;
    }

    public String getTeesPlayed() {
        return this.teesPlayed;
    }
    public void setTeesPlayed(String f) { this.teesPlayed = f; }

    public String getHolesPlayed() {
        return this.holesPlayed;
    }
    public void setHolesPlayed(String f) {
        this.holesPlayed = f;
    }

    public String getFormatPlayed() {
        return this.formatPlayed;
    }
    public void setFormatPlayed(String f) { this.formatPlayed = f; }

    public String getAttestor() {
        return this.attestor;
    }
    public void setAttestor(String f) { this.attestor = f; }

    public ArrayList getGroupMembers() {
        return this.groupMembers;
    }
    public void setUsername(ArrayList f) { this.groupMembers = f; }

    public String getGameID(){
        return this.gameID;
    }

    public void setGameID(String gameID){
        this.gameID = gameID;
    }
}
