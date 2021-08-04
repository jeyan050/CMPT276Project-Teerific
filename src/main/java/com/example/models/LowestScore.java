package com.example.models;

public class LowestScore {
    private String courseName;
    private int score;
    private String gameID;
    private String date;

    public String getCourseName(){
        return this.courseName;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public String getGameID(){
        return this.gameID;
    }

    public void setGameID(String gameID){
        this.gameID = gameID;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }
}