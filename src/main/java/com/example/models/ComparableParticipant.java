package com.example.models;


public class ComparableParticipant implements Comparable<ComparableParticipant>
{
    private Integer id;
    private String username;
    private String fname;
    private String lname;
    private Integer score;

    public Integer getId(){
        return this.id;
    }
    public void setId(Integer i){
        this.id = i;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String f) {
        this.username = f;
    }

    public String getFname() {
        return this.fname;
    }
    public void setFname(String f) {
        this.fname = f;
    }


    public String getLname() {
        return this.lname;
    }
    public void setLname(String f) {
        this.lname = f;
    }


    public Integer getScore(){
        return this.score;
    }
    public void setScore(Integer s){
        this.score = s;
    }

    public int compareTo(ComparableParticipant participant){
        return this.score - participant.score;
    }
}
