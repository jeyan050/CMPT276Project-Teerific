package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.TeeTimeBooking;

class TeeTimeBookingTest {
    static TeeTimeBooking tb;

    @BeforeAll
    static void setUp(){
        tb = new TeeTimeBooking();
        tb.setGameID(1);
        tb.setCourseName("courseName");
        tb.setUsername("username");
        tb.setDate("date");
        tb.setNumPlayers(4);
        tb.setTime("time");
        tb.setOptionalNote("opNote");
        tb.setRentalID("rentalID");
    }

    @Test
    public void teeTimeBookingGameID(){
        // initial tests with getters and setters
        assertEquals(1,tb.getGameID());
        tb.setGameID(0);
        assertEquals(0,tb.getGameID());
    }

    @Test
    public void teeTimeBookingCourseName(){
        // initial tests with getters and setters
        assertEquals("courseName",tb.getCourseName());
        tb.setCourseName("test");
        assertEquals("test",tb.getCourseName());

        // test more than 1 word values
        tb.setCourseName("Course Name Test");
        assertEquals("Course Name Test",tb.getCourseName());

        // test empty values
        tb.setCourseName("");
        assertEquals("",tb.getCourseName());
    }

    @Test
    public void teeTimeBookingUsername(){
        // initial tests with getters and setters
        assertEquals("username",tb.getUsername());
        tb.setUsername("test");
        assertEquals("test",tb.getUsername());

        // test more than 1 word values
        tb.setUsername("Username Test");
        assertEquals("Username Test",tb.getUsername());

        // test empty values
        tb.setUsername("");
        assertEquals("",tb.getUsername());
    }

    @Test
    public void teeTimeBookingDate(){
        // initial tests with getters and setters
        assertEquals("date",tb.getDate());
        tb.setDate("test");
        assertEquals("test",tb.getDate());

        // test more than 1 word values
        tb.setDate("Date Test");
        assertEquals("Date Test",tb.getDate());

        // test empty values
        tb.setDate("");
        assertEquals("",tb.getDate());
    }

    @Test
    public void teeTimeBookingNumPlayers(){
        // initial tests with getters and setters
        assertEquals(4,tb.getNumPlayers());
        tb.setNumPlayers(0);
        assertEquals(0,tb.getNumPlayers());
    }

    @Test
    public void teeTimeBookingTime(){
        // initial tests with getters and setters
        assertEquals("time",tb.getTime());
        tb.setTime("test");
        assertEquals("test",tb.getTime());

        // test more than 1 word values
        tb.setTime("Time Test");
        assertEquals("Time Test",tb.getTime());

        // test empty values
        tb.setTime("");
        assertEquals("",tb.getTime());
    }

    @Test
    public void teeTimeBookingOptionalNote(){
        // initial tests with getters and setters
        assertEquals("opNote",tb.getOptionalNote());
        tb.setOptionalNote("test");
        assertEquals("test",tb.getOptionalNote());

        // test more than 1 word values
        tb.setOptionalNote("Optional Note Test");
        assertEquals("Optional Note Test",tb.getOptionalNote());

        // test empty values
        tb.setOptionalNote("");
        assertEquals("",tb.getOptionalNote());
    }

    @Test
    public void teeTimeBookingRentalID(){
        // initial tests with getters and setters
        assertEquals("rentalID",tb.getRentalID());
        tb.setRentalID("test");
        assertEquals("test",tb.getRentalID());

        // test more than 1 word values
        tb.setRentalID("Rental ID Test");
        assertEquals("Rental ID Test",tb.getRentalID());

        // test empty values
        tb.setRentalID("");
        assertEquals("",tb.getRentalID());
    }
}
