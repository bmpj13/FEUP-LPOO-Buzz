package com.buzzit.Logic;


public enum Difficulty {
    EASY(5),
    MEDIUM(10),
    HARD(20);

    private int points;

    Difficulty(int points){
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
