package com.buzzit.GUI.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.buzzit.GUI.Interactor;

public class Decision implements GameStrategy {
    protected Interactor interactor;
    private Stage stage;
    private Label LabelPointsToAdd;
    private int timerTicksCount;
    private int pointsToAdd;
    private boolean started;
    private boolean finished;
    private int numberToDisplay;                // Variable used on updatePointsAnimation
    private Timer.Task timerTask;

    private final float MOVE_ANIMATION_TIME = 0.8f;
    private final float DELAY = 0.8f;

    public Decision(final Interactor interactor, final int pointsToAdd) {
        this.interactor = interactor;
        timerTicksCount = 0;

        this.pointsToAdd = pointsToAdd;
        started = false;
        finished = false;

        LabelPointsToAdd = new Label (Integer.toString(pointsToAdd), interactor.skin);
        LabelPointsToAdd.setFontScale(interactor.LABEL_POINTS_FSCALE);
        LabelPointsToAdd.setPosition(interactor.labelQuestion.getX(),
                interactor.labelPoints.getY(Align.center));

        stage = new Stage();
        stage.addActor(LabelPointsToAdd);

        timerTask = pointAnimationTask();
    }


    @Override
    public void start() {
        started = true;

        LabelPointsToAdd.addAction(Actions.alpha(0));
        LabelPointsToAdd.addAction(Actions.fadeIn(DELAY));

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
                    LabelPointsToAdd.addAction(Actions.moveTo(interactor.labelPoints.getX(),
                            interactor.labelPoints.getY(Align.center), MOVE_ANIMATION_TIME));

                    timerTicksCount++;
                }
                else if (timerTicksCount == 1) {
                    LabelPointsToAdd.addAction(Actions.alpha(0));
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

        if (increment > 0)  interactor.labelPoints.setColor(Color.GREEN);
        else                interactor.labelPoints.setColor(Color.RED);

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
