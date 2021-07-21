package com.example.models;

public class TeeTimeBooking {
    private String username;
    private String date; //will be input in dd/MM/yyyy format https://www.w3schools.com/java/java_date.asp
    private int numPlayers;
    private String time;
    private String optionalNote;
    private String rentalID;


    public String getUsername() {
        return this.username;
    }
    public void setUsername(String f) {
        this.username = f;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String f) { this.date = f; }

    public int getNumPlayers() {
        return this.numPlayers;
    }
    public void setNumPlayers(int f) {
        this.numPlayers = f;
    }

    public String getTime() {
        return this.time;
    }
    public void setTime(String f) {
        this.time = f;
    }

    public String getOptionalNote() {
        return this.optionalNote;
    }
    public void setOptionalNote(String f) {
        this.optionalNote = f;
    }

   public String getRentalID() {
       return this.rentalID;
   }

   public void setRentalID(String f) {
       this.rentalID = f;
   }
}
