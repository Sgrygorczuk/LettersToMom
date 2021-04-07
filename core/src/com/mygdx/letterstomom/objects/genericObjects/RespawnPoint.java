package com.mygdx.letterstomom.objects.genericObjects;

public class RespawnPoint extends GenericObjects{
    /**
     * Location of a point where the user can come back after getting hurt
     * @param x position
     * @param y position
     * @param width width
     * @param height height
     */
    public RespawnPoint(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }
}
