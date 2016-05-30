package com.buzzit.GUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends SuperScreen {

    Screen newGameScreen;
    Screen highscoresScreen;
    Screen creditsScreen;

    Skin skin;
    BitmapFont font;
    Pixmap pixmap;
    Texture pixmapTexture;
    Stage stage;

    public MainMenuScreen(Game g) {
        create();
        game = g;

        newGameScreen = new NewGameMenuScreen(game,this);
    }


    public void create() {
        super.create(this);

        /*** Creating a skin ***/
        skin = new Skin();

        // Creating fonts
        font = new BitmapFont();
        skin.add("default", font);

        // Creating textures
        pixmap = new Pixmap(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        pixmapTexture = new Texture(pixmap);
        skin.add("background", pixmapTexture);


        // Creating button styles
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        /*** Creating buttons ***/
        TextButton btnNewGame = new TextButton("NEW GAME", skin);
        btnNewGame.getLabel().setFontScale(3, 3);

        TextButton btnHighscores = new TextButton("Highscores", skin);
        btnHighscores.getLabel().setFontScale(3, 3);

        TextButton btnCredits = new TextButton("credits", skin);
        btnCredits.getLabel().setFontScale(3, 3);


        /*** Creating stage ***/
        Table buttonsTable = new Table();
        buttonsTable.add(btnNewGame).width(Gdx.graphics.getWidth()/2).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnHighscores).width(Gdx.graphics.getWidth()/2).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnCredits).width(Gdx.graphics.getWidth()/2);
        buttonsTable.row();

        buttonsTable.setFillParent(true);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        stage.addActor(buttonsTable);


        /*** Listeners ***/
        btnNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(newGameScreen);
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

        stage.dispose();
        skin.dispose();
        font.dispose();
        pixmap.dispose();
        pixmapTexture.dispose();
    }
}
