package com.example;

public class Tournament {
    private Integer id;
    private String name;
    private Integer participant_slots;
    private Integer buyIn;
    private Integer first_prize;
    private Integer second_prize;
    private Integer third_prize;
    private Integer age_requirement;
    private String game_mode;

// individual par points
// hidden partner par points
// individual best net
// hidden partner best net
// hidden partner odds and evens
// scramble
// skins
// best ball


    // date, I think I can import a date package
    // club 

    public Integer getId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    public void setName(String n)
    {
        this.name = n;
    }
}
