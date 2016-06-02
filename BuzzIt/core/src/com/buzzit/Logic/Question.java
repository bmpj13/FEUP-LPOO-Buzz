package com.buzzit.Logic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wnfuk_000 on 20/04/2016.
 */
public class Question {
    private String question;
    private ArrayList<String> wrong;
    private String correct;
    private String category;
    private Difficulty difficulty;

    public Question(String question, ArrayList<String> wrong, String correct, Difficulty difficulty){
        this.question = question;
        this.wrong = wrong;
        this.correct = correct;
        this.difficulty = difficulty;
    }


    @SuppressWarnings("unchecked")
    public String[] generateOptions(int num) {

        if (num > wrong.size() + 1)
            throw new IllegalArgumentException("Not enough answers");

        Random rand = new Random();
        String[] options = new String[num];

        ArrayList<Integer> indices = Play.scramble(wrong.size());
        int perin = rand.nextInt(num);

        for (int i = 0; i < num ; i++) {
            if (i == perin)
                options[i] = correct;
            else
            {
                options[i] = wrong.get(indices.get(i));
            }
        }

        return options;
    }



    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getWrong() {
        return wrong;
    }

    public void setWrong(ArrayList<String> wrong) {
        this.wrong = wrong;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
