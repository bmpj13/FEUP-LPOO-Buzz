package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;

public class ScreenState {
    private static Game game;
    private static MenuScreen menu;
    private static SingleplayerScreen singlePlayer;
    private static SettingsScreen settings;
    private static HighscoreScreen highScore;
    private static ScreenState ourInstance = new ScreenState();

    public static ScreenState getInstance() { return ourInstance; }


    public enum ScreenType { MENU, SINGLEPLAYER, SETTINGS, HIGHSCORE };


    private ScreenState() {
        menu = new MenuScreen(null);
        singlePlayer = new SingleplayerScreen(ScreenType.MENU);
        settings = new SettingsScreen(ScreenType.MENU);
        highScore = new HighscoreScreen(ScreenType.MENU);
    }

    public void setGame(Game g) {
        game = g;
    }

    public void changeState(ScreenType screenType) {

        switch (screenType) {
            case MENU:
                game.setScreen(menu);
                break;

            case SINGLEPLAYER:
                game.setScreen(singlePlayer);
                break;

            case SETTINGS:
                game.setScreen(settings);
                break;

            case HIGHSCORE:
                game.setScreen(highScore);
                break;
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        menu.dispose();
        singlePlayer.dispose();
        settings.dispose();
        highScore.dispose();
    }
}
