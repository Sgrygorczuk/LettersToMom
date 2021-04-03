package com.mygdx.letterstomom.objects.genericObjects;

public class Water extends GenericObjects{
    public Water(float x, float y, float width, float height) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
    }
}
