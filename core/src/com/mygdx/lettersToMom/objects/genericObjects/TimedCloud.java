package com.mygdx.lettersToMom.objects.genericObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.lettersToMom.objects.staticObjects.RainDrop;

public class TimedCloud extends Cloud{

    //Timer counting down until we turn the draw function on/Off
    private static final float PAUSE_TIME = 1.75F;
    private float pauseTimer = PAUSE_TIME;

    private boolean isRaining = true;

    public TimedCloud(float x, float y, float width, float height, Texture texture) {
        super(x, y, width, height, texture);
    }

    public void update(float delta) {
        if(rainDrops.size == 100){ isRaining = false; }

        if(rainDrops.size < 100 && isRaining){
            float randomX = MathUtils.random(hitBox.x + 8, hitBox.x + hitBox.width);
            rainDrops.add(new RainDrop(randomX, hitBox.y, rainDropTexture));
        }

        for(RainDrop rainDrop : rainDrops){ rainDrop.update(); }
        timer(delta);
    }

    /**
     Input: Float delta
     Purpose: Ticks down to turn off invincibility
     */
    public void timer(float delta) {
        pauseTimer -= delta;
        if (pauseTimer <= 0) {
            pauseTimer = PAUSE_TIME;
            isRaining = true;
        }
    }
}
