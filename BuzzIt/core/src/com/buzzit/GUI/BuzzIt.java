package com.buzzit.GUI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BuzzIt extends Game {
	
	@Override
	public void create () {
        setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
