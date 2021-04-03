package com.mygdx.letterstomom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

public class RainDrop extends StaticObjects{

    public RainDrop(float x, float y, Texture texture) {
        super(x, y, texture);
        hitBox.height = 16;
        hitBox.width = 16;
    }

    public void update(){
        hitBox.y -= 3;
    }
}
