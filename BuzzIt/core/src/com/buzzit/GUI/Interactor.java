package com.buzzit.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;


public class Interactor {

    public final int LABEL_SCALE = 3;         // Font scale
    public final Color RightColor = Color.GREEN;
    public final Color WrongColor = Color.RED;

    public Skin skin;
    public OptionButton btnOptionA;
    public OptionButton btnOptionB;
    public OptionButton btnOptionC;
    public OptionButton btnOptionD;
    public Label labelPoints;
    public Label labelCategory;
    public Label labelQuestion;
    public Label labelStatus;

    public Label.LabelStyle labelPointsStyle;


    public Interactor (Skin skin) {
        this.skin = skin;

        // Creating styles
        labelPointsStyle = new Label.LabelStyle();
        labelPointsStyle.font = skin.getFont("default");
        labelPointsStyle.fontColor = Color.WHITE;

        // Creating elements
        labelPoints = new Label("0", labelPointsStyle);
        labelPoints.setAlignment(Align.center);
        labelPoints.setFontScale(LABEL_SCALE);

        labelCategory = new Label("Categoria:", skin, "question_background");
        labelCategory.setAlignment(Align.center);

        labelQuestion = new Label("", skin, "question_background");
        labelQuestion.setWrap(true);
        labelQuestion.setAlignment(Align.center);


        TextButton.TextButtonStyle textButtonStyle;
        Color uncheckedColor;
        Color checkedColor = Color.GRAY;

        // Button A
        uncheckedColor = generateColor();
        textButtonStyle = createTextButtonStyle(skin, uncheckedColor, checkedColor);
        btnOptionA = new OptionButton("A", textButtonStyle, uncheckedColor, checkedColor);


        // Button B
        uncheckedColor = generateColor();
        textButtonStyle = createTextButtonStyle(skin, uncheckedColor, checkedColor);
        btnOptionB = new OptionButton("B", textButtonStyle, uncheckedColor, checkedColor);


        // Button C
        uncheckedColor = generateColor();
        textButtonStyle = createTextButtonStyle(skin, uncheckedColor, checkedColor);
        btnOptionC = new OptionButton("C", textButtonStyle, uncheckedColor, checkedColor);

        // Button D
        uncheckedColor = generateColor();
        textButtonStyle = createTextButtonStyle(skin, uncheckedColor, checkedColor);
        btnOptionD = new OptionButton("D", textButtonStyle, uncheckedColor, checkedColor);


        labelStatus = new Label("", skin);
        labelStatus.setWrap(true);
        labelStatus.setFontScale(LABEL_SCALE);
        labelStatus.setAlignment(Align.center);
    }


    public void enableButtons() {
        btnOptionA.setTouchable(Touchable.enabled);
        btnOptionB.setTouchable(Touchable.enabled);
        btnOptionC.setTouchable(Touchable.enabled);
        btnOptionD.setTouchable(Touchable.enabled);
    }

    public void disableButtons() {
        btnOptionA.setTouchable(Touchable.disabled);
        btnOptionB.setTouchable(Touchable.disabled);
        btnOptionC.setTouchable(Touchable.disabled);
        btnOptionD.setTouchable(Touchable.disabled);
    }

    public void uncheckButtons() {
        btnOptionA.setChecked(false);
        btnOptionB.setChecked(false);
        btnOptionC.setChecked(false);
        btnOptionD.setChecked(false);
    }

    public void hideElementsExceptPoints() {
        btnOptionA.addAction(Actions.alpha(0));
        btnOptionB.addAction(Actions.alpha(0));
        btnOptionC.addAction(Actions.alpha(0));
        btnOptionD.addAction(Actions.alpha(0));

        labelCategory.addAction(Actions.alpha(0));
        labelQuestion.addAction(Actions.alpha(0));
        labelStatus.addAction(Actions.alpha(0));
    }

    public void nextQuestion(float delay, final float duration) {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                btnOptionA.addAction(Actions.fadeOut(duration));
                btnOptionB.addAction(Actions.fadeOut(duration));
                btnOptionC.addAction(Actions.fadeOut(duration));
                btnOptionD.addAction(Actions.fadeOut(duration));
                labelCategory.addAction(Actions.fadeOut(duration));
                labelQuestion.addAction(Actions.fadeOut(duration));
                labelStatus.addAction(Actions.fadeOut(duration));
            }
        }, delay);
    }

    private Color generateColor() {
        Random rand = new Random();

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

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
