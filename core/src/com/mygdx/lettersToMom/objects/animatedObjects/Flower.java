package com.mygdx.lettersToMom.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Flower extends AnimatedObjects{
    public Flower(float x, float y, TextureRegion[][] spriteSheet, Animation.PlayMode playMode) {
        super(x, y, spriteSheet, playMode);
        hitBox.height = 32;
        hitBox.width = 32;
    }
}
