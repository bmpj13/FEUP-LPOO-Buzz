package com.buzzit.gui.screen;

import com.badlogic.gdx.Game;

public class ScreenState {
    private static Game game;
    private static MenuScreen menu;
    private static com.buzzit.gui.screen.SingleplayerScreen singlePlayer;
    private static com.buzzit.gui.screen.SettingsScreen settings;
    private static com.buzzit.gui.screen.HighscoreScreen highScore;
    private static ScreenState ourInstance = new ScreenState();

    public static ScreenState getInstance() { return ourInstance; }


    public enum ScreenType { MENU, SINGLEPLAYER, SETTINGS, HIGHSCORE }


    private ScreenState() {
        menu = new MenuScreen(null);
        singlePlayer = new com.buzzit.gui.screen.SingleplayerScreen(ScreenType.MENU);
        settings = new com.buzzit.gui.screen.SettingsScreen(ScreenType.MENU);
        highScore = new com.buzzit.gui.screen.HighscoreScreen(ScreenType.MENU);
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
    }
}
