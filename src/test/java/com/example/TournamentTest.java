package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.Tournament;

class TournamentTest {
    static Tournament t;

    @BeforeAll
    static void setUp(){
        t = new Tournament();
        t.setId(1);
        t.setName("name");
        t.setParticipantSlots(4);
        t.setBuyIn(5);
        t.setFirstPrize("firstP");
        t.setSecondPrize("secondP");
        t.setThirdPrize("thirdP");
        t.setAgeRequirement("reqAge");
        t.setGameMode("mode");
        t.setClubName("clubName");
        t.setDate("date");
        t.setTime("time");
        t.setCreator("creator");
        t.setNumSignedUp(1);
    }

    @Test
    public void tournamentID(){
        // initial tests with getters and setters
        assertEquals(1,t.getId());
        t.setId(0);
        assertEquals(0,t.getId());
    }

    @Test
    public void tournamentName(){
        // initial tests with getters and setters
        assertEquals("name",t.getName());
        t.setName("test");
        assertEquals("test",t.getName());

        // test more than 1 word values
        t.setName("Name Test");
        assertEquals("Name Test",t.getName());

        // test empty values
        t.setName("");
        assertEquals("",t.getName());
    }

    @Test
    public void tournamentParticipantSlots(){
        // initial tests with getters and setters
        assertEquals(4,t.getParticipantSlots());
        t.setParticipantSlots(0);
        assertEquals(0,t.getParticipantSlots());
    }

    @Test
    public void tournamentBuyIn(){
        // initial tests with getters and setters
        assertEquals(5,t.getBuyIn());
        t.setBuyIn(0);
        assertEquals(0,t.getBuyIn());
    }


    @Test
    public void tournamentFirstPrize(){
        // initial tests with getters and setters
        assertEquals("firstP",t.getFirstPrize());
        t.setFirstPrize("test");
        assertEquals("test",t.getFirstPrize());

        // test more than 1 word values
        t.setFirstPrize("First Prize Test");
        assertEquals("First Prize Test",t.getFirstPrize());

        // test empty values
        t.setFirstPrize("");
        assertEquals("",t.getFirstPrize());
    }

    @Test
    public void tournamentSecondPrize(){
        // initial tests with getters and setters
        assertEquals("secondP",t.getSecondPrize());
        t.setSecondPrize("test");
        assertEquals("test",t.getSecondPrize());

        // test more than 1 word values
        t.setSecondPrize("Second Prize Test");
        assertEquals("Second Prize Test",t.getSecondPrize());

        // test empty values
        t.setSecondPrize("");
        assertEquals("",t.getSecondPrize());
    }

    @Test
    public void tournamentThirdPrize(){
        // initial tests with getters and setters
        assertEquals("thirdP",t.getThirdPrize());
        t.setThirdPrize("test");
        assertEquals("test",t.getThirdPrize());

        // test more than 1 word values
        t.setThirdPrize("Third Prize Test");
        assertEquals("Third Prize Test",t.getThirdPrize());

        // test empty values
        t.setThirdPrize("");
        assertEquals("",t.getThirdPrize());
    }

    @Test
    public void tournamentAgeReq(){
        // initial tests with getters and setters
        assertEquals("reqAge",t.getAgeRequirement());
        t.setAgeRequirement("test");
        assertEquals("test",t.getAgeRequirement());

        // test more than 1 word values
        t.setAgeRequirement("Age Requirement Test");
        assertEquals("Age Requirement Test",t.getAgeRequirement());

        // test empty values
        t.setAgeRequirement("");
        assertEquals("",t.getAgeRequirement());
    }

    @Test
    public void tournamentGameMode(){
        // initial tests with getters and setters
        assertEquals("mode",t.getGameMode());
        t.setGameMode("test");
        assertEquals("test",t.getGameMode());

        // test more than 1 word values
        t.setGameMode("Gamemode Test");
        assertEquals("Gamemode Test",t.getGameMode());

        // test empty values
        t.setGameMode("");
        assertEquals("",t.getGameMode());
    }

    @Test
    public void tournamentClubName(){
        // initial tests with getters and setters
        assertEquals("clubName",t.getClubName());
        t.setClubName("test");
        assertEquals("test",t.getClubName());

        // test more than 1 word values
        t.setClubName("Club Name Test");
        assertEquals("Club Name Test",t.getClubName());

        // test empty values
        t.setClubName("");
        assertEquals("",t.getClubName());
    }

    @Test
    public void tournamentDate(){
        // initial tests with getters and setters
        assertEquals("date",t.getDate());
        t.setDate("test");
        assertEquals("test",t.getDate());

        // test more than 1 word values
        t.setDate("Date Test");
        assertEquals("Date Test",t.getDate());

        // test empty values
        t.setDate("");
        assertEquals("",t.getDate());
    }

    @Test
    public void tournamentTime(){
        // initial tests with getters and setters
        assertEquals("time",t.getTime());
        t.setTime("test");
        assertEquals("test",t.getTime());

        // test more than 1 word values
        t.setTime("Time Test");
        assertEquals("Time Test",t.getTime());

        // test empty values
        t.setTime("");
        assertEquals("",t.getTime());
    }

    @Test
    public void tournamentCreator(){
        // initial tests with getters and setters
        assertEquals("creator",t.getCreator());
        t.setCreator("test");
        assertEquals("test",t.getCreator());

        // test more than 1 word values
        t.setCreator("Creator Test");
        assertEquals("Creator Test",t.getCreator());

        // test empty values
        t.setCreator("");
        assertEquals("",t.getCreator());
    }

    @Test
    public void tournamentNumSignedUp(){
        // initial tests with getters and setters
        assertEquals(1,t.getNumSignedUp());
        t.setNumSignedUp(0);
        assertEquals(0,t.getNumSignedUp());
    }
}
