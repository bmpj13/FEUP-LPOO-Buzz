package com.buzzit.GUI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Random;

public class OptionButton extends TextButton {
    private String prefix;
    private String content;
    private TextButtonStyle textButtonStyle;
    private Color uncheckedColor;
    private Color checkedColor;

    public OptionButton(String prefix, TextButtonStyle textButtonStyle, Color uncheckedColor, Color checkedColor) {
        super(prefix, textButtonStyle);
        this.prefix = prefix;
        this.content = "";

        this.textButtonStyle = textButtonStyle;
        this.uncheckedColor = uncheckedColor;
        this.checkedColor = checkedColor;

        setTransform(true);
    }

    @Override
    public void setText(String text) {
        super.setText(prefix + ": " + text);
        this.content = text;
    }

    public String getContent() {
        return content;
    }


    public Color getUncheckedColor() {
        return uncheckedColor;
    }

    public Color getCheckedColor() {
        return checkedColor;
    }

    public void setUncheckedColor(Color uncheckedColor) {
        this.uncheckedColor = uncheckedColor;
    }

    public void setCheckedColor(Color checkedColor) {
        this.checkedColor = checkedColor;
    }

    public TextButtonStyle getButtonStyle() {
        return textButtonStyle;
    }
}
