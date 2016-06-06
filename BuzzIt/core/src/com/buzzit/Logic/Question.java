package com.buzzit.Logic;

import java.util.ArrayList;
import java.util.Random;


public class Question {

    private final int numOptions = 4;
    private String question;
    private ArrayList<String> wrong;
    private String correct;
    private Category category;
    private Difficulty difficulty;

    /**
     * Constructor
     * @param question Question to be asked
     * @param wrong Array of wrong options
     * @param correct Correct answer
     * @param difficulty Difficulty of the question
     * @param category Category of the question
     */
    public Question(String question, ArrayList<String> wrong, String correct,
                    Difficulty difficulty, Category category){
        this.question = question;
        this.wrong = wrong;
        this.correct = correct;
        this.difficulty = difficulty;
        this.category = category;
    }

    /**
     * Generates an array of size numOptions in which one of them is the correct variable and the rest belong to the wrong ArrayList
     * @return Array of string
     */
    public String[] generateOptions() {

        Random rand = new Random();
        String[] options = new String[numOptions];

        ArrayList<Integer> indexes = Play.scramble(wrong.size());

        int correctAnswerIndex = rand.nextInt(numOptions);
        options[correctAnswerIndex] = correct;

        for (int i = 0; i < numOptions ; i++) {
            if (i != correctAnswerIndex) {
                options[i] = wrong.get(indexes.get(i));
            }
        }

        return options;
    }

    /**
     * @return Correct answer
     */
    public String getCorrect() {
        return correct;
    }

    /**
     *
     * @return Returns Difficulty enum of question
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     *
     * @return Returns Category enum of question
     */
    public Category getCategory() {
        return category;
    }

    /**
     *
     * @return ArrayList of the wrong options
     */
    public ArrayList<String> getWrong() {
        return wrong;
    }

    /**
     *
     * @return Question to be asked
     */
    public String getQuestion() {
        return question;
    }

}
