package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.LowestScore;

class LowestScoreTest {
    static LowestScore ls;

    @BeforeAll
    static void setUp(){
        ls = new LowestScore();
        ls.setCourseName("courseName");
        ls.setScore(4);
        ls.setGameID("gameID");
        ls.setDate("date");
    }

    @Test
    public void lowestScoreCourseName(){
        // initial tests with getters and setters
        assertEquals("courseName",ls.getCourseName());
        ls.setCourseName("test");
        assertEquals("test",ls.getCourseName());

        // test more than 1 word values
        ls.setCourseName("Course Name Test");
        assertEquals("Course Name Test",ls.getCourseName());

        // test empty values
        ls.setCourseName("");
        assertEquals("",ls.getCourseName());
    }

    @Test
    public void lowestScoreScore(){
        // initial tests with getters and setters
        assertEquals(4,ls.getScore());
        ls.setScore(0);
        assertEquals(0,ls.getScore());
    }

    @Test
    public void lowestScoreGameID(){
        // initial tests with getters and setters
        assertEquals("gameID",ls.getGameID());
        ls.setGameID("test");
        assertEquals("test",ls.getGameID());

        // test more than 1 word values
        ls.setGameID("Game ID Test");
        assertEquals("Game ID Test",ls.getGameID());

        // test empty values
        ls.setGameID("");
        assertEquals("",ls.getGameID());
    }

    @Test
    public void lowestScoreDate(){
        // initial tests with getters and setters
        assertEquals("date",ls.getDate());
        ls.setDate("test");
        assertEquals("test",ls.getDate());

        // test more than 1 word values
        ls.setDate("Game ID Test");
        assertEquals("Game ID Test",ls.getDate());

        // test empty values
        ls.setDate("");
        assertEquals("",ls.getDate());
    }
}
