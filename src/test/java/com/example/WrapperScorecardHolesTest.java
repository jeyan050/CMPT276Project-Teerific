package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.WrapperScorecardHoles;
import com.example.models.ScorecardHole;
import com.example.models.Hole;
import java.io.*;
import java.util.*;

class WrapperScorecardHolesTest {
    static WrapperScorecardHoles wsh;

    @BeforeAll
    static void setUp(){
        wsh = new WrapperScorecardHoles();
        ArrayList<ScorecardHole> initialAL = new ArrayList<ScorecardHole>();
        wsh.setScorecardHoles(initialAL);
        wsh.setActive(true);
    }  
    
    @Test
    public void wrapperHolesHoles(){
        // detect if empty
        assertEquals(wsh.getScorecardHoles().size(), 0);

        // initial tests with getters and setters
        ArrayList<ScorecardHole> scoreHolesAL = new ArrayList<ScorecardHole>();
        for(Integer h = 0; h < 2; h++){
            ScorecardHole tempSH = new ScorecardHole();

            Hole temp = new Hole();
            temp.setYardage(h);
            temp.setHoleNumber(h);
            temp.setHandicap(h);
            temp.setPar(h);
            tempSH.setHoleInfo(temp);

            tempSH.setStroke(h);

            scoreHolesAL.add(tempSH);
        }
        wsh.setScorecardHoles(scoreHolesAL);
        assertEquals(scoreHolesAL,wsh.getScorecardHoles());

        // test empty arrayList
        ArrayList<ScorecardHole> emptyScoreHolesAL = new ArrayList<ScorecardHole>();
        wsh.setScorecardHoles(emptyScoreHolesAL);
        assertEquals(emptyScoreHolesAL,wsh.getScorecardHoles());
    }

    @Test
    public void tournamentID(){
        // initial tests with getters and setters
        assertEquals(true,wsh.isActive());
        wsh.setActive(false);
        assertEquals(false,wsh.isActive());
    }

}
