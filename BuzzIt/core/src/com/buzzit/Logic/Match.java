package com.buzzit.Logic;

import java.util.ArrayList;


public class Match {

    private ArrayList<Question> questions;
    private int questionIndex;
    private int totalQuestions;
    private Player player;

    /**
     * Constructor
     * @param numQuestions Number of questions for this match
     * @param categoriesChosen Categories that will be displayed
     * @param difficulty Difficulty of questions
     */
    public Match(int numQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty) {
        this.questions = Play.getInstance().play(numQuestions, categoriesChosen, difficulty);
        this.questionIndex = 0;
        this.totalQuestions = numQuestions;
        player = new Player("UEUEUEUEUEUEUEUEUEUEUEUEUEUE");
    }

    /**
     * Analyses the given answer and updates Player's points
     * @param answer Answer given by the player
     * @return true if player's answer is the same as correct; false if not
     */
    public boolean isCorrect(String answer) {
        Question questions = this.questions.get(questionIndex);

        if (questions.getCorrect().equals(answer))
            return true;
        else
            return false;
    }

    /**
     * Gets current question
     * @return Question from ArrayList
     */
    public Question getCurrentQuestion(){return questions.get(questionIndex);}

    /**
     * Sets index to next Question
     */
    public void nextQuestion(){
        questionIndex++;
    }

    /**
     *
     * @return Returns all questions in ArrayList
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     *
     * @return Returns total number of questions for this match
     */
    public int getTotalQuestions(){
        return totalQuestions;
    }

    /**
     *
     * @return Returns how many questions were answered until now
     */
    public int getQuestionIndex(){
        return questionIndex;
    }

    /**
     *
     * @return Player
     */
    public Player getPlayer() { return player; }
}