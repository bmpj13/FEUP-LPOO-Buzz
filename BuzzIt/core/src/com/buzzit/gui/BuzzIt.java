package com.buzzit.gui;

import com.badlogic.gdx.Game;
import com.buzzit.gui.screen.ScreenState;

public class BuzzIt extends Game {
	
	@Override
	public void create () {
<<<<<<< HEAD:BuzzIt/core/src/com/buzzit/gui/BuzzIt.java
		ScreenState.getInstance().setGame(this);
		ScreenState.getInstance().changeState(ScreenState.ScreenType.MENU);
=======
		com.buzzit.gui.screen.ScreenState.getInstance().setGame(this);
		com.buzzit.gui.screen.ScreenState.getInstance().changeState(ScreenState.ScreenType.MENU);
>>>>>>> 54f063b275a656d66f13c4695856cf06444d0915:BuzzIt/core/src/com/buzzit/GUI/BuzzIt.java
	}

	@Override
	public void render () {
		super.render();
	}
}
