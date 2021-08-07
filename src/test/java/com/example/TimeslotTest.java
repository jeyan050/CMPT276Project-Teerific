package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.Timeslot;
import java.io.*;
import java.util.*;

public class TimeslotTest {
    static Timeslot ts;

    @BeforeAll
    static void setUp(){
        ts = new Timeslot();

        ArrayList<Integer> initialAL = new ArrayList<Integer>();
        ts.setPartySizes(initialAL);

        ts.setTime("time");
    }

    @Test
    public void timeSlotPartySize(){
        // detect if empty
        assertEquals(ts.getPartySizes().size(), 0);

        // initial tests with getters and setters
        ArrayList<Integer> partySizeAL = new ArrayList<Integer>();
        for(int h = 0; h < 2; h++){
            partySizeAL.add(h);
        }
        ts.setPartySizes(partySizeAL);
        assertEquals(ts.getPartySizes(), Arrays.asList(0,1));

        // test empty arrayList
        ArrayList<Integer> emptyHolesAL = new ArrayList<Integer>();
        ts.setPartySizes(emptyHolesAL);
        assertEquals(emptyHolesAL,ts.getPartySizes());
    }

    @Test
    public void timeSlotTime(){
        // initial tests with getters and setters
        assertEquals("time",ts.getTime());
        ts.setTime("test");
        assertEquals("test",ts.getTime());

        // test more than 1 word values
        ts.setTime("Game ID Test");
        assertEquals("Game ID Test",ts.getTime());

        // test empty values
        ts.setTime("");
        assertEquals("",ts.getTime());
    }
}
