package com.buzzit.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

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

    public Color getCheckedColor() {
        return checkedColor;
    }

    public void setUncheckedColor(Color uncheckedColor) {
        this.uncheckedColor = uncheckedColor;
    }

    public TextButtonStyle getButtonStyle() {
        return textButtonStyle;
    }
}
