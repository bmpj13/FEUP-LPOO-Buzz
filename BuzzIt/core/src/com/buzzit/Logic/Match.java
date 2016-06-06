package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;

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
     * @param player Player on this match
     */
    public Match(int numQuestions, ArrayList<Category> categoriesChosen, Difficulty difficulty, Player player) {
        this.questions = Play.getInstance().play(numQuestions, categoriesChosen, difficulty);
        this.questionIndex = 0;
        this.totalQuestions = numQuestions;
        this.player = player;
    }

    /**
     * Analyses the given answer and updates Player's points
     * @param answer Answer given by the player
     * @return true if player's answer is the same as correct; false if not
     */
    public boolean isCorrect(String answer) {
        Question question = this.questions.get(questionIndex);
        Gdx.app.log("points", Integer.toString(player.getPoints()));
        if (question.getCorrect().equals(answer)) {
            Gdx.app.log("question", " got yes");
            return true;
        }
        else {
            Gdx.app.log("question", " got no");
            return false;
        }
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
     * Takes points in case the player doesn't answer in time
     */
    public void unanswered(){
        Question question = questions.get(questionIndex);
        player.addPoints(-question.getDifficulty().getPoints());
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