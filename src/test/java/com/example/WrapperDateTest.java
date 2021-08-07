package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.WrapperDate;

class WrapperDateTest {
    static WrapperDate wd;

    @BeforeAll
    static void setUp(){
        wd = new WrapperDate();
        wd.setDate("date");
    }

    @Test
    public void wrapperDateDate(){
        // initial tests with getters and setters
        assertEquals("date",wd.getDate());
        wd.setDate("test");
        assertEquals("test",wd.getDate());

        // test more than 1 word values
        wd.setDate("Date Test");
        assertEquals("Date Test",wd.getDate());

        // test empty values
        wd.setDate("");
        assertEquals("",wd.getDate());
    }
}
