package com.mygdx.letterstomom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;

public class Painting extends SpriteObject {

    private int currentMaxWidth = 0;

    public Painting(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    public void update(float x){
        float width = x - sprite.getX();
        if(width < 0){width = 0;}
        if(width > sprite.getWidth()){
            width = sprite.getWidth();
        }

        if(currentMaxWidth < width){ currentMaxWidth = (int) width; }
        else{ width = currentMaxWidth; }

        sprite.setAlpha((int) width/sprite.getWidth());
    }
}
