package com.example.models;

import java.util.ArrayList;

public class Scorecard {
    private boolean active;
    private String datePlayed;
    private String userName;
    private String coursePlayed;
    private String coursePlayedSC;
    private String teesPlayed;
    private String holesPlayed;
    private String formatPlayed;
    private String attestor;
    private ArrayList<String> groupMembers;
    private String gameID;
    private ArrayList<Integer> strokes = new ArrayList<Integer>();


    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }


    public String getDatePlayed() {
        return this.datePlayed;
    }
    public void setDatePlayed(String f) {
        this.datePlayed = f;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getCoursePlayed() {
        return this.coursePlayed;
    }
    public void setCoursePlayed(String f) {
        this.coursePlayed = f;
    }

    public String getCoursePlayedSC() {
        return this.coursePlayedSC;
    }

    public void setCoursePlayedSC(String f) {
        this.coursePlayedSC = f;
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

    public ArrayList<String> getGroupMembers() {
        return this.groupMembers;
    }
    public void setGroupMembers(ArrayList<String> f) { 
        this.groupMembers = f; 
    }

    public String getGameID(){
        return this.gameID;
    }

    public void setGameID(String gameID){
        this.gameID = gameID;
    }

    public ArrayList<Integer> getStrokes(){
        return this.strokes;
    }

    public void setStrokes(ArrayList<Integer> strokes){
        this.strokes = strokes;
    }

}
