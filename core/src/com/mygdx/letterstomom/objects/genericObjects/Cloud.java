package com.mygdx.letterstomom.objects.genericObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.letterstomom.objects.staticObjects.RainDrop;

public class Cloud extends GenericObjects{

    protected final Array<RainDrop> rainDrops = new Array<>(); //Collection of all the rain drops
    Texture rainDropTexture;    //Texture of the rain drop

    /**
     * Constructor of the cloud that always rains
     * @param x position
     * @param y position
     * @param width width
     * @param height height
     * @param texture texture of the rain drop
     */
    public Cloud(float x, float y, float width, float height, Texture texture) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
        rainDropTexture = texture;
    }

    /**
     * Will continuously spawn 300 drain drops and then update their locations
     */
    public void update(){
        if(rainDrops.size < 300){
            float randomX = MathUtils.random(hitBox.x + 8, hitBox.x + hitBox.width);
            rainDrops.add(new RainDrop(randomX, hitBox.y, rainDropTexture));
        }

        for(RainDrop rainDrop : rainDrops){ rainDrop.update(); }
    }

    /**
     * Checks if any of the rain drops are touching that rectangle
     * if they are remove the rain drop
     * @param rectangle given platform or person
     */
    public void isRainColliding(Rectangle rectangle){
        for(RainDrop rainDrop : rainDrops){
            if(rainDrop.isColliding(rectangle)){
                rainDrops.removeValue(rainDrop, true);
            }
        }
    }

    /**
     * Special collision for letter
     * @param rectangle the letter
     * @return tells us if the letter is touching the cloud or any of the rain drops
     */
    public boolean isLetterColliding(Rectangle rectangle){
        //Checks if the letter touched the cloud
        if(isColliding(rectangle)){ return true; }

        //Checks if the letter touched a rain drop
        for(RainDrop rainDrop : rainDrops){ if(rainDrop.isColliding(rectangle)){ return true; } }

        return false;
    }

    /**
     * Draws all of the rain drops
     * @param batch where it's drawn
     */
    public void draw(SpriteBatch batch){
        for(RainDrop rainDrop : rainDrops){
            rainDrop.draw(batch);
        }
    }

}
