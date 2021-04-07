package com.mygdx.letterstomom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.letterstomom.objects.genericObjects.GenericObjects;

public class SpriteObject extends GenericObjects {

    protected Vector2 velocity;                 //Speed of the object
    protected Texture texture;                  //A texture
    protected TextureRegion[][] spriteSheet;    //A sprite sheet
    protected Sprite sprite;                    //A sprite
    protected int rotation;                     //The rotation of the spite


    /**
     * Used for textured sprites, like letter, and panting
     * @param x position
     * @param y position
     * @param texture texture
     */
    public SpriteObject(float x, float y, Texture texture) {
        super(x, y);
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.velocity = new Vector2(0,0);
    }

    /**
     * Used for people who have a sprite sheet of actions
     * @param x position
     * @param y positron
     * @param spriteSheet sprite sheet
     */
    public SpriteObject(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y);
        hitBox.width = spriteSheet[0][0].getRegionWidth();
        hitBox.height = spriteSheet[0][0].getRegionHeight();
        this.spriteSheet = spriteSheet;
        this.sprite = new Sprite(spriteSheet[0][0]);
        this.sprite.setPosition(x, y);
        this.velocity = new Vector2(0,0);
    }

    /**
     * Updates the speed at which the sprite is moving
     * @param x xSpeed
     * @param y ySpeed
     */
    protected void updateVelocity(float x, float y){
        velocity.x = x;
        velocity.y = y;
    }

    /**
     * Draws the sprite
     * @param batch where it's drawn
     */
    public void draw(SpriteBatch batch){ sprite.draw(batch); }
}
