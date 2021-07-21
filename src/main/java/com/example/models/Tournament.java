package com.example.models;

import java.util.ArrayList;

public class Tournament {
    private Integer id;
    private String name;
    private Integer participantSlots;
    private Integer buyIn;
    private String firstPrize;
    private String secondPrize;
    private String thirdPrize;
    private String ageRequirement;
    private String gameMode;
    private String clubName; 
    private String date;
    private String time;
    private ArrayList<User> participants;

//getters

    public Integer getId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public Integer getParticipantSlots()
    {
        return this.participantSlots;
    }
    public Integer getBuyIn()
    {
        return this.buyIn;
    }
    public String getFirstPrize()
    {
        return this.firstPrize;
    }
    public String getSecondPrize()
    {
        return this.secondPrize;
    }
    public String getThirdPrize()
    {
        return this.thirdPrize;
    }
    public String getAgeRequirement()
    {
        return this.ageRequirement;
    }
    public String getGameMode()
    {
        return this.gameMode;
    }
    public String getClubName()
    {
        return this.clubName;
    }
    public String getDate()
    {
        return this.date;
    }
    public String getTime()
    {
        return this.time;
    }
    public ArrayList<User> getParticipants()
    {
        return this.participants;
    }

//setters

    public void setId(Integer id)
    {
        this.id = id;
    }
    public void setName(String n)
    {
        this.name = n;
    }
    public void setParticipantSlots(Integer num_participant)
    {
        this.participantSlots = num_participant;
    }
    public void setBuyIn(Integer buy)
    {
        this.buyIn = buy;
    }
    public void setFirstPrize(String firstP)
    {
        this.firstPrize = firstP;
    }
    public void setSecondPrize(String secondP)
    {
        this.secondPrize = secondP;
    }
    public void setThirdPrize(String thirdP)
    {
        this.thirdPrize = thirdP;
    }
    public void setAgeRequirement(String reqAge)
    {
        this.ageRequirement = reqAge;
    }
    public void setGameMode(String mode)
    {
        this.gameMode = mode;
    }
    public void setClubName(String clubName)
    {
        this.clubName = clubName;
    }
    public void setDate(String d)
    {
        this.date = d;
    }
    public void setTime(String t)
    {
        this.time = t;
    }
    public void setParticipants(ArrayList<User> updatedUserList)
    {
        this.participants = updatedUserList;
    }
}
