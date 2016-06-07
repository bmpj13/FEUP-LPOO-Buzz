package com.buzzit.GUI.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.buzzit.Logic.Play;
import com.buzzit.Logic.Player;

import java.util.ArrayList;

public class HighscoreScreen implements Screen {
    private ScreenState.ScreenType parentType;

    /* Disposables */
    private Stage stage;
    private Skin skin;
    private BitmapFont fontScores;
    private BitmapFont fontHeaders;
    private BitmapFont fontTitle;
    private FreeTypeFontGenerator generator;
    private Table highscoreTable, positionTable, nameTable, pointsTable;

    /* Constants */
    final int WIDTH = 1080;
    final int HEIGHT = 1920;
    final float bigPadX = WIDTH / 10;
    final float smallPadY = HEIGHT / 50;
    final float bigPadY = HEIGHT / 10;

    HighscoreScreen(ScreenState.ScreenType pType) {
        create();
        parentType = pType;
    }

    public void create() {
        Gdx.input.setCatchBackKey(true);

        createSkin();

        Table table = new Table();

        Label title = new Label("HIGHSCORES", skin, "title");
        table.add(title).padBottom(bigPadY);
        table.row();

        // Sub-table
        highscoreTable = new Table();
        positionTable = new Table();
        nameTable = new Table();
        pointsTable = new Table();

        table.add(highscoreTable);
        table.setFillParent(true);

        stage = new Stage(new FillViewport(WIDTH, HEIGHT));
        stage.addActor(table);
    }


    public void createSkin() {
        skin = new Skin();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/good_times.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = WIDTH / 30;

        fontScores = generator.generateFont(parameter);
        skin.add("default", fontScores);

        parameter.size = WIDTH / 20;
        fontHeaders = generator.generateFont(parameter);
        skin.add("headers", fontHeaders);

        parameter.size = WIDTH / 10;
        fontTitle = generator.generateFont(parameter);
        skin.add("title", fontTitle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.CORAL;
        skin.add("default", labelStyle);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("headers");
        labelStyle.fontColor = Color.GOLD;
        skin.add("headers", labelStyle);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("title");
        labelStyle.fontColor = Color.WHITE;
        skin.add("title", labelStyle);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        resetTables();
        updateHighscores();
    }


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
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }


    @Override
    public void hide() {

    }


    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        fontScores.dispose();
        fontHeaders.dispose();
        fontTitle.dispose();
        generator.dispose();
    }


    private void resetTables() {
        highscoreTable.clear();
        positionTable.clear();
        nameTable.clear();
        pointsTable.clear();
    }


    private void updateHighscores() {
        Label name = new Label("Name", skin, "headers");
        Label points = new Label("Points", skin, "headers");
        Label dummy = new Label("", skin, "headers");           // To ensure alignment

        highscoreTable.add(positionTable);
        highscoreTable.add(nameTable).padLeft(bigPadX);
        highscoreTable.add(pointsTable).padLeft(bigPadX);

        positionTable.add(dummy).padBottom(smallPadY).row();
        nameTable.add(name).padBottom(smallPadY).row();
        pointsTable.add(points).padBottom(smallPadY).row();

        ArrayList<Player> playersWithHighscore = Play.getInstance().getHighScores();
        int count = 1;
        for (Player player : playersWithHighscore) {
            Label position = new Label(Integer.toString(count) + ".", skin);
            name = new Label(player.getName(), skin);
            points = new Label(Integer.toString(player.getPoints()), skin);

            positionTable.add(position).padBottom(smallPadY).row();
            nameTable.add(name).padBottom(smallPadY).row();
            pointsTable.add(points).padBottom(smallPadY).row();
            highscoreTable.row();
            count++;
        }
    }
}
