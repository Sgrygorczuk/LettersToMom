package com.mygdx.letterstomom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

public class Stamp extends StaticObjects {

    /**
     * Stamps are collectibles the player can gather
     * @param x position
     * @param y position
     * @param texture texture
     */
    public Stamp(float x, float y, Texture texture) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();

    }
}
