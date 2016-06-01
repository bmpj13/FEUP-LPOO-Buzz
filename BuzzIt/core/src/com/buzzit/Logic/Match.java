package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;


public class Match {

    private ArrayList<Question> questions;
    private int rounds;
    private int questionIndex;
    private Player[] players;

    public Match(ArrayList<Question> questions, int rounds, int numPlayers){
        this.questions = questions;
        this.rounds = rounds;
        this.questionIndex = 0;
        players = new Player[numPlayers];
    }

    public String[] generateOptions(){
        String[] options = questions.get(questionIndex).generateOptions(4);
        Gdx.app.log("UEUEUEUEUEUEUEUE - QUESTION", questions.get(questionIndex).question);
        for(int i = 0; i < options.length; i++){
            Gdx.app.log("UEUEUEUEUEUEUEUE - OPTIONS", options[i]);
        }
        return options;
    }

    public boolean isCorrect(int playerIndex, String answer){
        if(questions.get(questionIndex).correct == answer){
            players[playerIndex].addPoints(questions.get(questionIndex).difficulty.getPoints());
            return true;
        }
        else{
            players[playerIndex].setPoints(-questions.get(questionIndex).difficulty.getPoints());
            return true;
        }
    }

    public void nextQuestion(){
        questionIndex++;
    }
}
