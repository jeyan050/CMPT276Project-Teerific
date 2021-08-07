package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.ComparableParticipant;

class CompariableParticipantTest {
    static ComparableParticipant cp;

    @BeforeAll
    static void setUp(){
        cp = new ComparableParticipant();
        cp.setId(1);
        cp.setUsername("userName");
        cp.setFname("fname");
        cp.setLname("lname");
        cp.setScore(5);
    }    

    @Test
    public void comparableParticipantID(){
        // initial tests with getters and setters
        assertEquals(1,cp.getId());
        cp.setId(0);
        assertEquals(0,cp.getId());
    }

    @Test
    public void comparableParticipantUsername(){
        // initial tests with getters and setters
        assertEquals("userName",cp.getUsername());
        cp.setUsername("test");
        assertEquals("test",cp.getUsername());

        // test more than 1 word values
        cp.setUsername("Username Test");
        assertEquals("Username Test",cp.getUsername());

        // test empty values
        cp.setUsername("");
        assertEquals("",cp.getUsername());
    }

    @Test
    public void comparableParticipantFirstName(){
        // initial tests with getters and setters
        assertEquals("fname",cp.getFname());
        cp.setFname("test");
        assertEquals("test",cp.getFname());

        // test more than 1 word values
        cp.setFname("First Name Test");
        assertEquals("First Name Test",cp.getFname());

        // test empty values
        cp.setFname("");
        assertEquals("",cp.getFname());
    }

    @Test
    public void comparableParticipantLastName(){
        // initial tests with getters and setters
        assertEquals("lname",cp.getLname());
        cp.setLname("test");
        assertEquals("test",cp.getLname());

        // test more than 1 word values
        cp.setLname("Last Name Test");
        assertEquals("Last Name Test",cp.getLname());

        // test empty values
        cp.setLname("");
        assertEquals("",cp.getLname());
    }

    @Test
    public void comparableParticipantScore(){
        // initial tests with getters and setters
        assertEquals(5,cp.getScore());
        cp.setScore(0);
        assertEquals(0,cp.getScore());
    }

    @Test
    public void comparableParticpantsCompareTO(){
        ComparableParticipant newcp = new ComparableParticipant();
        newcp.setId(1);
        newcp.setUsername("userName");
        newcp.setFname("fname");
        newcp.setLname("lname");
        newcp.setScore(5);
        
        assertEquals(0,cp.compareTo(newcp));
    }
}
