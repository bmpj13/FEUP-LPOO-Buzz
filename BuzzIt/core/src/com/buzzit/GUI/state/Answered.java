package com.buzzit.GUI.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.buzzit.GUI.Interactor;

public class Answered extends Decision implements GameStrategy {
    private Interactor interactor;
    private int timerTicksCount;
    private final int numberAnimations;
    private Color color;
    private Timer.Task timerTask;

    private final float delay;
    private final float delta;
    private final Color finalColor;
    private boolean finished;

    public Answered(final Interactor interactor, int pointsToAdd, boolean answeredCorrectly) {
        super(interactor, pointsToAdd);

        this.interactor = interactor;
        this.timerTicksCount = 0;
        this.delay = 0;                     // Default value
        this.delta = 0.7f;                  // Default value
        this.numberAnimations = 4;          // Default value
        this.color = Color.GREEN;
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
                interactor.textButtonStyle.checked = interactor.skin.newDrawable("btn_background", color);
                switchColor();

                if (timerTicksCount == numberAnimations) {
                    finished = true;
                    Answered.super.start();
                    interactor.textButtonStyle.checked = interactor.skin.newDrawable("btn_background", finalColor);
                }

                timerTicksCount++;
            }

            private void switchColor() {
                if (color == Color.DARK_GRAY)
                    color = interactor.RightColor;
                else
                    color = color.DARK_GRAY;
            }
        };
    }
}
