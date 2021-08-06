package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.Hole;

class HoleTest {
    static Hole h;

    @BeforeAll
    static void setUp(){
        h = new Hole();
        h.setYardage(500);
        h.setPar(5);
        h.setHandicap(5);
        h.setHoleNumber(1);
    }

    @Test
    public void holeYardage(){
        // initial tests with getters and setters
        assertEquals(500,h.getYardage());
        h.setYardage(0);
        assertEquals(0,h.getYardage());
    }

    @Test
    public void holePar(){
        // initial tests with getters and setters
        assertEquals(5,h.getPar());
        h.setPar(0);
        assertEquals(0,h.getPar());
    }

    @Test
    public void holeHandicap(){
        // initial tests with getters and setters
        assertEquals(5,h.getHandicap());
        h.setHandicap(0);
        assertEquals(0,h.getHandicap());
    }

    @Test
    public void holeHoleNumber(){
        // initial tests with getters and setters
        assertEquals(1,h.getHoleNumber());
        h.setHoleNumber(0);
        assertEquals(0,h.getHoleNumber());
    }
}
