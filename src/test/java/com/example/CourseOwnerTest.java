package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import com.example.models.CourseOwner;
import com.example.models.Hole;
import java.io.*;
import java.util.*;

class CourseOwnerTest {
    static CourseOwner co;

    @BeforeAll
    static void setUp(){
        co = new CourseOwner();
        co.setUsername("username");
        co.setPassword("password");
        co.setFname("first");
        co.setLname("last");
        co.setEmail("email");
        co.setGender("gender");

        co.setAddress("address");
        co.setCity("city");
        co.setCountry("country");
        co.setPhoneNumber("phoneNumber");
        co.setTimeOpen("timeOpen");
        co.setTimeClose("timeClose");
        co.setBookingInterval("interval");
        co.setWebsite("website");
        co.setNumHoles(18);

        ArrayList<Hole> initialAL = new ArrayList<Hole>();
        co.setHoles(initialAL);

        co.setYardage("yardage");
        co.setCourseName("courseName");
        co.setCourseLogo("logo");
        co.setDirectionsToCourse("directions");
        co.setDescription("description");
        co.setWeekdayRates("wdRates");
        co.setWeekendRates("weRates");
        co.setRating(5.0);
        co.setNumberRatings(5.0);
        co.setFeedback("feedback");
    }

    @Test
    public void ownerUsername(){
        // initial tests with getters and setters
        assertEquals("username",co.getUsername());
        co.setUsername("test");
        assertEquals("test",co.getUsername());

        // test more than 1 word values
        co.setUsername("Username Test");
        assertEquals("Username Test",co.getUsername());

        // test empty values
        co.setUsername("");
        assertEquals("",co.getUsername());
    }

    @Test
    public void ownerPassword(){
        // initial tests with getters and setters
        assertEquals("password",co.getPassword());
        co.setPassword("test");
        assertEquals("test",co.getPassword());

        // test more than 1 word values
        co.setPassword("Password Test");
        assertEquals("Password Test",co.getPassword());

        // test empty values
        co.setPassword("");
        assertEquals("",co.getPassword());
    }

    @Test
    public void ownerFname(){
        // initial tests with getters and setters
        assertEquals("first",co.getFname());
        co.setFname("test");
        assertEquals("test",co.getFname());

        // test more than 1 word values
        co.setFname("First Name Test");
        assertEquals("First Name Test",co.getFname());

        // test empty values
        co.setFname("");
        assertEquals("",co.getFname());
    }

    @Test
    public void ownerLname(){
        // initial tests with getters and setters
        assertEquals("last",co.getLname());
        co.setLname("test");
        assertEquals("test",co.getLname());

        // test more than 1 word values
        co.setLname("Last Name Test");
        assertEquals("Last Name Test",co.getLname());

        // test empty values
        co.setLname("");
        assertEquals("",co.getLname());
    }

    @Test
    public void ownerEmail(){
        // initial tests with getters and setters
        assertEquals("email",co.getEmail());
        co.setEmail("test");
        assertEquals("test",co.getEmail());

        // test more than 1 word values
        co.setEmail("Email Test");
        assertEquals("Email Test",co.getEmail());

        // test empty values
        co.setEmail("");
        assertEquals("",co.getEmail());
    }

    @Test
    public void ownerGender(){
        // initial tests with getters and setters
        assertEquals("gender",co.getGender());
        co.setGender("test");
        assertEquals("test",co.getGender());

        // test more than 1 word values
        co.setGender("Gender Test");
        assertEquals("Gender Test",co.getGender());

        // test empty values
        co.setGender("");
        assertEquals("",co.getGender());
    }

    @Test
    public void ownerAddress(){
        // initial tests with getters and setters
        assertEquals("address",co.getAddress());
        co.setAddress("test");
        assertEquals("test",co.getAddress());

        // test more than 1 word values
        co.setAddress("Address Test");
        assertEquals("Address Test",co.getAddress());

        // test empty values
        co.setAddress("");
        assertEquals("",co.getAddress());
    }

