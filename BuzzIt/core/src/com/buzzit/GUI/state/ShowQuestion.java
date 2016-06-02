package com.buzzit.GUI.state;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.buzzit.GUI.Interactor;


public class ShowQuestion implements GameStrategy {
    private float timerTicksCount;
    private Interactor interactor;


    private final float delay;
    private float delta;
    private final float duration;
    private final int secondsToAnswer;
    private boolean finished;
    private Timer.Task timerTask;


    /**
     *
     * @param delay time before starting animation (seconds)
     * @param delta time between the animation of different elements (seconds)
     * @param duration animation's duration (seconds)
     * @param secondsToAnswer number to display on status label
     * @param interactor holds the multiple widgets being used
     */
    public ShowQuestion (final Interactor interactor, float delay, float delta, final float duration, final int secondsToAnswer) {

        this.timerTicksCount = 0;
        this.interactor = interactor;
        this.interactor.uncheckButtons();

        this.delay = delay;
        this.delta = delta;
        this.duration = duration;
        this.secondsToAnswer = secondsToAnswer;
        this.finished = false;


        timerTask = new Timer.Task() {
            @Override
            public void run() {
                if (timerTicksCount == 0) {
                    interactor.hideElements();
                    interactor.labelStatus.setText(Integer.toString(secondsToAnswer));
                    interactor.labelPoints.addAction(Actions.fadeIn(duration / 2));
                    interactor.labelCategory.addAction(Actions.fadeIn(duration));
                }
                else if (timerTicksCount == 1) {
                    interactor.labelQuestion.addAction(Actions.fadeIn(duration));
                }
                else if (timerTicksCount == 2) {
                    interactor.btnOptionA.addAction(Actions.fadeIn(duration));
                    interactor.btnOptionB.addAction(Actions.fadeIn(duration));
                    interactor.btnOptionC.addAction(Actions.fadeIn(duration));
                    interactor.btnOptionD.addAction(Actions.fadeIn(duration));
                    interactor.labelStatus.addAction(Actions.fadeIn(duration));
                }
                else if (timerTicksCount == 3)
                    finished = true;

                timerTicksCount++;
            }
        };
    }

    @Override
    public void start() {
        interactor.disableButtons();
        Timer.schedule(timerTask, delay, delta, 3);
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
