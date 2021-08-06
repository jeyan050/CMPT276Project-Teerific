package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.EquipmentCart;

class EquipmentCartTest {
    static EquipmentCart ec;

    @BeforeAll
    static void setUp(){
        ec = new EquipmentCart();
        ec.setNumBalls(4);
        ec.setNumCarts(4);
        ec.setNumClubs(4);
    }

    @Test
    public void equipmentCartBalls(){
        // initial tests with getters and setters
        assertEquals(4,ec.getNumBalls());
        ec.setNumBalls(0);
        assertEquals(0,ec.getNumBalls());
    }

    @Test
    public void equipmentCartCarts(){
        // initial tests with getters and setters
        assertEquals(4,ec.getNumCarts());
        ec.setNumCarts(0);
        assertEquals(0,ec.getNumCarts());
    }

    @Test
    public void equipmentCartClubs(){
        // initial tests with getters and setters
        assertEquals(4,ec.getNumClubs());
        ec.setNumClubs(0);
        assertEquals(0,ec.getNumClubs());
    }
}