    @Test
    public void ownerCity(){
        // initial tests with getters and setters
        assertEquals("city",co.getCity());
        co.setCity("test");
        assertEquals("test",co.getCity());

        // test more than 1 word values
        co.setCity("City Test");
        assertEquals("City Test",co.getCity());

        // test empty values
        co.setCity("");
        assertEquals("",co.getCity());
    }

    @Test
    public void ownerCountry(){
        // initial tests with getters and setters
        assertEquals("country",co.getCountry());
        co.setCountry("test");
        assertEquals("test",co.getCountry());

        // test more than 1 word values
        co.setCountry("Country Test");
        assertEquals("Country Test",co.getCountry());

        // test empty values
        co.setCountry("");
        assertEquals("",co.getCountry());
    }

    @Test
    public void ownerPhoneNumber(){
        // initial tests with getters and setters
        assertEquals("phoneNumber",co.getPhoneNumber());
        co.setPhoneNumber("test");
        assertEquals("test",co.getPhoneNumber());

        // test more than 1 word values
        co.setPhoneNumber("Phone Number Test");
        assertEquals("Phone Number Test",co.getPhoneNumber());

        // test empty values
        co.setPhoneNumber("");
        assertEquals("",co.getPhoneNumber());
    }

    @Test
    public void ownerTimeOpen(){
        // initial tests with getters and setters
        assertEquals("timeOpen",co.getTimeOpen());
        co.setTimeOpen("test");
        assertEquals("test",co.getTimeOpen());

        // test more than 1 word values
        co.setTimeOpen("Time Open Test");
        assertEquals("Time Open Test",co.getTimeOpen());

        // test empty values
        co.setTimeOpen("");
        assertEquals("",co.getTimeOpen());
    }

    @Test
    public void ownerTimeClose(){
        // initial tests with getters and setters
        assertEquals("timeClose",co.getTimeClose());
        co.setTimeClose("test");
        assertEquals("test",co.getTimeClose());

        // test more than 1 word values
        co.setTimeClose("Time Close Test");
        assertEquals("Time Close Test",co.getTimeClose());

        // test empty values
        co.setTimeClose("");
        assertEquals("",co.getTimeClose());
    }

    @Test
    public void ownerBookingInterval(){
        // initial tests with getters and setters
        assertEquals("interval",co.getBookingInterval());
        co.setBookingInterval("test");
        assertEquals("test",co.getBookingInterval());

        // test more than 1 word values
        co.setBookingInterval("Booking Interval Test");
        assertEquals("Booking Interval Test",co.getBookingInterval());

        // test empty values
        co.setBookingInterval("");
        assertEquals("",co.getBookingInterval());
    }

    @Test
    public void ownerWebsite(){
        // initial tests with getters and setters
        assertEquals("website",co.getWebsite());
        co.setWebsite("test");
        assertEquals("test",co.getWebsite());

        // test more than 1 word values
        co.setWebsite("Website Test");
        assertEquals("Website Test",co.getWebsite());

        // test empty values
        co.setWebsite("");
        assertEquals("",co.getWebsite());
    }

    @Test
    public void ownerNumHoles(){
        // initial tests with getters and setters
        assertEquals(18,co.getNumHoles());
        co.setNumHoles(0);
        assertEquals(0,co.getNumHoles());
    }

    @Test
    public void ownerHoles(){
        // detect if empty
        assertEquals(co.getHoles().size(), 0);

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
        co.setHoles(holesAL);
        assertEquals(holesAL,co.getHoles());

        // test empty arrayList
        ArrayList<Hole> emptyHolesAL = new ArrayList<Hole>();
        co.setHoles(emptyHolesAL);
        assertEquals(emptyHolesAL,co.getHoles());
    }

