package com.mygdx.letterstomom.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Flower extends AnimatedObjects{
    /**
     * Little flowers that don't do anything other than
     * @param x position
     * @param y position
     * @param spriteSheet sprite sheet
     * @param playMode type of loop
     */
    public Flower(float x, float y, TextureRegion[][] spriteSheet, Animation.PlayMode playMode) {
        super(x, y, spriteSheet, 4f, playMode);
        hitBox.height = 32;
        hitBox.width = 32;
    }
}
