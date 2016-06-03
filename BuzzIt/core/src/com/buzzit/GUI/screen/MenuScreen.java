package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class MenuScreen extends SuperScreen {
    Stage stage;
    Texture playTexture;
    Texture settingsTexture;
    Texture highscoreTexture;

    public MenuScreen(Game g, ScreenState.ScreenType pType) {
        create();
        game = g;
        parentType = pType;
    }


    public void create() {
        super.create();

        /*** Creating buttons ***/
        playTexture = new Texture(Gdx.files.internal("menu/play.png"));
        ImageButton btnSingleplayer = new ImageButton(new SpriteDrawable(new Sprite(playTexture)));

        settingsTexture = new Texture(Gdx.files.internal("menu/settings.png"));
        ImageButton btnSettings = new ImageButton(new SpriteDrawable(new Sprite((settingsTexture))));

        highscoreTexture = new Texture(Gdx.files.internal("menu/highscore.png"));
        ImageButton btnHighscore = new ImageButton(new SpriteDrawable(new SpriteDrawable( new Sprite(highscoreTexture))));

        /*** Creating stage ***/
        Table buttonsTable = new Table();
        buttonsTable.add(btnSingleplayer).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnSettings).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnHighscore);
        buttonsTable.row();
        buttonsTable.setFillParent(true);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(buttonsTable);


        /*** Listeners ***/
        btnSingleplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ScreenState.getInstance().changeState(ScreenState.ScreenType.SINGLEPLAYER);

            }
        });

        btnSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ScreenState.getInstance().changeState(ScreenState.ScreenType.SETTINGS);
            }
        });
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
    public void pause() {
        super.pause();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        playTexture.dispose();
        stage.dispose();
    }
}