    @Test
    public void ownerYardage(){
        // initial tests with getters and setters
        assertEquals("yardage",co.getYardage());
        co.setYardage("test");
        assertEquals("test",co.getYardage());

        // test more than 1 word values
        co.setYardage("Yardage Test");
        assertEquals("Yardage Test",co.getYardage());

        // test empty values
        co.setYardage("");
        assertEquals("",co.getYardage());
    }

    @Test
    public void ownerCourseName(){
        // initial tests with getters and setters
        assertEquals("courseName",co.getCourseName());
        co.setCourseName("test");
        assertEquals("test",co.getCourseName());

        // test more than 1 word values
        co.setCourseName("Course Name Test");
        assertEquals("Course Name Test",co.getCourseName());

        // test empty values
        co.setCourseName("");
        assertEquals("",co.getCourseName());
    }

    @Test
    public void ownerCourseLogo(){
        // initial tests with getters and setters
        assertEquals("logo",co.getCourseLogo());
        co.setCourseLogo("test");
        assertEquals("test",co.getCourseLogo());

        // test more than 1 word values
        co.setCourseLogo("Course Logo Test");
        assertEquals("Course Logo Test",co.getCourseLogo());

        // test empty values
        co.setCourseLogo("");
        assertEquals("",co.getCourseLogo());
    }

    @Test
    public void ownerDirectionsToCourse(){
        // initial tests with getters and setters
        assertEquals("directions",co.getDirectionsToCourse());
        co.setDirectionsToCourse("test");
        assertEquals("test",co.getDirectionsToCourse());

        // test more than 1 word values
        co.setDirectionsToCourse("Directions to Course Test");
        assertEquals("Directions to Course Test",co.getDirectionsToCourse());

        // test empty values
        co.setDirectionsToCourse("");
        assertEquals("",co.getDirectionsToCourse());
    }

    @Test
    public void ownerDescription(){
        // initial tests with getters and setters
        assertEquals("description",co.getDescription());
        co.setDescription("test");
        assertEquals("test",co.getDescription());

        // test more than 1 word values
        co.setDescription("Description Test");
        assertEquals("Description Test",co.getDescription());

        // test empty values
        co.setDescription("");
        assertEquals("",co.getDescription());
    }

    @Test
    public void ownerWeekdayRates(){
        // initial tests with getters and setters
        assertEquals("wdRates",co.getWeekdayRates());
        co.setWeekdayRates("test");
        assertEquals("test",co.getWeekdayRates());

        // test more than 1 word values
        co.setWeekdayRates("Weekday Rates Test");
        assertEquals("Weekday Rates Test",co.getWeekdayRates());

        // test empty values
        co.setWeekdayRates("");
        assertEquals("",co.getWeekdayRates());
    }

    @Test
    public void ownerWeekendRates(){
        // initial tests with getters and setters
        assertEquals("weRates",co.getWeekendRates());
        co.setWeekendRates("test");
        assertEquals("test",co.getWeekendRates());

        // test more than 1 word values
        co.setWeekendRates("Weekend Rates Test");
        assertEquals("Weekend Rates Test",co.getWeekendRates());

        // test empty values
        co.setWeekendRates("");
        assertEquals("",co.getWeekendRates());
    }

    @Test
    public void ownerRatings(){
        // initial tests with getters and setters
        assertEquals(5.0,co.getRating());
        co.setRating(0);
        assertEquals(0,co.getRating());
    }

    @Test
    public void ownerNumRatings(){
        // initial tests with getters and setters
        assertEquals(5.0,co.getNumberRatings());
        co.setNumberRatings(0);
        assertEquals(0,co.getNumberRatings());
    }

    @Test
    public void ownerFeedback(){
        // initial tests with getters and setters
        assertEquals("feedback",co.getFeedback());
        co.setFeedback("test");
        assertEquals("test",co.getFeedback());

        // test more than 1 word values
        co.setFeedback("Feedback Test");
        assertEquals("Feedback Test",co.getFeedback());

        // test empty values
        co.setFeedback("");
        assertEquals("",co.getFeedback());
    }
}
