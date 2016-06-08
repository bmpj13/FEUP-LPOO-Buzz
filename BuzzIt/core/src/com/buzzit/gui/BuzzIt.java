package com.buzzit.gui;

import com.badlogic.gdx.Game;
import com.buzzit.gui.screen.ScreenState;

public class BuzzIt extends Game {
	
	@Override
	public void create () {

		ScreenState.getInstance().setGame(this);
		ScreenState.getInstance().changeState(ScreenState.ScreenType.MENU);

	}

	@Override
	public void render () {
		super.render();
	}
}
