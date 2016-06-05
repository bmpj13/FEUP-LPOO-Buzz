package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;

public class ScreenState {
    private static Game game;
    private static MenuScreen menu;
    private static SingleplayerScreen singlePlayer;
    private static Multiplayer1stScreen multiPlayer1;
    private static Multiplayer2ndScreen multiPlayer2;
    private static SettingsScreen settingsScreen;
    private static MultiplayerSettingsScreen multiPlayerSettingsScreen;

    private static ScreenState ourInstance = new ScreenState();

    public static ScreenState getInstance() { return ourInstance; }


    public enum ScreenType { MENU, SINGLEPLAYER, SETTINGS, MULTIPLAYER1, MULTIPLAYER2, MULTIPLAYERSETTINGS}


    private ScreenState() {
        menu = new MenuScreen(game, null);
        singlePlayer = new SingleplayerScreen(game, ScreenType.MENU);
        settingsScreen = new SettingsScreen(game, ScreenType.MENU);
        multiPlayer1 = new Multiplayer1stScreen(game, ScreenType.MENU);
        multiPlayer2 = new Multiplayer2ndScreen(game, ScreenType.MENU);
        multiPlayerSettingsScreen = new MultiplayerSettingsScreen(game, ScreenType.MENU);
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
                game.setScreen(settingsScreen);
                break;

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
        settingsScreen.dispose();
        multiPlayer1.dispose();
    }
}
