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
    private String club_name; 
    // date, I think I can import a date package
    

// individual par points
// hidden partner par points
// individual best net
// hidden partner best net
// hidden partner odds and evens
// scramble
// skins
// best ball


//getters

    public Integer getId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public Integer participant_slots()
    {
        return this.participant_slots;
    }
    public Integer buyIn()
    {
        return this.buyIn;
    }
    public Integer first_prize()
    {
        return this.first_prize;
    }
    public Integer second_prize()
    {
        return this.second_prize;
    }
    public Integer third_prize()
    {
        return this.third_prize;
    }
    public Integer age_requirement()
    {
        return this.age_requirement;
    }
    public String game_mode()
    {
        return this.game_mode;
    }
    public String club_name()
    {
        return this.club_name;
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
    public void set_participant_slots(Integer num_participant)
    {
        this.participant_slots = num_participant;
    }
    public void set_buyIn(Integer buy)
    {
        this.buyIn = buy;
    }
    public void set_first_prize(Integer firstP)
    {
        this.first_prize = firstP;
    }
    public void set_second_prize(Integer secondP)
    {
        this.second_prize = secondP;
    }
    public void set_third_prize(Integer thirdP)
    {
        this.third_prize = thirdP;
    }
    public void set_age_requirement(Integer reqAge)
    {
        this.age_requirement = reqAge;
    }
    public void set_game_mode(String mode)
    {
        this.game_mode = mode;
    }
    public void set_club_name(String clubName)
    {
        this.club_name = clubName;
    }
}
