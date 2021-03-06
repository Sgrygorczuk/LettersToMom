package com.mygdx.letterstomom.objects.genericObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.mygdx.letterstomom.Const.TILED_HEIGHT;
import static com.mygdx.letterstomom.Const.TILED_WIDTH;


public class GenericObjects {
    protected Rectangle hitBox;

    /**
     * Constructor that makes a hit box
     * @param x portion of hitBox
     * @param y portion of hitBox
     */
    public GenericObjects(float x , float y){
        this.hitBox = new Rectangle(x, y, TILED_WIDTH, TILED_HEIGHT);
    }

    /**
     * @return Rectangle
     */
    public Rectangle getHitBox(){return hitBox;}

    public float getX(){return hitBox.x;}
    public void setX(float x){hitBox.x = x;}

    public float getY(){return hitBox.y;}
    public void setY(float y){hitBox.y = y;}

    public void setWidth(float width){hitBox.width = width;}
    public float getWidth(){return hitBox.width;}

    public void setHeight(float height){hitBox.height = height;}
    public float getHeight(){return hitBox.height;}

    /**
     * Tells us if we are touching anything else
     * @param other the new hit box
     * @return if we are touching that hit box
     */
    public boolean isColliding(Rectangle other) { return this.hitBox.overlaps(other); }

    /**
     * Purpose: Draws the circle on the screen using render
     */
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}
