package com.mygdx.letterstomom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;

public class Painting extends SpriteObject {

    private int currentMaxWidth = 0;    //Where the artists is currently

    /**
     * Special spire that will increase it's alpha value as the artist passes it's width
     * @param x position
     * @param y position
     * @param texture texture
     */
    public Painting(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    /**
     * Checks the position of the artist, and then increases the opacity based on how far the artist has
     * walked, if they walked the width of the painting it's at full opacity
     * @param x position of artist
     */
    public void update(float x){
        float width = x - sprite.getX();

        //Lowest opacity can be 0
        if(width < 0){width = 0;}
        //Highest it can be is width
        if(width > sprite.getWidth()){ width = sprite.getWidth();}

        //Check if the artist started walking back, the opacity shouldn't decrease only increase
        if(currentMaxWidth < width){ currentMaxWidth = (int) width; }
        else{ width = currentMaxWidth; }

        //Update the sprite
        sprite.setAlpha((int) width/sprite.getWidth());
    }
}
