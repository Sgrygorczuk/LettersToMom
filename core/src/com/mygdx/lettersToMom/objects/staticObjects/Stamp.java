package com.mygdx.lettersToMom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;

public class Stamp extends StaticObjects {

    public Stamp(float x, float y, Texture texture) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();

    }
}
