package com.buzzit.Logic;

import java.util.ArrayList;

/**
 * Created by wnfuk_000 on 18/05/2016.
 */
public class Match {

    private ArrayList<Question> questions;
    private int rounds;
    private int questionIndex;
    private Player[] players;

    public Match(ArrayList<Question> questions, int rounds){
        this.questions = questions;
        this.rounds = rounds;
        this.questionIndex = 0;
        players = new Player[1];
    }

    public String[] generateOptions(){
        String[] options = questions.get(questionIndex).generateOptions(4);

        return options;
    }

    public boolean isCorrect(String answer){
        if(questions.get(questionIndex++).correct == answer){
            players[0].setPoints(5);
            return true;
        }
        else{
            players[0].setPoints(-5);
            return true;
        }
    }
}
