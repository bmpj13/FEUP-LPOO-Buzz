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

    public Question(String question, ArrayList<String> wrong, String correct){
        this.question = question;
        this.wrong = wrong;
        this.correct = correct;
    }


    @SuppressWarnings("unchecked")
    public String[] generateOptions(int num) {

        if (num > wrong.size() + 1)
            throw new IllegalArgumentException("Not enough answers");

        Random rand = new Random();
        String[] options = new String[num];
        ArrayList<String> temp = (ArrayList<String>) wrong.clone();


        int correctPos = rand.nextInt(num);

        for (int i = 0; i < num ; i++) {
            if (i == correctPos)
                options[correctPos] = correct;
            else
            {
                int wrongPos = rand.nextInt(temp.size());
                options[i] = temp.get(wrongPos);
                temp.remove(wrongPos);
            }
        }

        return options;
    }

}
