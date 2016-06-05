package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.buzzit.GUI.OptionButton;
import com.buzzit.GUI.state.*;
import com.buzzit.GUI.Interactor;
import com.buzzit.Logic.Category;
import com.buzzit.Logic.Difficulty;
import com.buzzit.Logic.Match;

import java.util.ArrayList;


public class SingleplayerScreen extends SuperScreen {
    private GameStrategy strat = null;

    /* Disposable elements */
    private Skin skin;
    private BitmapFont txtFont;
    private FreeTypeFontGenerator generator;
    private Pixmap pixmap;
    private Texture btnBackgroundTexture;
    private Texture questionBackgroundTexture;
    private Stage stage;

    /* Displayed elements */
    private Interactor interactor;

    /* Constants */
    private final int SECONDS_TO_ANSWER = 10;
    private final float TIME_BETWEEN_QUESTIONS = 1f;

    private Match match;

    public SingleplayerScreen(Game g, ScreenState.ScreenType pType) {
        create();
        game = g;
        parentType = pType;

    }

    public void create() {
        super.create();

        createSkin();
        interactor = new Interactor(skin);

        /*** Creating stage ***/
        Table table = new Table();

        table.add(interactor.labelPoints).minWidth((int) (Gdx.graphics.getWidth() * 0.1))
                .padLeft((int) (Gdx.graphics.getWidth() * 0.8)).padBottom(80);
        table.row();

        table.add(interactor.labelCategory).minWidth(Gdx.graphics.getWidth()/2).minHeight(100).padBottom(50);
        table.row();

        table.add(interactor.labelQuestion).minWidth((int) (Gdx.graphics.getWidth()/1.3)).minHeight(100).padBottom(150);
        table.row();


        for(OptionButton button: interactor.btnOptions){
            table.add(button).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
            table.row();
        }

//        table.add(interactor.btnOptionA).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
//        table.row();
//
//        table.add(interactor.btnOptionB).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
//        table.row();
//
//        table.add(interactor.btnOptionC).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
//        table.row();
//
//        table.add(interactor.btnOptionD).minWidth(Gdx.graphics.getWidth()/2).height(100).padBottom(100);
//        table.row();
//
//        table.add(interactor.labelStatus);
//        table.row();

        table.setFillParent(true);


        stage = new Stage();
        stage.addActor(table);


        /*** Listeners ***/
        for(final OptionButton button: interactor.btnOptions){
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    handleButton(button);
                }
            });
        }
//        interactor.btnOptionA.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                handleButton(interactor.btnOptionA);
//            }
//        });
//
//        interactor.btnOptionB.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                handleButton(interactor.btnOptionB);
//            }
//        });
//
//        interactor.btnOptionC.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                handleButton(interactor.btnOptionC);
//            }
//        });
//
//        interactor.btnOptionD.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                handleButton(interactor.btnOptionD);
//            }
//        });
    }


    void createSkin() {
        /*** Creating a skin ***/
        skin = new Skin();

        // Creating fonts
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;

        txtFont = generator.generateFont(parameter);
        txtFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", txtFont);

        // Creating textures
        pixmap = new Pixmap(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        btnBackgroundTexture = new Texture(pixmap);
        skin.add("btn_background", btnBackgroundTexture);

        questionBackgroundTexture = new Texture(Gdx.files.internal("play/questionBackground.PNG"));
        skin.add("question_background", new NinePatch(questionBackgroundTexture));


        // Creating styles (that are not needed by interactor)
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        labelStyle.background = skin.getDrawable("question_background");
        skin.add("question_background", labelStyle);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);
    }


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);

        match = new Match(SettingsScreen.getNumQuestions(),
                    SettingsScreen.getCategories(), Difficulty.EASY);
        strat = new ShowQuestion(interactor, 0, 0.8f, 0.8f, SECONDS_TO_ANSWER, match.getCurrentQuestion());
        strat.start();
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

        if (strat.finished())
            switchStrategy();

        strat.render();

        stage.act();
        stage.draw();
    }

    private void switchStrategy() {

        
        if (strat instanceof ShowQuestion)
            strat = new WaitingAnswer(interactor, SECONDS_TO_ANSWER);
        else if (strat instanceof WaitingAnswer) {
            strat = new Unanswered(interactor, -match.getCurrentQuestion().getDifficulty().getPoints());
            match.nextQuestion();
        }
        else if (strat instanceof Decision) {
            interactor.nextQuestion(TIME_BETWEEN_QUESTIONS/2, TIME_BETWEEN_QUESTIONS/2 - 0.3f);
            strat = new ShowQuestion(interactor, TIME_BETWEEN_QUESTIONS, 0.8f, 0.8f, SECONDS_TO_ANSWER, match.getCurrentQuestion());
        }
        strat.start();
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
        btnBackgroundTexture.dispose();
        questionBackgroundTexture.dispose();
        generator.dispose();
    }



    void handleButton(OptionButton button) {
        strat.finish();

        int points = match.getCurrentQuestion().getDifficulty().getPoints();

        if (match.isCorrect(button.getContent()))     strat = new Answered(interactor, button, points, true);
        else                                          strat = new Answered(interactor, button, -points, false);
        match.nextQuestion();
        strat.start();
    }
}
