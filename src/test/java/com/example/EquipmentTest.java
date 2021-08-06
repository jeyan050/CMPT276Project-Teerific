package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.Equipment;

class EquipmentTest {
    static Equipment e;

    @BeforeAll
    static void setUp(){
        e = new Equipment();
        e.setItemName("item");
        e.setStock(4);
    }

    @Test
    public void equipmentItemName(){
        // initial tests with getters and setters
        assertEquals("item",e.getItemName());
        e.setItemName("test");
        assertEquals("test",e.getItemName());

        // test more than 1 word values
        e.setItemName("Item Name Test");
        assertEquals("Item Name Test",e.getItemName());

        // test empty values
        e.setItemName("");
        assertEquals("",e.getItemName());
    }

    @Test
    public void equipmentStock(){
        // initial tests with getters and setters
        assertEquals(4,e.getStock());
        e.setStock(0);
        assertEquals(0,e.getStock());
    }
}
