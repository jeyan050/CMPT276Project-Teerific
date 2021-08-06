package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.User;

class UserTest {
    static User u;

    @BeforeAll
    static void setUp(){
        u = new User();
        u.setUsername("username");
        u.setPassword("password");
        u.setFname("first");
        u.setLname("last");
        u.setEmail("email");
        u.setGender("gender");
    }

    @Test
    public void userUsername(){
        // initial tests with getters and setters
        assertEquals("username",u.getUsername());
        u.setUsername("test");
        assertEquals("test",u.getUsername());

        // test more than 1 word values
        u.setUsername("Username Test");
        assertEquals("Username Test",u.getUsername());

        // test empty values
        u.setUsername("");
        assertEquals("",u.getUsername());
    }

    @Test
    public void userPassword(){
        // initial tests with getters and setters
        assertEquals("password",u.getPassword());
        u.setPassword("test");
        assertEquals("test",u.getPassword());

        // test more than 1 word values
        u.setPassword("Password Test");
        assertEquals("Password Test",u.getPassword());

        // test empty values
        u.setPassword("");
        assertEquals("",u.getPassword());
    }

    @Test
    public void userFname(){
        // initial tests with getters and setters
        assertEquals("first",u.getFname());
        u.setFname("test");
        assertEquals("test",u.getFname());

        // test more than 1 word values
        u.setFname("First Name Test");
        assertEquals("First Name Test",u.getFname());

        // test empty values
        u.setFname("");
        assertEquals("",u.getFname());
    }

    @Test
    public void userLname(){
        // initial tests with getters and setters
        assertEquals("last",u.getLname());
        u.setLname("test");
        assertEquals("test",u.getLname());

        // test more than 1 word values
        u.setLname("Last Name Test");
        assertEquals("Last Name Test",u.getLname());

        // test empty values
        u.setLname("");
        assertEquals("",u.getLname());
    }

    @Test
    public void userEmail(){
        // initial tests with getters and setters
        assertEquals("email",u.getEmail());
        u.setEmail("test");
        assertEquals("test",u.getEmail());

        // test more than 1 word values
        u.setEmail("Email Test");
        assertEquals("Email Test",u.getEmail());

        // test empty values
        u.setEmail("");
        assertEquals("",u.getEmail());
    }

    @Test
    public void userGender(){
        // initial tests with getters and setters
        assertEquals("gender",u.getGender());
        u.setGender("test");
        assertEquals("test",u.getGender());

        // test more than 1 word values
        u.setGender("Gender Test");
        assertEquals("Gender Test",u.getGender());

        // test empty values
        u.setGender("");
        assertEquals("",u.getGender());
    }

}
