package com.mygdx.lettersToMom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.lettersToMom.tools.MusicControl;

import static com.mygdx.lettersToMom.Const.FEET_HEAD_HEIGHT;

public class Letter extends SpriteObject{

    protected final Rectangle feetBox;      //Used for collisions with floors
    protected final Rectangle headBox;      //Used for collisions with ceiling's
    protected boolean isFalling = false;    //Used to tell if Cole if falling off a platform

    protected boolean isHit = false;

    public Letter(float x, float y, Texture texture) {
        super(x, y, texture);

        //Feet and Head for collision
        feetBox = new Rectangle(sprite.getX() + sprite.getWidth() + 0.15f, sprite.getY() - FEET_HEAD_HEIGHT, sprite.getWidth() * 0.7f, FEET_HEAD_HEIGHT);
        headBox = new Rectangle(sprite.getX(), sprite.getY() + sprite.getHeight() + FEET_HEAD_HEIGHT, sprite.getWidth()/2f, FEET_HEAD_HEIGHT);
        hitBox.width = texture.getWidth();
        hitBox.height = texture.getHeight();

    }

    public void move(float value){
        updateVelocity(value, (value*value)/15 - value/8);
    }

    public void stop(){
        velocity.x = 0;
        velocity.y = 0;
    }

    /**
     * Purpose: Turns isFalling true whenever Cole doesn't have ground below him
     */
    public void setFalling(boolean falling){isFalling = falling;}
    public boolean getIsFalling(){return isFalling;}

    /**
     * Purpose: Central Update function for Cole all continuous updates come through here
     * @param levelWidth the end of the level
     */
    public void update(float levelWidth, float levelHeight){

        hitBox.y += velocity.y;
        hitBox.x += velocity.x;

        checkIfWorldBound(levelWidth, levelHeight);

        sprite.setX(hitBox.x);
        sprite.setY(hitBox.y);
        sprite.setRotation(rotation);

        if(velocity.x > 0){
            velocity.x -= 0.1f;
            rotation--;
        }
        if(velocity.x < 0 ){
            velocity.x += 0.1f;
            rotation++;
        }

        if(velocity.x >= -0.2f && velocity.x <= 0.2f){ velocity.x = 0; }

        velocity.y -= 0.1f;
    }

    public float getX(){return sprite.getX();}
    public float getY(){return sprite.getY();}

    //========================== Platform Collision ===========================================

    /**
     * Purpose: Check if Cole is touching any platform
     * @param rectangle the platform we're checking against
     * @return tells us if there is any platform below Cole
     */
    public boolean updateCollision(Rectangle rectangle){
        feetBox.x = hitBox.x + hitBox.width * 0.15f;
        feetBox.y = hitBox.y - FEET_HEAD_HEIGHT;

        headBox.x = hitBox.x + hitBox.width/4f;
        headBox.y = hitBox.y + hitBox.height + FEET_HEAD_HEIGHT;

        if(!feetBox.overlaps(rectangle) && !headBox.overlaps(rectangle) && !hitBox.overlaps(rectangle)){
            isHit = false;
        }

        //Vertical
        //==================== Floor Collision ======================
        if(feetBox.overlaps(rectangle)){
            this.hitBox.y = rectangle.y + rectangle.height;
            isFalling = false;  //Is no longer falling
            if(velocity.y != 0){isHit = true;}
            velocity.y = 0;
        }

        //===================== Ceiling Collision =====================
        if(headBox.overlaps(rectangle)){
            this.hitBox.y = rectangle.y - this.hitBox.height - this.headBox.height;
            velocity.y = -1;
            isHit = true;
        }

        //Horizontal
        //====================== On the LEft of colliding Platform ==================
        if(hitBox.overlaps(rectangle) && !headBox.overlaps(rectangle)) {
            if (this.hitBox.x + this.hitBox.width >= rectangle.x
                    && hitBox.x < rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y) {
                this.hitBox.x = rectangle.x - this.hitBox.width;
                velocity.x = 0; //Stops movement
            }
            //=============== On the Right of the Colliding Platform ====================
            else if (this.hitBox.x <= rectangle.x + rectangle.width
                    && this.hitBox.x > rectangle.x
                    && !(this.hitBox.y >= rectangle.y + rectangle.height)
                    && this.hitBox.y >= rectangle.y) {
                this.hitBox.x = rectangle.x + rectangle.width;
                velocity.x = 0; //Stop movement
            }
            isHit = true;
        }

        //Tells us if the user is standing on ground (used for respawn)
        return feetBox.overlaps(rectangle);
    }

    public boolean getIsHit(){return isHit;}

    /* ============================ Utility Functions =========================== */

    /**
     * Purpose: Keeps Object within the level
     * @param levelWidth tells where the map ends
     */
    protected void checkIfWorldBound(float levelWidth, float levelHeight) {
        //Makes sure we're bound by x
        if (hitBox.x < 0) {
            hitBox.x = 0;
            velocity.x = 0;
        }
        else if (hitBox.x + hitBox.width >= levelWidth) {
            hitBox.x = (int) (levelWidth - hitBox.width);
            velocity.x = 0;
        }

        //Makes sure that we stop moving down when we hit the ground
        if (hitBox.y < 0) {
            hitBox.y = 0;
            velocity.y = 0;
        }
        else if (hitBox.y + hitBox.height >= levelHeight){
            hitBox.y = levelHeight - hitBox.height;
        }
    }

    /**
     * Purpose: Draws the circle on the screen using render
     */
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        shapeRenderer.rect(hitBox.getX(), hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
        shapeRenderer.rect(feetBox.getX(), feetBox.getY(), feetBox.getWidth(), feetBox.getHeight());
        shapeRenderer.rect(headBox.getX(), headBox.getY(), headBox.getWidth(), headBox.getHeight());
    }

}
