package com.buzzit.gui;

import com.badlogic.gdx.Game;

public class BuzzIt extends Game {
	
	@Override
	public void create () {
		com.buzzit.gui.screen.ScreenState.getInstance().setGame(this);
		com.buzzit.gui.screen.ScreenState.getInstance().changeState(com.buzzit.gui.screen.ScreenState.ScreenType.MENU);
	}

	@Override
	public void render () {
		super.render();
	}
}
