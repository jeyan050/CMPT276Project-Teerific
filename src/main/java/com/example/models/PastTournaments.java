package com.example.models;


public class PastTournaments
{
    private Integer id;
    private String name;
    private String date;
    private String clubName;

    public Integer getId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public String getClubName()
    {
        return this.clubName;
    }
    public String getDate()
    {
        return this.date;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }
    public void setName(String n)
    {
        this.name = n;
    }
    public void setClubName(String clubName)
    {
        this.clubName = clubName;
    }
    public void setDate(String d)
    {
        this.date = d;
    }
}