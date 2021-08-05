package com.example.models;

import java.util.ArrayList;

public class WrapperTournamentParticipants{
    private ArrayList<TournamentParticipant> participants = new ArrayList<TournamentParticipant>();

    public ArrayList<TournamentParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<TournamentParticipant> participants)
    {
        this.participants = participants;
    }
}