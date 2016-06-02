package com.buzzit.GUI;

import com.badlogic.gdx.Game;
import com.buzzit.GUI.screen.*;

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
