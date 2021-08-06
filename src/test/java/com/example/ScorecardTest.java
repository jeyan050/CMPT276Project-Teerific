package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.Scorecard;
import java.io.*;
import java.util.*;

class ScorecardTest {
    static Scorecard s;

    @BeforeAll
    static void setUp(){
        s = new Scorecard();
        s.setActive(true);
        s.setDatePlayed("datePlayed");
        s.setUserName("userName");
        s.setCoursePlayed("coursePlayed");
        s.setCoursePlayedSC("snakeCaseCoursePlayed");
        s.setTeesPlayed("teesPlayed");
        s.setHolesPlayed("holesPlayed");
        s.setAttestor("attestor");
        s.setGameID("gameID");
        
        ArrayList<String> initialGM = new ArrayList<String>();
        s.setGroupMembers(initialGM);

        ArrayList<Integer> initialS = new ArrayList<Integer>();
        s.setStrokes(initialS);
    }

    @Test
    public void scorecardActive(){
        // initial tests with getters and setters
        assertEquals(true,s.isActive());
        s.setActive(false);
        assertEquals(false,s.isActive());
    }

    @Test
    public void scorecardDatePlayed(){
        // initial tests with getters and setters
        assertEquals("datePlayed",s.getDatePlayed());
        s.setDatePlayed("test");
        assertEquals("test",s.getDatePlayed());

        // test more than 1 word values
        s.setDatePlayed("Date Played Test");
        assertEquals("Date Played Test",s.getDatePlayed());

        // test empty values
        s.setDatePlayed("");
        assertEquals("",s.getDatePlayed());
    }

    @Test
    public void scorecardUsername(){
        // initial tests with getters and setters
        assertEquals("userName",s.getUserName());
        s.setUserName("test");
        assertEquals("test",s.getUserName());

        // test more than 1 word values
        s.setUserName("Username Test");
        assertEquals("Username Test",s.getUserName());

        // test empty values
        s.setUserName("");
        assertEquals("",s.getUserName());
    }

    @Test
    public void scorecardCoursePlayed(){
        // initial tests with getters and setters
        assertEquals("coursePlayed",s.getCoursePlayed());
        s.setCoursePlayed("test");
        assertEquals("test",s.getCoursePlayed());

        // test more than 1 word values
        s.setCoursePlayed("Course Played Test");
        assertEquals("Course Played Test",s.getCoursePlayed());

        // test empty values
        s.setCoursePlayed("");
        assertEquals("",s.getCoursePlayed());
    }

    @Test
    public void scorecardCoursePlayedSC(){
        // initial tests with getters and setters
        assertEquals("snakeCaseCoursePlayed",s.getCoursePlayedSC());
        s.setCoursePlayedSC("test");
        assertEquals("test",s.getCoursePlayedSC());

        // test more than 1 word values
        s.setCoursePlayedSC("Course Played Snake Cased Test");
        assertEquals("Course Played Snake Cased Test",s.getCoursePlayedSC());

        // test empty values
        s.setCoursePlayedSC("");
        assertEquals("",s.getCoursePlayedSC());
    }

    @Test
    public void scorecardTeesPlayed(){
        // initial tests with getters and setters
        assertEquals("teesPlayed",s.getTeesPlayed());
        s.setTeesPlayed("test");
        assertEquals("test",s.getTeesPlayed());

        // test more than 1 word values
        s.setTeesPlayed("Tees Played Test");
        assertEquals("Tees Played Test",s.getTeesPlayed());

        // test empty values
        s.setTeesPlayed("");
        assertEquals("",s.getTeesPlayed());
    }

    @Test
    public void scorecardHolesPlayed(){
        // initial tests with getters and setters
        assertEquals("holesPlayed",s.getHolesPlayed());
        s.setHolesPlayed("test");
        assertEquals("test",s.getHolesPlayed());

        // test more than 1 word values
        s.setHolesPlayed("Holes Played Test");
        assertEquals("Holes Played Test",s.getHolesPlayed());

        // test empty values
        s.setHolesPlayed("");
        assertEquals("",s.getHolesPlayed());
    }

    @Test
    public void scorecardAttestor(){
        // initial tests with getters and setters
        assertEquals("attestor",s.getAttestor());
        s.setAttestor("test");
        assertEquals("test",s.getAttestor());

        // test more than 1 word values
        s.setAttestor("Attestor Test");
        assertEquals("Attestor Test",s.getAttestor());

        // test empty values
        s.setAttestor("");
        assertEquals("",s.getAttestor());
    }

    @Test
    public void scorecardGameID(){
        // initial tests with getters and setters
        assertEquals("gameID",s.getGameID());
        s.setGameID("test");
        assertEquals("test",s.getGameID());

        // test more than 1 word values
        s.setGameID("Game ID Test");
        assertEquals("Game ID Test",s.getGameID());

        // test empty values
        s.setGameID("");
        assertEquals("",s.getGameID());
    }

    @Test
    public void scoreCardGroupMem(){
        // detect if empty
        assertEquals(s.getGroupMembers().size(), 0);

        // initial tests with getters and setters
        ArrayList<String> groupMemAL = new ArrayList<String>();
        for(int h = 0; h < 2; h++){
            String temp = "test:"+h;
            groupMemAL.add(temp);
        }
        s.setGroupMembers(groupMemAL);
        assertEquals(s.getGroupMembers(), Arrays.asList("test:0","test:1"));

        // test empty arrayList
        ArrayList<String> emptyHolesAL = new ArrayList<String>();
        s.setGroupMembers(emptyHolesAL);
        assertEquals(emptyHolesAL,s.getGroupMembers());
    }

    @Test
    public void scoreCardStrokes(){
        // detect if empty
        assertEquals(s.getStrokes().size(), 0);

        // initial tests with getters and setters
        ArrayList<Integer> groupMemAL = new ArrayList<Integer>();
        for(int h = 0; h < 2; h++){
            groupMemAL.add(h);
        }
        s.setStrokes(groupMemAL);
        assertEquals(s.getStrokes(), Arrays.asList(0,1));

        // test empty arrayList
        ArrayList<Integer> emptyHolesAL = new ArrayList<Integer>();
        s.setStrokes(emptyHolesAL);
        assertEquals(emptyHolesAL,s.getStrokes());
    }
}
