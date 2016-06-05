package com.buzzit.Logic;


public enum Difficulty {
    EASY(5, 0),
    MEDIUM(10, 1),
    HARD(20, 2);

    private int points;
    private int index;

    Difficulty(int points, int index){
        this.points = points;
        this.index = index;
    }

    public int getPoints() {
        return points;
    }

    public int getIndex(){
        return index;
    }
}
