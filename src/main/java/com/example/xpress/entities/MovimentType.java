package com.example.xpress.entities;

public enum MovimentType {
    INCOMING("incoming"),
    OUTGOING("outgoing");

    private String moviment;


    MovimentType(String moviment) {
        this.moviment = moviment;
    }

    public String getMoviment(){
        return moviment;
    }
}
