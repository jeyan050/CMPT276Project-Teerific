package com.example.models;

import java.util.ArrayList;

public class WrapperScorecardHoles {
    private ArrayList<ScorecardHole> scorecardHoles = new ArrayList<ScorecardHole>();
    boolean active;

    public ArrayList<ScorecardHole> getScorecardHoles() {
        return scorecardHoles;
    }

    public void setScorecardHoles(ArrayList<ScorecardHole> scorecardHoles){
        this.scorecardHoles = scorecardHoles;   
    }

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
}