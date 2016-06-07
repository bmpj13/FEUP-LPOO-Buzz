package com.buzzit.GUI.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.buzzit.GUI.Interactor;

public class Decision implements GameStrategy {
    protected Interactor interactor;
    private Stage stage;
    private Label labelPointsToAdd;
    private int timerTicksCount;
    private int pointsToAdd;
    private boolean started;
    private boolean finished;
    private int numberToDisplay;                // Variable used on updatePointsAnimation
    private Timer.Task timerTask;

    final int WIDTH = 1080;
    final int HEIGHT = 1920;
    private final float MOVE_ANIMATION_TIME = 0.8f;
    private final float DELAY = 0.8f;

    public Decision(final Interactor interactor, final int pointsToAdd) {
        this.interactor = interactor;
        timerTicksCount = 0;

        this.pointsToAdd = pointsToAdd;

        started = false;
        finished = false;

        labelPointsToAdd = new Label (Integer.toString(pointsToAdd), interactor.skin, "numbers");
        labelPointsToAdd.setPosition(interactor.labelQuestion.getX(), interactor.labelPoints.getY());


        stage = new Stage(new FillViewport(WIDTH, HEIGHT));
        stage.addActor(labelPointsToAdd);

        timerTask = pointAnimationTask();
    }


    @Override
    public void start() {
        started = true;

        labelPointsToAdd.addAction(Actions.alpha(0));
        labelPointsToAdd.addAction(Actions.fadeIn(DELAY));

        Timer.schedule(timerTask, DELAY, MOVE_ANIMATION_TIME, 2);
    }

    @Override
    public boolean finished() {
        return finished;
    }

    @Override
    public void render() {

        if (started) {
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void finish() {
        timerTask.cancel();
        finished = true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stage.dispose();
    }


    private Timer.Task pointAnimationTask() {
        return new Timer.Task() {
            @Override
            public void run() {

                if (timerTicksCount == 0) {
                    labelPointsToAdd.addAction(Actions.moveTo(interactor.labelPoints.getX(),
                            interactor.labelPoints.getY(), MOVE_ANIMATION_TIME));

                    timerTicksCount++;
                }
                else if (timerTicksCount == 1) {
                    labelPointsToAdd.addAction(Actions.alpha(0));
                    updatePointsAnimation(pointsToAdd);
                }
            }
        };
    }


    private void updatePointsAnimation(final int pointsAdded) {
        final float TIMER_TICK = 0.1f;
        timerTicksCount = 0;
        numberToDisplay = Integer.parseInt(interactor.labelPoints.getText().toString());
        timerTask = addPointsTask(pointsAdded);

        Timer.schedule(timerTask, 0, TIMER_TICK, Math.abs(pointsAdded));
    }


    private Timer.Task addPointsTask(final int pointsAdded) {
        final int increment = pointsAdded / Math.abs(pointsAdded);          // Works with positive or negative add of points

        if (increment > 0)  interactor.labelPoints.setColor(interactor.RightColor);
        else                interactor.labelPoints.setColor(interactor.WrongColor);

        return new Timer.Task() {
            @Override
            public void run () {
                if (timerTicksCount == Math.abs(pointsAdded)) {
                    interactor.labelPoints.setColor(interactor.labelPointsStyle.fontColor);
                    finished = true;
                }
                else {
                    numberToDisplay += increment;
                    interactor.labelPoints.setText(Integer.toString(numberToDisplay));
                }

                timerTicksCount++;
            }
        };
    }
}
