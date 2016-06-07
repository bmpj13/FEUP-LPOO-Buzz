package com.buzzit.gui;

import com.badlogic.gdx.Game;
import com.buzzit.gui.screen.ScreenState;

public class BuzzIt extends Game {
	
	@Override
	public void create () {
		com.buzzit.gui.screen.ScreenState.getInstance().setGame(this);
		com.buzzit.gui.screen.ScreenState.getInstance().changeState(ScreenState.ScreenType.MENU);
	}

	@Override
	public void render () {
		super.render();
	}
}
