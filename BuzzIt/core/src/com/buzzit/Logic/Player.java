package com.buzzit.Logic;


public class Player {
    private String name;
    private int points;
    private int id;
    public static int numPlayers = 0;

    public Player(String name) {
        this.name = name;
        this.id = ++numPlayers;
        this.points = 0;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints (int points) {
        this.points += points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id= id;
    }

}
