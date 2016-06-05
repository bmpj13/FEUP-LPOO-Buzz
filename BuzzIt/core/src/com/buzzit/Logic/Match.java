package com.buzzit.Logic;

import java.util.ArrayList;


public class Match {

    private ArrayList<Question> questions;
    private int questionIndex;
    private int totalQuestions;
    private Player player;

    public Match(int numQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty) {
        this.questions = Play.getInstance().play(numQuestions, categoriesChosen, difficulty);
        this.questionIndex = 0;
        this.totalQuestions = numQuestions;
        player = new Player("UEUEUEUEUEUEUEUEUEUEUEUEUEUE");
    }

    public boolean isCorrect(String answer) {
        Question questions = this.questions.get(questionIndex);

        if (questions.getCorrect().equals(answer)){
            player.addPoints(questions.getDifficulty().getPoints());
            return true;
        }
        else {
            player.addPoints(-questions.getDifficulty().getPoints());
            return false;
        }
    }

    public Question getCurrentQuestion(){return questions.get(questionIndex);}

    public void nextQuestion(){
        questionIndex++;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public int getTotalQuestions(){
        return totalQuestions;
    }

    public int getQuestionIndex(){
        return questionIndex;
    }
}