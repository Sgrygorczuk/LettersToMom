package com.mygdx.letterstomom.objects.genericObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.letterstomom.objects.staticObjects.RainDrop;

public class Cloud extends GenericObjects{

    protected final Array<RainDrop> rainDrops = new Array<>();

    Texture rainDropTexture;

    public Cloud(float x, float y, float width, float height, Texture texture) {
        super(x, y);
        hitBox.width = width;
        hitBox.height = height;
        rainDropTexture = texture;
    }

    public void update(){
        if(rainDrops.size < 300){
            float randomX = MathUtils.random(hitBox.x + 8, hitBox.x + hitBox.width);
            rainDrops.add(new RainDrop(randomX, hitBox.y, rainDropTexture));
        }

        for(RainDrop rainDrop : rainDrops){
            rainDrop.update();
        }
    }

    public void isRainColliding(Rectangle rectangle){
        for(RainDrop rainDrop : rainDrops){
            if(rainDrop.isColliding(rectangle)){
                rainDrops.removeValue(rainDrop, true);
            }
        }
    }

    public boolean isLetterColliding(Rectangle rectangle){

        if(isColliding(rectangle)){
            return true;
        }

        for(RainDrop rainDrop : rainDrops){
            if(rainDrop.isColliding(rectangle)){ return true; }
        }

        return false;
    }

    public void draw(SpriteBatch batch){
        for(RainDrop rainDrop : rainDrops){
            rainDrop.draw(batch);
        }
    }

}
