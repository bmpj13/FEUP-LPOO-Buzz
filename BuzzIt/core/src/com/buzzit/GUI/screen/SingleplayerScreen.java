package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.buzzit.GUI.OptionButton;
import com.buzzit.GUI.state.*;
import com.buzzit.GUI.Interactor;
import com.buzzit.Logic.*;
import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import de.tomgrill.gdxdialogs.core.listener.ButtonClickListener;


public class SingleplayerScreen implements Screen {

    private ScreenState.ScreenType parentType;
    private GameStrategy strat = null;

    /* Disposable elements */
    private Skin skin;
    private BitmapFont txtFont;
    private BitmapFont numbersFont;
    private FreeTypeFontGenerator generator;
    private Pixmap btnPixmap;
    private Texture btnBackgroundTexture;
    private Texture questionBackgroundTexture;
    private Stage stage;
    private Viewport viewport;
    private Camera camera;

    /* Displayed elements */
    private Interactor interactor;

    /* Constants */
    final int WIDTH = 1080;
    final int HEIGHT = 1920;
    private enum GameState { RUNNING, WAITING }
    private final int SECONDS_TO_ANSWER = 10;
    private final float TIME_BETWEEN_QUESTIONS = 1f;

    /* Dialogs */
    private GDXDialogs dialogs;
    private GDXButtonDialog finishedDialog;
    private GDXButtonDialog backDialog;


    /* Variables */
    private Match match;
    private GameState gameState;

    public SingleplayerScreen(ScreenState.ScreenType pType) {
        create();
        parentType = pType;
    }

    public void create() {
        camera = new PerspectiveCamera();
        viewport = new FillViewport(WIDTH, HEIGHT, camera);
        Gdx.input.setCatchBackKey(true);

        createSkin();
        interactor = new Interactor(skin);

        /*** Creating stage ***/
        Table table = new Table();

        table.add(interactor.labelPoints).minWidth((int) (WIDTH * 0.1))
                .padLeft((int) (WIDTH * 0.7)).padBottom(80);
        table.row();

        table.add(interactor.labelCategory).minWidth(WIDTH/2).minHeight(100).padBottom(50);
        table.row();

        table.add(interactor.labelQuestion).minWidth((int) (WIDTH/1.3)).minHeight(100).padBottom(150);
        table.row();


        for(OptionButton button: interactor.btnOptions){
            table.add(button).width(WIDTH/2).height(100).padBottom(100);
            table.row();
        }

        table.add(interactor.labelStatus);
        table.row();

        table.setFillParent(true);


        stage = new Stage(new FillViewport(WIDTH, HEIGHT));
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


        /*** Dialogs ***/
        dialogs = GDXDialogsSystem.install();

        finishedDialog = dialogs.newDialog(GDXButtonDialog.class);
        finishedDialog.setTitle("Good Game!");
        finishedDialog.addButton("OK");
        finishedDialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {
                goBack();
            }
        });


        backDialog = dialogs.newDialog(GDXButtonDialog.class);
        backDialog.setTitle("Go to menu?");
        backDialog.setMessage("Your amazing run will be lost!");
        backDialog.addButton("Yes");
        backDialog.addButton("No");
        backDialog.setClickListener(new ButtonClickListener() {
            @Override
            public void click(int button) {

                if (button == 0)            // "Yes" button
                    goBack();
                else if (button == 1)       // "No" button
                    run();
            }
        });
    }


    void createSkin() {
        /*** Creating a skin ***/
        skin = new Skin();

        // Creating fonts
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Sansation-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2;

        txtFont = generator.generateFont(parameter);
        txtFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        skin.add("default", txtFont);


        numbersFont = new BitmapFont(Gdx.files.internal("fonts/sansation.fnt"));
        numbersFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        numbersFont.getData().setScale(3,3);
        skin.add("numbers", numbersFont);

        // Creating textures
        btnPixmap = new Pixmap(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/10, Pixmap.Format.RGBA8888);
        btnPixmap.setColor(Color.WHITE);
        btnPixmap.fill();
        btnBackgroundTexture = new Texture(btnPixmap);
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

        labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("numbers");
        labelStyle.fontColor = Color.WHITE;
        skin.add("numbers", labelStyle);
    }


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Player player = new Player(SettingsScreen.getName());
        match = new Match(SettingsScreen.getNumQuestions(), SettingsScreen.getCategories(), SettingsScreen.getDifficulty(), player);
        strat = new ShowQuestion(interactor, 0, SECONDS_TO_ANSWER, match.getCurrentQuestion());
        interactor.hideElementsExceptPoints();

        run();
        strat.start();
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
                stop();
                backDialog.build().show();
            }
        }

        if (gameState == GameState.RUNNING) {
            if (strat.finished()) {
                if (match.getQuestionIndex() == match.getTotalQuestions())
                    endGame();
                else
                    switchStrategy();
            }

            strat.render();
        }

        stage.act();
        stage.draw();
    }

    private void switchStrategy() {


        if (strat instanceof ShowQuestion)
            strat = new WaitingAnswer(interactor, SECONDS_TO_ANSWER);
        else if (strat instanceof WaitingAnswer) {
            strat = new Unanswered(interactor, -match.getCurrentQuestion().getDifficulty().getPoints());
            match.unanswered();
            match.nextQuestion();
        }
        else if (strat instanceof Decision) {
            interactor.nextQuestion(TIME_BETWEEN_QUESTIONS/2, TIME_BETWEEN_QUESTIONS/3);
            strat = new ShowQuestion(interactor, TIME_BETWEEN_QUESTIONS, SECONDS_TO_ANSWER, match.getCurrentQuestion());
        }

        strat.start();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        interactor.reset();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        txtFont.dispose();
        numbersFont.dispose();
        btnPixmap.dispose();
        btnBackgroundTexture.dispose();
        questionBackgroundTexture.dispose();
        generator.dispose();
    }



    private void handleButton(OptionButton button) {
        strat.finish();

        int points = match.getCurrentQuestion().getDifficulty().getPoints();

        if (match.isCorrect(button.getContent())) {
            match.getPlayer().addPoints(points);
            strat = new Answered(interactor, button, points, true);
        }
        else {
            match.getPlayer().addPoints(-points);
            strat = new Answered(interactor, button, -points, false);
        }

        match.nextQuestion();
        strat.start();
    }


    private void endGame() {
        stop();

        String message = "Hey, " + match.getPlayer().getName() + " ! You got " + match.getPlayer().getPoints() + " points.";

        if (Play.getInstance().addHighScore(match.getPlayer())) {
            message += "\nTHAT'S TOP 10 WORTHY !!!";
            Play.getInstance().saveHighScores();
        }

        finishedDialog.setMessage(message);
        finishedDialog.build().show();
    }

    private void goBack() {
        Timer.instance().clear();
        strat.finish();
        interactor.reset();
        ScreenState.getInstance().changeState(parentType);
    }

    private void run() {
        Timer.instance().start();
        gameState = gameState.RUNNING;
    }

    private void stop() {
        Timer.instance().stop();
        gameState = gameState.WAITING;
    }
}
