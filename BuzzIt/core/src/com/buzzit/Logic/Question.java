package com.buzzit.Logic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wnfuk_000 on 20/04/2016.
 */
public class Question {
    public String question;
    public ArrayList<String> wrong;
    public String correct;
    public String category;
    public Difficulty difficulty;

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

}
