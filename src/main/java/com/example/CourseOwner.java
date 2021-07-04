package com.example;

import java.awt.*;
import java.util.ArrayList;

public class CourseOwner {

    private String username;
    private String password;
    private String fname;
    private String lname;
    private String email;
    private String address;
    private String city;
    private String country;
    private String phoneNumber;
    private String website;
    private int numHoles;
    private ArrayList<Hole> holes;
    private String yardage;
    private String courseName;
    private String courseLogo;          //need to figure this out later
    private String directionsToCourse;
    private String description;
    private String weekdayRates;
    private String weekendRates;
    private String gender;


    public String getUsername() {
        return this.username;
    }
    public void setUsername(String f) {
        this.username = f;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String f) {
        this.password = f;
    }

    public String getFname() {
        return this.fname;
    }
    public void setFname(String f) {
        this.fname = f;
    }


    public String getLname() {
        return this.lname;
    }
    public void setLname(String f) {
        this.lname = f;
    }


    public String getEmail() {
        return this.email;
    }
    public void setEmail(String f) {
        this.email = f;
    }

    public String getAddress() {
        return this.address;
    }
    public void setAddress(String f) {
        this.address = f;
    }

    public String getCity(){
        return this.city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getCountry(){
        return this.country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String f) {
        this.phoneNumber = f;
    }

    public String getWebsite() {
        return this.website;
    }
    public void setWebsite(String f) {
        this.website = f;
    }

    public int getNumHoles() {
        return this.numHoles;
    }
    public void setNumHoles(int f) {
        this.numHoles = f;
    }

    public ArrayList<Hole> getHoles() {
        return this.holes;
    }

    public void setHoles(ArrayList<Hole> listOfUpdatedHoles){
        this.holes = listOfUpdatedHoles;
    }

    public String getYardage() {
        return this.yardage;
    }
    public void setYardage(String f) {
        this.yardage = f;
    }

    public String getCourseName() {
        return this.courseName;
    }
    public void setCourseName(String f) {
        this.courseName = f;
    }

    public String getCourseLogo() {                 //Will need to figure out how to store an image in SQL, then this can be changed to an image
        return this.courseLogo;
    }
    public void setCourseLogo(String f) {           //Will need to figure out how to store an image in SQL, then this can be changed to an image
        this.courseLogo = f;
    }

    public String getDirectionsToCourse() {
        return this.directionsToCourse;
    }
    public void setDirectionsToCourse(String f) {
        this.directionsToCourse = f;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String f) {
        this.description = f;
    }

    public String getWeekdayRates() {
        return this.weekdayRates;
    }
    public void setWeekdayRates(String f) {
        this.weekdayRates = f;
    }

    public String getWeekendRates() {
        return this.weekendRates;
    }
    public void setWeekendRates(String f) {
        this.weekendRates = f;
    }

    public String getGender(){
        return this.gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

}
