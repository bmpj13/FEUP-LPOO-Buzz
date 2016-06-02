package com.buzzit.GUI.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.buzzit.GUI.Interactor;


public class WaitingAnswer implements GameStrategy {
    private Interactor interactor;

    private int secondsLeft;
    private final int secondsToAnswer;
    private boolean finished;
    private Timer.Task timerTask;

    public WaitingAnswer(final Interactor interactor, int secondsToAnswer) {
        this.interactor = interactor;
        this.secondsToAnswer = secondsToAnswer;
        this.secondsLeft = secondsToAnswer;
        this.finished = false;

        timerTask = new Timer.Task() {
            @Override
            public void run() {
                interactor.labelStatus.setText(Integer.toString(secondsLeft));
                secondsLeft--;

                if (secondsLeft == -1)
                    finished = true;

            }
        };
    }

    @Override
    public void start() {
        interactor.enableButtons();
        Timer.schedule(timerTask, 0, 1, secondsToAnswer);
    }

    @Override
    public boolean finished() {
        return finished;
    }

    @Override
    public void render() {

    }

    @Override
    public void finish() {
        timerTask.cancel();
        finished = true;
    }
}
