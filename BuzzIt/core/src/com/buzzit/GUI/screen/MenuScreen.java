package com.buzzit.gui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.buzzit.gui.AnimatedDrawable;
import com.buzzit.gui.AudioManager;
import com.buzzit.logic.Play;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;

public class MenuScreen implements Screen {
    private ScreenState.ScreenType parentType;

    final int WIDTH = 1080;
    final int HEIGHT = 1920;

    /* Disposables */
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture playTexture;
    private TextureAtlas settingsAtlas;
    private TextureAtlas highscoreAtlas;
    private FreeTypeFontGenerator generator;
    private BitmapFont font;

    /* Dialogs */
    private GDXDialogs dialogs;
    private GDXButtonDialog impossibleQuestionsDialog;


    public MenuScreen(ScreenState.ScreenType pType) {
        create();
        parentType = pType;
    }


    public void create() {
        Gdx.input.setCatchBackKey(true);

        batch = new SpriteBatch();

        /*** Title ***/
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/good_times.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 150;
        font = generator.generateFont(parameter);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.RED;
        Label titleImage = new Label("Buzz It", labelStyle);



        /*** Creating buttons ***/
        playTexture = new Texture(Gdx.files.internal("menu/play.png"));
        ImageButton btnSingleplayer = new ImageButton(new SpriteDrawable(new Sprite(playTexture)));
        btnSingleplayer.getImage().setScaling(Scaling.fit);

        settingsAtlas = new TextureAtlas(Gdx.files.internal("packs/settings/settings.pack"));
        Animation settingsAnimation = new Animation(1f/20f, settingsAtlas.getRegions());
        AnimatedDrawable animatedDrawable = new AnimatedDrawable(settingsAnimation);
        ImageButton btnSettings = new ImageButton(animatedDrawable, animatedDrawable);

        highscoreAtlas = new TextureAtlas(Gdx.files.internal("packs/highscore/highscore.pack"));
        Animation highscoreAnimation = new Animation(1f/20f, highscoreAtlas.getRegions());
        animatedDrawable = new AnimatedDrawable(highscoreAnimation);
        ImageButton btnHighscore = new ImageButton(animatedDrawable, animatedDrawable);

        /*** Creating stage ***/
        Table buttonsTable = new Table();
        buttonsTable.add(titleImage).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnSingleplayer).width(400).height(400).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnSettings).minWidth(200).minHeight(200).padBottom(100);
        buttonsTable.row();

        buttonsTable.add(btnHighscore).minWidth(200).minHeight(200);
        buttonsTable.row();
        buttonsTable.setFillParent(true);

        backgroundTexture = new Texture(Gdx.files.internal("menu/background.jpg"));
        buttonsTable.background(new SpriteDrawable(new Sprite(backgroundTexture)));

        stage = new Stage(new FillViewport(WIDTH, HEIGHT));
        stage.addActor(buttonsTable);


        /*** Listeners ***/
        btnSingleplayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (Play.playable(SettingsScreen.getNumQuestions(),
                        SettingsScreen.getCategories(), SettingsScreen.getDifficulty())) {
                    AudioManager.getInstance().getMusic("background").stop();
                    ScreenState.getInstance().changeState(ScreenState.ScreenType.SINGLEPLAYER);
                }
                else {
                    if(SettingsScreen.getNumQuestions() == 0)
                        impossibleQuestionsDialog.setMessage("You must have questions!");
                    else impossibleQuestionsDialog.setMessage("We don't have that many questions for you");
                    impossibleQuestionsDialog.build().show();
                }
            }
        });

        btnSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ScreenState.getInstance().changeState(ScreenState.ScreenType.SETTINGS);
            }
        });

        btnHighscore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ScreenState.getInstance().changeState(ScreenState.ScreenType.HIGHSCORE);
            }
        });


        /*** Dialogs ***/
        dialogs = GDXDialogsSystem.install();

        impossibleQuestionsDialog = dialogs.newDialog(GDXButtonDialog.class);
        impossibleQuestionsDialog.setTitle("Slow down!");
        impossibleQuestionsDialog.addButton("Back");

        Gdx.input.setInputProcessor(stage);


    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        if(!AudioManager.getInstance().getMusic("background").isPlaying())
            AudioManager.getInstance().getMusic("background").play();
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
        stage.getViewport().update(width, height, true);
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
        backgroundTexture.dispose();
        playTexture.dispose();
        stage.dispose();
        batch.dispose();
        settingsAtlas.dispose();
        highscoreAtlas.dispose();
        font.dispose();
        generator.dispose();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.dispose();
    }
}
