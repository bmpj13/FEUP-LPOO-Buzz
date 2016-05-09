package com.buzzit.GUI;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;

public class SuperScreen implements Screen {

    protected Game game;
    protected Screen parent = null;

    ArrayList<Screen> screens = new ArrayList<Screen>();


    public void create(Screen s) {
        screens.add(s);
        Gdx.input.setCatchBackKey(true);
    }


    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {

            if (parent != null) {
                game.setScreen(parent);
            }
            else {
                disposeScreens();
                Gdx.app.exit();
            }
        }
    }


    @Override
    public void resize(int width, int height) {

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

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }


    private void disposeScreens() {
        for (Screen s: screens)
            s.dispose();
    }
}
