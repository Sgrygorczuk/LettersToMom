package com.mygdx.letterstomom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

public class RainDrop extends StaticObjects{

    /**
     * Rain drops are drop from the cloud and make player respawn
     * @param x position
     * @param y position
     * @param texture texture
     */
    public RainDrop(float x, float y, Texture texture) {
        super(x, y, texture);
        hitBox.height = 16;
        hitBox.width = 16;
    }

    /**
     * Update the y position of the rain drop
     */
    public void update(){ hitBox.y -= 3; }
}
