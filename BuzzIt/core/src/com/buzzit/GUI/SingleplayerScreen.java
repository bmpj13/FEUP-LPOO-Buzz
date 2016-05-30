package com.buzzit.GUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import com.buzzit.Logic.Play;


public class SingleplayerScreen extends SuperScreen {

    Skin skin;
    BitmapFont txtFont;
    Pixmap pixmap;
    Texture pixmapTexture;
    Stage stage;

    Play play;

    public SingleplayerScreen(Game g, Screen p) {
        create();
        game = g;
        parent = p;
    }

    private void create() {
        super.create(this);

        /*** Creating a skin ***/
        skin = new Skin();

        // Creating fonts
        txtFont = new BitmapFont();
        skin.add("default", txtFont);

        // Creating textures
        pixmap = new Pixmap(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixmapTexture = new Texture(pixmap);
        skin.add("btn_background", pixmapTexture);

        skin.add("question_background", new NinePatch(new Texture(Gdx.files.internal("questionBackground.PNG"))));


        // Creating styles
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btn_background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("btn_background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("btn_background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("btn_background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        labelStyle.background = skin.getDrawable("question_background");
        skin.add("default", labelStyle);



        /*** Creating views ***/
        Label labelCategory = new Label("Categoria: Amizade", skin);
        labelCategory.setFontScale(3);
        labelCategory.setAlignment(Align.center);

        Label labelQuestion = new Label("Qual o nivel de amizade?", skin);
        labelQuestion.setWrap(true);
        labelQuestion.setFontScale(3);
        labelQuestion.setAlignment(Align.center);

        TextButton btnOptionA = new TextButton("A: 7", skin);
        btnOptionA.getLabel().setFontScale(3, 3);

        TextButton btnOptionB = new TextButton("B: 999999", skin);
        btnOptionB.getLabel().setFontScale(3, 3);

        TextButton btnOptionC = new TextButton("C: 0", skin);
        btnOptionC.getLabel().setFontScale(3, 3);

        TextButton btnOptionD = new TextButton("D: 25", skin);
        btnOptionD.getLabel().setFontScale(3, 3);


        /*** Creating stage ***/
        Table buttonsTable = new Table();

        buttonsTable.add(labelCategory).minWidth(Gdx.graphics.getWidth()/2).minHeight(100).padBottom(50);
        buttonsTable.row();

        buttonsTable.add(labelQuestion).minWidth((int) (Gdx.graphics.getWidth()/1.3)).minHeight(100).padBottom(150);
        buttonsTable.row();

        buttonsTable.add(btnOptionA).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnOptionB).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnOptionC).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnOptionD).minWidth(Gdx.graphics.getWidth()/2).height(100);
        buttonsTable.row();

        buttonsTable.setFillParent(true);


        stage = new Stage();
        stage.addActor(buttonsTable);

        /*** Listeners ***/
    }


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        super.hide();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        super.dispose();

        stage.dispose();
        skin.dispose();
        txtFont.dispose();
        pixmap.dispose();
        pixmapTexture.dispose();
    }
}
