package com.buzzit.Logic;

/**
 * Created by wnfuk_000 on 01/06/2016.
 */
public enum Difficulty {
    EASY(5),
    MEDIUM(10),
    HARD(20);

    private int points;

    private Difficulty(int points){
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
