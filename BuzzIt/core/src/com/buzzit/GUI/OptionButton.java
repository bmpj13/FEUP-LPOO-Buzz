package com.buzzit.GUI;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionButton extends TextButton {
    String prefix;
    String content;

    public OptionButton(String prefix, Skin skin) {
        super(prefix, skin);
        this.prefix = prefix;
        this.content = "";
    }

    public OptionButton(String prefix, Skin skin, String styleName) {
        super(prefix, skin, styleName);
        this.prefix = prefix;
        this.content = "";
    }

    public OptionButton(String prefix, TextButtonStyle style) {
        super(prefix, style);
        this.prefix = prefix;
    }

    @Override
    public void setText(String text) {
        super.setText(prefix + ": " + text);
        this.content = text;
    }

    public String getContent() {
        return content;
    }
}
