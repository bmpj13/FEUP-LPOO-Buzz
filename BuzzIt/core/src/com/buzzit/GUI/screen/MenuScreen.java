package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.buzzit.GUI.AnimatedDrawable;

public class MenuScreen implements Screen {
    private ScreenState.ScreenType parentType;

    /* Disposables */
    private Stage stage;
    private Texture titleTexture;
    private Texture playTexture;
    private Texture highscoreTexture;
    private SpriteBatch batch;
    private TextureAtlas settingsAtlas;


    public MenuScreen(ScreenState.ScreenType pType) {
        create();
        parentType = pType;
    }


    public void create() {
        Gdx.input.setCatchBackKey(true);

        batch = new SpriteBatch();

        /*** Title ***/
        titleTexture = new Texture(Gdx.files.internal("menu/title.png"));
        Image titleImage = new Image(titleTexture);


        /*** Creating buttons ***/
        playTexture = new Texture(Gdx.files.internal("menu/play.png"));
        ImageButton btnSingleplayer = new ImageButton(new SpriteDrawable(new Sprite(playTexture)));
        btnSingleplayer.getImage().setScaling(Scaling.fit);

        settingsAtlas = new TextureAtlas(Gdx.files.internal("packs/settings/settings.pack"));
        Animation settingsAnimation = new Animation(1f/30f, settingsAtlas.getRegions());
        AnimatedDrawable animatedDrawable = new AnimatedDrawable(settingsAnimation);
        ImageButton btnSettings = new ImageButton(animatedDrawable, animatedDrawable);

        highscoreTexture = new Texture(Gdx.files.internal("menu/highscore.png"));
        ImageButton btnHighscore = new ImageButton(new SpriteDrawable(new SpriteDrawable( new Sprite(highscoreTexture))));

        ImageButton btnMultiplayer = new ImageButton(new SpriteDrawable(new Sprite(playTexture)));
        btnMultiplayer.getImage().setScaling(Scaling.fit);

        /*** Creating stage ***/
        Table buttonsTable = new Table();
        buttonsTable.add(titleImage).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnSingleplayer).width(400).height(400).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnSettings).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnHighscore);
        buttonsTable.row();

        buttonsTable.add(btnMultiplayer).width(300).height(300).padBottom(100);
        buttonsTable.row();
        buttonsTable.setFillParent(true);

        stage = new Stage();
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

        btnMultiplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ScreenState.getInstance().changeState(ScreenState.ScreenType.MULTIPLAYER1);

            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (parentType != null) {
                ScreenState.getInstance().changeState(parentType);
            }
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        titleTexture.dispose();
        playTexture.dispose();
        highscoreTexture.dispose();
        stage.dispose();
        batch.dispose();
        settingsAtlas.dispose();
    }
}
