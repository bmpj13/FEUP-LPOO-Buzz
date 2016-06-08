package com.buzzit.gui.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.buzzit.gui.Interactor;

public class Answered extends Decision implements com.buzzit.gui.state.GameStrategy {
    private Interactor interactor;
    private com.buzzit.gui.OptionButton button;
    private int timerTicksCount;
    private final int numberAnimations;
    private Color color;
    private Timer.Task timerTask;

    private final float delay;
    private final float delta;
    private final Color finalColor;
    private boolean finished;

    public Answered(final Interactor interactor, com.buzzit.gui.OptionButton button, int pointsToAdd, boolean answeredCorrectly) {
        super(interactor, pointsToAdd);

        this.interactor = interactor;
        this.button = button;
        this.timerTicksCount = 0;
        this.delay = 0;                     // Default value
        this.delta = 0.7f;                  // Default value
        this.numberAnimations = 4;          // Default value
        this.color = interactor.RightColor;
        this.finished = false;
        this.timerTask = answeredTask();

        if (answeredCorrectly) finalColor = interactor.RightColor;
        else                   finalColor = interactor.WrongColor;
    }


    @Override
    public void start() {
        interactor.disableButtons();
        Timer.schedule(timerTask, delay, delta, numberAnimations);
    }



    @Override
    public void render() {
        super.render();
    }

    @Override
    public void finish() {
        timerTask.cancel();
        finished = true;
    }

    @Override
    public boolean finished() {
        return this.finished && super.finished();
    }


    private Timer.Task answeredTask() {

        return new Timer.Task() {
            @Override
            public void run() {
                button.getButtonStyle().checked = interactor.skin.newDrawable("btn_background", color);
                switchColor();

                if (timerTicksCount == numberAnimations) {
                    finished = true;
                    Answered.super.start();
                    button.getButtonStyle().checked = interactor.skin.newDrawable("btn_background", finalColor);
                }

                timerTicksCount++;
            }

            private void switchColor() {
                if (color == interactor.RightColor)
                    color = button.getCheckedColor();
                else
                    color = interactor.RightColor;
            }
        };
    }
}
