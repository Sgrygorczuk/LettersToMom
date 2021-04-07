package com.mygdx.letterstomom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bird extends StaticObjects{

    float velocityX = 2.5f;         //How fast the bird is moving
    boolean isFacingRight = true;   //Which way the bird is facing

    /**
     * A bird is an obstacle that will hurt and respawn the player if touched
     * @param x position
     * @param y position
     * @param texture texture
     */
    public Bird(float x, float y, Texture texture) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();
    }

    /**
     * Update the postion of the bird
     */
    public void update(){
        if(isFacingRight){ hitBox.x += velocityX; }
        else{ hitBox.x -= velocityX; }
    }

    /**
     * Updates the collision with platforms, if touches a platform it switch which side it faces
     * @param rectangle platform
     */
    public void checkCollision(Rectangle rectangle) {
        //Horizontal
        //====================== On the LEft of colliding Platform ==================
        if (hitBox.overlaps(rectangle)) {
            if (this.hitBox.x + this.hitBox.width >= rectangle.x
                    && hitBox.x < rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y) {
                isFacingRight = !isFacingRight;
                this.hitBox.x = rectangle.x - this.hitBox.width;
            }
            //=============== On the Right of the Colliding Platform ====================
            else if (this.hitBox.x <= rectangle.x + rectangle.width
                    && this.hitBox.x > rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y) {
                isFacingRight = !isFacingRight;
                this.hitBox.x = rectangle.x + rectangle.width;
            }
            velocityX = 0;
        }
        else{ velocityX = 2.5f; }
    }

    /**
     * Specail drawing that takes into the account which way the bird is facing
     * @param batch where it's drawn
     */
    public void draw(SpriteBatch batch){
        batch.draw(texture, !isFacingRight ? hitBox.x + hitBox.width : hitBox.x, hitBox.y, !isFacingRight ? -hitBox.width : hitBox.width, hitBox.height);

    }
}
