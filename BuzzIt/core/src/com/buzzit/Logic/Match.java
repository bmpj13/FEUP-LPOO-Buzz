package com.buzzit.Logic;

import java.util.ArrayList;


public class Match {

    private ArrayList<Question> questions;
    private int questionIndex;
    private Player player;

    public Match(int numQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty){
        this.questions = Play.getInstance().play(numQuestions,categoriesChosen, difficulty);
        this.questionIndex = 0;
        player = new Player("UEUEUEUEUEUEUEUEUEUEUEUEUEUE");
    }

    public boolean isCorrect(String answer){
        Question ques = questions.get(questionIndex);
        if(ques.getCorrect().equals(answer)){
            player.addPoints(ques.getDifficulty().getPoints());
            return true;
        }
        else{
            player.addPoints(-ques.getDifficulty().getPoints());
            return false;
        }
    }

    public Question getCurrentQuestion(){return questions.get(questionIndex);}

    public void nextQuestion(){
        questionIndex++;
    }
}