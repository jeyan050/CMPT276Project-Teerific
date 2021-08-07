package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.WrapperHoles;
import com.example.models.Hole;
import java.io.*;
import java.util.*;

class WrapperHolesTest {
    static WrapperHoles wh;

    @BeforeAll
    static void setUp(){
        wh = new WrapperHoles();
        ArrayList<Hole> initialAL = new ArrayList<Hole>();
        wh.setHoles(initialAL);
    }    

    @Test
    public void wrapperHolesHoles(){
        // detect if empty
        assertEquals(wh.getHoles().size(), 0);

        // initial tests with getters and setters
        ArrayList<Hole> holesAL = new ArrayList<Hole>();
        for(int h = 0; h < 2; h++){
            Hole temp = new Hole();
            temp.setYardage(h);
            temp.setHoleNumber(h);
            temp.setHandicap(h);
            temp.setPar(h);

            holesAL.add(temp);
        }
        wh.setHoles(holesAL);
        assertEquals(holesAL,wh.getHoles());

        // test empty arrayList
        ArrayList<Hole> emptyHolesAL = new ArrayList<Hole>();
        wh.setHoles(emptyHolesAL);
        assertEquals(emptyHolesAL,wh.getHoles());
    }
}
