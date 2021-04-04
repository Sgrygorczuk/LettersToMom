package com.mygdx.letterstomom.objects.genericObjects;

public class RespawnPoint extends GenericObjects{
    public RespawnPoint(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }
}
