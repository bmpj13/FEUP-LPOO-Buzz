package com.buzzit.gui.state;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.buzzit.logic.Question;


public class ShowQuestion implements GameStrategy {
    private float timerTicksCount;
    private com.buzzit.gui.Interactor interactor;


    private final float delay;
    private final float delta;
    private final float duration;

    private boolean finished;
    private Timer.Task timerTask;
    private Question question;


    public ShowQuestion (final com.buzzit.gui.Interactor interactor, final float delay, final int secondsToAnswer, Question question) {
        this.timerTicksCount = 0;
        this.interactor = interactor;

        this.delay = delay;
        this.delta = 0.8f;
        this.duration = 0.8f;
        this.finished = false;

        this.question = question;

        timerTask = new Timer.Task() {
            @Override
            public void run() {
                if (timerTicksCount == 0) {
                    interactor.hideElementsExceptPoints();
                    interactor.newButtonColors();
                    updateText();

                    interactor.labelStatus.setText(Integer.toString(secondsToAnswer));
                    interactor.labelCategory.addAction(Actions.fadeIn(duration));
                }
                else if (timerTicksCount == 1) {
                    interactor.labelQuestion.addAction(Actions.fadeIn(duration));
                }
                else if (timerTicksCount == 2) {
                    for(com.buzzit.gui.OptionButton button: interactor.btnOptions){
                        button.addAction(Actions.fadeIn(duration));
                    }

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
        interactor.uncheckButtons();
        interactor.disableButtons();
        Timer.schedule(timerTask, delay, delta, 3);
    }

    public void updateText() {
        String[] options = question.generateOptions();
        interactor.labelQuestion.setText(question.getQuestion());
        interactor.labelCategory.setText(question.getCategory().getName());

        int i = 0;
        for (com.buzzit.gui.OptionButton button: interactor.btnOptions){
            button.setText(options[i]);
            i++;
        }
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
