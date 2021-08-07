package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.TournamentParticipant;

class TournamentParticipantTest {
    static TournamentParticipant tp;

    @BeforeAll
    static void setUp(){
        tp = new TournamentParticipant();
        tp.setId(1);
        tp.setUsername("userName");
        tp.setFname("fname");
        tp.setLname("lname");
        tp.setScore(5);
    }

    @Test
    public void tournamentParticipantID(){
        // initial tests with getters and setters
        assertEquals(1,tp.getId());
        tp.setId(0);
        assertEquals(0,tp.getId());
    }

    @Test
    public void tournamentParticipantName(){
        // initial tests with getters and setters
        assertEquals("userName",tp.getUsername());
        tp.setUsername("test");
        assertEquals("test",tp.getUsername());

        // test more than 1 word values
        tp.setUsername("Username Test");
        assertEquals("Username Test",tp.getUsername());

        // test empty values
        tp.setUsername("");
        assertEquals("",tp.getUsername());
    }

    @Test
    public void tournamentParticipantFirstName(){
        // initial tests with getters and setters
        assertEquals("fname",tp.getFname());
        tp.setFname("test");
        assertEquals("test",tp.getFname());

        // test more than 1 word values
        tp.setFname("First Name Test");
        assertEquals("First Name Test",tp.getFname());

        // test empty values
        tp.setFname("");
        assertEquals("",tp.getFname());
    }

    @Test
    public void tournamentParticipantLastName(){
        // initial tests with getters and setters
        assertEquals("lname",tp.getLname());
        tp.setLname("test");
        assertEquals("test",tp.getLname());

        // test more than 1 word values
        tp.setLname("Last Name Test");
        assertEquals("Last Name Test",tp.getLname());

        // test empty values
        tp.setLname("");
        assertEquals("",tp.getLname());
    }
    @Test
    public void tournamentParticipantScore(){
        // initial tests with getters and setters
        assertEquals(5,tp.getScore());
        tp.setScore(0);
        assertEquals(0,tp.getScore());
    }
}
