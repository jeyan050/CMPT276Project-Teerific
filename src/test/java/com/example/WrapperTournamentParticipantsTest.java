package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.WrapperTournamentParticipants;
import com.example.models.TournamentParticipant;
import java.io.*;
import java.util.*;

class WrapperTournamentParticipantsTest {
    static WrapperTournamentParticipants wtp;

    @BeforeAll
    static void setUp(){
        wtp = new WrapperTournamentParticipants();
        ArrayList<TournamentParticipant> initialAL = new ArrayList<TournamentParticipant>();
        wtp.setParticipants(initialAL);
    }

    @Test
    public void wrapperHolesHoles(){
        // detect if empty
        assertEquals(wtp.getParticipants().size(), 0);

        // initial tests with getters and setters
        ArrayList<TournamentParticipant> tournPartAL = new ArrayList<TournamentParticipant>();
        for(Integer h = 0; h < 2; h++){
            TournamentParticipant tempTP = new TournamentParticipant();

            String hString = "test:" + h;
            tempTP.setId(h);
            tempTP.setUsername(hString);
            tempTP.setFname(hString);
            tempTP.setLname(hString);
            tempTP.setScore(h);

            tournPartAL.add(tempTP);
        }
        wtp.setParticipants(tournPartAL);
        assertEquals(tournPartAL,wtp.getParticipants());

        // test empty arrayList
        ArrayList<TournamentParticipant> emptyTournPartAL = new ArrayList<TournamentParticipant>();
        wtp.setParticipants(emptyTournPartAL);
        assertEquals(emptyTournPartAL,wtp.getParticipants());
    }
}
