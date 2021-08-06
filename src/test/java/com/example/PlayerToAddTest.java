package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.PlayerToAdd;

class PlayerToAddTest {
    static PlayerToAdd p;

    @BeforeAll
    static void setUp(){
        p = new PlayerToAdd();
        p.setName("name");
    }

    @Test
    public void playerToAddName(){
        // initial tests with getters and setters
        assertEquals("name",p.getName());
        p.setName("test");
        assertEquals("test",p.getName());

        // test more than 1 word values
        p.setName("Name Test");
        assertEquals("Name Test",p.getName());

        // test empty values
        p.setName("");
        assertEquals("",p.getName());
    }
}
