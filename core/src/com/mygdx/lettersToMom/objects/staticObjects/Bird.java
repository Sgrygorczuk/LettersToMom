package com.mygdx.lettersToMom.objects.staticObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bird extends StaticObjects{

    float velocityX = 3;
    boolean isFacingRight = true;

    public Bird(float x, float y, Texture texture) {
        super(x, y, texture);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();
    }

    public void update(){
        if(isFacingRight){
            hitBox.x += velocityX;
        }
        else{
            hitBox.x -= velocityX;
        }
    }

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
        else{
            velocityX = 3;
        }
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, !isFacingRight ? hitBox.x + hitBox.width : hitBox.x, hitBox.y, !isFacingRight ? -hitBox.width : hitBox.width, hitBox.height);

    }
}
