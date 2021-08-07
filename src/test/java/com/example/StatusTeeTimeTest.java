package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.StatusTeeTime;

public class StatusTeeTimeTest {
    static StatusTeeTime st;

    @BeforeAll
    static void setUp(){
        st = new StatusTeeTime();
        st.setStatus("status");
        st.setTeeTime("teeTime");
        st.setTotalPlayers(4);
    }

    @Test
    public void statusTeeTimeStatus(){
        // initial tests with getters and setters
        assertEquals("status",st.getStatus());
        st.setStatus("test");
        assertEquals("test",st.getStatus());

        // test more than 1 word values
        st.setStatus("Status Test");
        assertEquals("Status Test",st.getStatus());

        // test empty values
        st.setStatus("");
        assertEquals("",st.getStatus());
    }

    @Test
    public void statusTeeTimeTeeTime(){
        // initial tests with getters and setters
        assertEquals("teeTime",st.getTeeTime());
        st.setTeeTime("test");
        assertEquals("test",st.getTeeTime());

        // test more than 1 word values
        st.setTeeTime("Tee Time Test");
        assertEquals("Tee Time Test",st.getTeeTime());

        // test empty values
        st.setTeeTime("");
        assertEquals("",st.getTeeTime());
    }

    @Test
    public void statusTeeTimeTotalPlayers(){
        // initial tests with getters and setters
        assertEquals(4,st.getTotalPlayers());
        st.setTotalPlayers(0);
        assertEquals(0,st.getTotalPlayers());
    }
}
