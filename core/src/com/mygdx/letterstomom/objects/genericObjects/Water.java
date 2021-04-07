package com.mygdx.letterstomom.objects.genericObjects;

public class Water extends GenericObjects{
    /**
     * Genric box that will respawn the player if is touched
     * @param x position
     * @param y position
     * @param width width
     * @param height height
     */
    public Water(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }
}
