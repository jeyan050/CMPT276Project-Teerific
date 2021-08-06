package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.ScorecardHole;
import com.example.models.Hole;

class ScorecardHoleTest {
    static ScorecardHole sh;

    @BeforeAll
    static void setUp(){
        sh = new ScorecardHole();

        Hole temp = new Hole();
        sh.setHoleInfo(temp);

        sh.setStroke(4);
    }    

    @Test
    public void scorecardHoleInfo(){
        // initial tests with getters and setters
        Hole test = new Hole();
        test.setHandicap(5);
        test.setHoleNumber(1);
        test.setPar(5);
        test.setYardage(10);

        sh.setHoleInfo(test);
        assertEquals(test,sh.getHoleInfo());

        // test empty hole
        Hole emptyHole = new Hole();
        sh.setHoleInfo(emptyHole);
        assertEquals(emptyHole,sh.getHoleInfo());
    }

    @Test
    public void scorecardHoleStroke(){
        // initial tests with getters and setters
        assertEquals(4,sh.getStroke());
        sh.setStroke(0);
        assertEquals(0,sh.getStroke());
    }
}
