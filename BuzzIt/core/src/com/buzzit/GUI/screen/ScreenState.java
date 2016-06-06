package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;

public class ScreenState {
    private static Game game;
    private static MenuScreen menu;
    private static SingleplayerScreen singlePlayer;
    private static SettingsScreen settings;
    //private static HighscoreScreen highScore;

    private static Multiplayer1stScreen multiPlayer1;
    private static Multiplayer2ndScreen multiPlayer2;
    private static MultiplayerSettingsScreen multiPlayerSettingsScreen;

    private static ScreenState ourInstance = new ScreenState();

    public static ScreenState getInstance() { return ourInstance; }


    public enum ScreenType { MENU, SINGLEPLAYER, SETTINGS, HIGHSCORE, MULTIPLAYER1, MULTIPLAYER2, MULTIPLAYERSETTINGS };


    private ScreenState() {
        menu = new MenuScreen(null);
        singlePlayer = new SingleplayerScreen(ScreenType.MENU);
        settings = new SettingsScreen(ScreenType.MENU);
        //highScore = new HighscoreScreen(ScreenType.MENU);
        multiPlayer1 = new Multiplayer1stScreen(ScreenType.MENU);
        multiPlayer2 = new Multiplayer2ndScreen(ScreenType.MENU);
        multiPlayerSettingsScreen = new MultiplayerSettingsScreen(ScreenType.MENU);
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

            /*
            case HIGHSCORE:
                game.setScreen(highScore);
                break;*/

            case MULTIPLAYER1:
                game.setScreen(multiPlayer1);
                break;

            case MULTIPLAYERSETTINGS:
                game.setScreen(multiPlayerSettingsScreen);
                break;

            case MULTIPLAYER2:
                game.setScreen(multiPlayer2);
                break;
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        menu.dispose();
        singlePlayer.dispose();
        settings.dispose();
        multiPlayer1.dispose();
        multiPlayer2.dispose();
        multiPlayerSettingsScreen.dispose();
        //highScore.dispose();
    }
}
