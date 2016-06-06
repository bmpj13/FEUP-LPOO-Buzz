package com.buzzit.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;


public class Interactor {
    private final String[] optionChars = {"A", "B", "C", "D"};
    private Random rand;

    public final Color RightColor = Color.GREEN;
    public final Color WrongColor = Color.RED;

    public Skin skin;
    public OptionButton[] btnOptions;
    public Label labelPoints;
    public Label labelCategory;
    public Label labelQuestion;
    public Label labelStatus;

    public Label.LabelStyle labelPointsStyle;


    public Interactor (Skin skin) {
        rand = new Random();

        this.skin = skin;

        // Creating styles
        labelPointsStyle = new Label.LabelStyle();
        labelPointsStyle.font = skin.getFont("numbers");
        labelPointsStyle.fontColor = Color.WHITE;

        // Creating elements
        labelPoints = new Label("0", labelPointsStyle);
        labelPoints.setAlignment(Align.center);

        labelCategory = new Label("", skin, "question_background");
        labelCategory.setAlignment(Align.center);

        labelQuestion = new Label("", skin, "question_background");
        labelQuestion.setWrap(true);
        labelQuestion.setAlignment(Align.center);


        TextButton.TextButtonStyle textButtonStyle;
        Color uncheckedColor;
        Color checkedColor = Color.GRAY;

        btnOptions = new OptionButton[4];

        for(int i = 0; i < 4; i++) {
            uncheckedColor = generateColor();
            textButtonStyle = createTextButtonStyle(skin, uncheckedColor, checkedColor);
            btnOptions[i] = new OptionButton(optionChars[i], textButtonStyle, uncheckedColor, checkedColor);
            btnOptions[i].getLabel().setWrap(true);
        }


        labelStatus = new Label("", skin, "numbers");
        labelStatus.setWrap(true);
        labelStatus.setAlignment(Align.center);
    }


    public void enableButtons() {
        for(OptionButton button: btnOptions){
            button.setTouchable(Touchable.enabled);
        }
    }

    public void disableButtons() {
        for(OptionButton button: btnOptions){
            button.setTouchable(Touchable.disabled);
        }
    }

    public void uncheckButtons() {
        for(OptionButton button: btnOptions){
            button.setChecked(false);
        }
    }

    public void hideElementsExceptPoints() {
        for(OptionButton button: btnOptions){
            button.addAction(Actions.alpha(0));
        }

        labelCategory.addAction(Actions.alpha(0));
        labelQuestion.addAction(Actions.alpha(0));
        labelStatus.addAction(Actions.alpha(0));
    }

    public void nextQuestion(float delay, final float duration) {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                for(OptionButton button: btnOptions){
                    button.addAction(Actions.fadeOut(duration));
                }

                labelCategory.addAction(Actions.fadeOut(duration));
                labelQuestion.addAction(Actions.fadeOut(duration));
                labelStatus.addAction(Actions.fadeOut(duration));
            }
        }, delay);
    }

    public void reset() {
        labelPoints.setText(Integer.toString(0));
        labelCategory.setText("");
        labelQuestion.setText("");
        labelStatus.setText("");

        for (OptionButton button : btnOptions) {
            button.setText("");
        }
    }

    public void newButtonColors() {
        for (OptionButton button : btnOptions) {
            Color color = generateColor();
            button.getButtonStyle().up = skin.newDrawable("btn_background", color);
            button.setUncheckedColor(color);
        }
    }

    private Color generateColor() {
        final float minLuminance = 0f;
        final float maxLuminance = 0.65f;

        float r = rand.nextFloat();                         // Generate red
        float b = rand.nextFloat();                         // Generate blue
        float Y = rand.nextFloat() * (maxLuminance - minLuminance) + minLuminance;  // Generate luminance in interval
        float g = (Y - 0.299f*r - 0.114f*b) / 0.587f;       // Generate green, depending on luminance

        return new Color(r, g, b, 1);
    }

    private TextButton.TextButtonStyle createTextButtonStyle(Skin skin, Color unchecked, Color checked) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btn_background", unchecked);
        textButtonStyle.checked = skin.newDrawable("btn_background", checked);
        textButtonStyle.font = skin.getFont("default");

        return textButtonStyle;
    }
}
