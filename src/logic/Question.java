package logic;

import java.util.ArrayList;

/**
 * Created by wnfuk_000 on 20/04/2016.
 */
public class Question {
    public String question;
    public ArrayList<String> wrong;
    public String correct;

    Question(String question, ArrayList<String> wrong, String correct){
        this.question = question;
        this.wrong = wrong;
        this.correct = correct;
    }

}
