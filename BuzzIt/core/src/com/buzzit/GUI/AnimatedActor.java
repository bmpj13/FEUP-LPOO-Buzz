package com.buzzit.GUI;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class AnimatedActor extends Actor {
    private float timeElapsed;
    private SpriteBatch batch;
    private Animation animation;

    public AnimatedActor(SpriteBatch batch, Animation animation) {
        this.timeElapsed = 0;
        this.batch = batch;
        this.animation = animation;
    }


    @Override
    public void act(float delta) {
        timeElapsed += delta;
        super.act(delta);

        batch.begin();
        batch.draw(animation.getKeyFrame(timeElapsed, true), super.getX() - animation.getKeyFrame(timeElapsed, true).getRegionWidth()/2,
                super.getY() - animation.getKeyFrame(timeElapsed, true).getRegionHeight()/2);
        batch.end();
    }
}
