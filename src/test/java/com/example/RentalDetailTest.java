package com.example;

import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.RentalDetail;

public class RentalDetailTest {
    static RentalDetail rd;

    @BeforeAll
    static void setUp(){
        rd = new RentalDetail();
        rd.setID("id");
        rd.setCourseName("courseName");
        rd.setUsername("username");
        rd.setDateCheckout("checkoutDate");
        rd.setNumBalls(5);
        rd.setNumCarts(5);
        rd.setNumClubs(5);
    }

    @Test
    public void rentalDetailID(){
        // initial tests with getters and setters
        assertEquals("id",rd.getID());
        rd.setID("test");
        assertEquals("test",rd.getID());

        // test more than 1 word values
        rd.setID("ID Test");
        assertEquals("ID Test",rd.getID());

        // test empty values
        rd.setID("");
        assertEquals("",rd.getID());
    }

    @Test
    public void rentalDetailCourseName(){
        // initial tests with getters and setters
        assertEquals("courseName",rd.getCourseName());
        rd.setCourseName("test");
        assertEquals("test",rd.getCourseName());

        // test more than 1 word values
        rd.setCourseName("Course Name Test");
        assertEquals("Course Name Test",rd.getCourseName());

        // test empty values
        rd.setCourseName("");
        assertEquals("",rd.getCourseName());
    }

    @Test
    public void rentalDetailUsername(){
        // initial tests with getters and setters
        assertEquals("username",rd.getUsername());
        rd.setUsername("test");
        assertEquals("test",rd.getUsername());

        // test more than 1 word values
        rd.setUsername("Username Test");
        assertEquals("Username Test",rd.getUsername());

        // test empty values
        rd.setUsername("");
        assertEquals("",rd.getUsername());
    }

    @Test
    public void rentalDetailCheckoutDate(){
        // initial tests with getters and setters
        assertEquals("checkoutDate",rd.getDateCheckout());
        rd.setDateCheckout("test");
        assertEquals("test",rd.getDateCheckout());

        // test more than 1 word values
        rd.setDateCheckout("Checkout Date Test");
        assertEquals("Checkout Date Test",rd.getDateCheckout());

        // test empty values
        rd.setDateCheckout("");
        assertEquals("",rd.getDateCheckout());
    }

    @Test
    public void rentalDetailNumBalls(){
        // initial tests with getters and setters
        assertEquals(5,rd.getNumBalls());
        rd.setNumBalls(0);
        assertEquals(0,rd.getNumBalls());
    }

    @Test
    public void rentalDetailNumCarts(){
        // initial tests with getters and setters
        assertEquals(5,rd.getNumCarts());
        rd.setNumCarts(0);
        assertEquals(0,rd.getNumCarts());
    }

    @Test
    public void rentalDetailNumClubs(){
        // initial tests with getters and setters
        assertEquals(5,rd.getNumClubs());
        rd.setNumClubs(0);
        assertEquals(0,rd.getNumClubs());
    }
}
