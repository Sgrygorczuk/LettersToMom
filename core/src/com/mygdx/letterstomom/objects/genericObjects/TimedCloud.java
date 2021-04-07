package com.mygdx.letterstomom.objects.genericObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.letterstomom.objects.staticObjects.RainDrop;

public class TimedCloud extends Cloud{

    //Timer counting down until we turn the draw function on/Off
    private static final float PAUSE_TIME = 1.75F;
    private float pauseTimer = PAUSE_TIME;

    //Tells us if we should spawn rain drops
    private boolean isRaining = true;

    /**
     * Specialized cloud that has timed rain
     * @param x position
     * @param y position
     * @param width width
     * @param height height
     * @param texture rain texture
     */
    public TimedCloud(float x, float y, float width, float height, Texture texture) {
        super(x, y, width, height, texture);
    }

    /**
     * Will spawn 100 drain drops, if we hit that number we stop raining and wait
     * for the timer to start again, updates position of the rain drops
     * @param delta timing var
     */
    public void update(float delta) {
        //If we reach 100 stop making rain drops
        if(rainDrops.size == 100){ isRaining = false; }

        //If we're raining create a new rain drop
        if(rainDrops.size < 100 && isRaining){
            float randomX = MathUtils.random(hitBox.x + 8, hitBox.x + hitBox.width);
            rainDrops.add(new RainDrop(randomX, hitBox.y, rainDropTexture));
        }

        //Update the position of each rain drop
        for(RainDrop rainDrop : rainDrops){ rainDrop.update(); }

        //Starts the rain
        startRainingTimer(delta);
    }

    /**
     * Counts down until we can start raining again
     * @param delta timing var
     */
    public void startRainingTimer(float delta) {
        pauseTimer -= delta;
        if (pauseTimer <= 0) {
            pauseTimer = PAUSE_TIME;
            isRaining = true;
        }
    }
}
