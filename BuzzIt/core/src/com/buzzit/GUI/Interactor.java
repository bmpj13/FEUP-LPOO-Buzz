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


public class Interactor {

    public final int LABEL_POINTS_FSCALE = 8;          // Font scale
    public final int LABEL_CATEGORY_FSCALE = 3;        // Font scale
    public final int LABEL_QUESTION_FSCALE = 3;        // Font scale
    public final int LABEL_STATUS_FSCALE = 10;         // Font scale
    public final int BUTTON_SCALE_X = 3;
    public final int BUTTON_SCALE_Y = 3;
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

    public TextButton.TextButtonStyle textButtonStyle;
    public Label.LabelStyle labelPointsStyle;


    public Interactor (Skin skin) {
        this.skin = skin;

        // Creating styles
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btn_background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("btn_background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("btn_background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("btn_background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", textButtonStyle);

        labelPointsStyle = new Label.LabelStyle();
        labelPointsStyle.font = skin.getFont("default");
        labelPointsStyle.fontColor = Color.WHITE;

        // Creating elements
        labelPoints = new Label("0", labelPointsStyle);
        labelPoints.setFontScale(LABEL_POINTS_FSCALE);
        labelPoints.setAlignment(Align.center);

        labelCategory = new Label("Categoria: Amizade", skin, "question_background");
        labelCategory.setFontScale(LABEL_CATEGORY_FSCALE);
        labelCategory.setAlignment(Align.center);

        labelQuestion = new Label("Qual o nivel de amizade?", skin, "question_background");
        labelQuestion.setWrap(true);
        labelQuestion.setFontScale(LABEL_QUESTION_FSCALE);
        labelQuestion.setAlignment(Align.center);

        btnOptionA = new OptionButton("A", skin);
        btnOptionA.setTransform(true);
        btnOptionA.getLabel().setFontScale(BUTTON_SCALE_X, BUTTON_SCALE_Y);

        btnOptionB = new OptionButton("B", skin);
        btnOptionB.setTransform(true);
        btnOptionB.getLabel().setFontScale(BUTTON_SCALE_X, BUTTON_SCALE_Y);

        btnOptionC = new OptionButton("C", skin);
        btnOptionC.setTransform(true);
        btnOptionC.getLabel().setFontScale(BUTTON_SCALE_X, BUTTON_SCALE_Y);

        btnOptionD = new OptionButton("D", skin);
        btnOptionD.setTransform(true);
        btnOptionD.getLabel().setFontScale(BUTTON_SCALE_X, BUTTON_SCALE_Y);
        btnOptionD.setText("teste");


        labelStatus = new Label("", skin);
        labelStatus.setWrap(true);
        labelStatus.setFontScale(LABEL_STATUS_FSCALE);
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

    public void hideElements() {
        btnOptionA.addAction(Actions.alpha(0));
        btnOptionB.addAction(Actions.alpha(0));
        btnOptionC.addAction(Actions.alpha(0));
        btnOptionD.addAction(Actions.alpha(0));

        labelCategory.addAction(Actions.alpha(0));
        labelQuestion.addAction(Actions.alpha(0));
        labelStatus.addAction(Actions.alpha(0));
        labelPoints.addAction(Actions.alpha(0));
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
                labelPoints.addAction(Actions.fadeOut(duration));
            }
        }, delay);
    }
}
