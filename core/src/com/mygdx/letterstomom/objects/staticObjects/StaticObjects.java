package com.mygdx.letterstomom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.letterstomom.objects.genericObjects.GenericObjects;

public class StaticObjects extends GenericObjects {

    protected Texture texture;

    public StaticObjects(float x, float y, Texture texture) {
        super(x, y);
        this.texture = texture;
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }


}
