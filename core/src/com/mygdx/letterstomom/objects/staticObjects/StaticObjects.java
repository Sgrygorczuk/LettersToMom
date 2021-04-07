package com.mygdx.letterstomom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.letterstomom.objects.genericObjects.GenericObjects;

public class StaticObjects extends GenericObjects {

    protected Texture texture; //The texture

    /**
     * Generic object but with a texture that can be drawn
     * @param x position
     * @param y position
     * @param texture texture
     */
    public StaticObjects(float x, float y, Texture texture) {
        super(x, y);
        this.texture = texture;
    }

    /**
     * Draws the texture
     * @param batch where it's drawn
     */
    public void draw(SpriteBatch batch){
        batch.draw(texture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }


}
