package com.example.models;

import java.util.ArrayList;

public class ScorecardHole {
    public Hole holeInfo;
    public Integer stroke;

    public Hole getHoleInfo(){
        return this.holeInfo;
    }

    public void setHoleInfo(Hole holeInfo){
        this.holeInfo = holeInfo;
    }

    public Integer getStroke(){
        return this.stroke;
    }

    public void setStroke(Integer stroke){
        this.stroke = stroke;
    }
}