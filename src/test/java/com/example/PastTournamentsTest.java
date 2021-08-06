package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.PastTournaments;

public class PastTournamentsTest {
    static PastTournaments pt;

    @BeforeAll
    static void setUp(){
        pt = new PastTournaments();
        pt.setId(1);
        pt.setName("name");
        pt.setDate("date");
        pt.setClubName("clubName");
    }

    @Test
    public void pastTournamentsID(){
        // initial tests with getters and setters
        assertEquals(1,pt.getId());
        pt.setId(0);
        assertEquals(0,pt.getId());
    }

    @Test
    public void pastTournamentsName(){
        // initial tests with getters and setters
        assertEquals("name",pt.getName());
        pt.setName("test");
        assertEquals("test",pt.getName());

        // test more than 1 word values
        pt.setName("Name Test");
        assertEquals("Name Test",pt.getName());

        // test empty values
        pt.setName("");
        assertEquals("",pt.getName());
    }

    @Test
    public void pastTournamentsDate(){
        // initial tests with getters and setters
        assertEquals("date",pt.getDate());
        pt.setDate("test");
        assertEquals("test",pt.getDate());

        // test more than 1 word values
        pt.setDate("Date Test");
        assertEquals("Date Test",pt.getDate());

        // test empty values
        pt.setDate("");
        assertEquals("",pt.getDate());
    }

    @Test
    public void pastTournamentsClubName(){
        // initial tests with getters and setters
        assertEquals("clubName",pt.getClubName());
        pt.setClubName("test");
        assertEquals("test",pt.getClubName());

        // test more than 1 word values
        pt.setClubName("Club Name Test");
        assertEquals("Club Name Test",pt.getClubName());

        // test empty values
        pt.setClubName("");
        assertEquals("",pt.getClubName());
    }
}
