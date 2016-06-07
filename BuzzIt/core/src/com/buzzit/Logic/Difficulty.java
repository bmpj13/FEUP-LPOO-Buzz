package com.buzzit.logic;


public enum Difficulty {
    EASY(5, 0),
    MEDIUM(10, 1),
    HARD(20, 2);

    private int points;
    private int index;

    /**
     * Constructor
     * @param points Number of points awarded for answering a question of this difficulty
     * @param index Position in array of size Difficulty.values().length
     */
    Difficulty(int points, int index){
        this.points = points;
        this.index = index;
    }

    /**
     * Returns points to award
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns position in array
     * @return index
     */
    public int getIndex(){
        return index;
    }


    /**
     *
     * @param name name of the difficulty
     * @return Difficulty object
     */
    public static Difficulty convert(String name) {
        for (Difficulty difficulty : Difficulty.values()) {
            if (difficulty.toString().equals(name))
                return difficulty;
        }

        return null;
    }
}
