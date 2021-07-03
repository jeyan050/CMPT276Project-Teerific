package com.example;

public class User {
    
    private String priorityLevel;
    private String username;
    private String password;
    private String fname;
    private String lname;
    private String email;
    private String gender;

    public String getPriority(){
        return this.priorityLevel;
    }

    public void setPriority(String priorityLevel){
        this.priorityLevel = priorityLevel;
    }

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


    public String getGender() {
        return this.gender;
    }
    public void setGender(String f) {
        this.gender = f;
    }
}
