package com.buzzit.Logic;


public class Player implements Comparable {
    private String name;
    private int points;

    /**
     * Constructor
     * @param name name of the player
     */
    public Player(String name) {
        this.name = name;
        this.points = 0;
    }

    /**
     *
     * @return returns points the player has
     */
    public int getPoints() {
        return points;
    }

    /**
     * Replaces points of player
     * @param points New points
     */
    public void setPoints(int points){
        this.points = points;
    }

    /**
     * Adds or decreases points for answering question
     * @param points Points to add
     */
    public void addPoints (int points) {
        this.points += points;
    }


    /**
     *
     * @return Returns name of the player
     */
    public String getName() {
        return name;
    }


    @Override
    public int compareTo(Object object) {
        Player other = (Player) object;
        return this.points - other.points;
    }
}
