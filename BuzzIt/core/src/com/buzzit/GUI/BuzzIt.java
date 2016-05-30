package com.buzzit.GUI;

import com.badlogic.gdx.Game;

public class BuzzIt extends Game {
	
	@Override
	public void create () {
        setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
