package com.mygdx.letterstomom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;


public class Person extends SpriteObject{

    private int state = 0; //0 - Inactive, 1 - Active, 2 - Talking, 3 - Close up
    private boolean goingLeft = false;
    private Texture speechBubble;
    private Sprite speechBubbleSprite;

    public Person(float x, float y, TextureRegion[][] spriteSheet, Texture closeUp, Texture speechBubble) {
        super(x, y, spriteSheet);
        texture = closeUp;
        this.speechBubble = speechBubble;
    }

    public int getState(){return state;}

    public void move(float velocity){
        this.velocity.x = velocity;
    }

    public void updateStateOne(){
        this.state = 1;
        Sprite sprite1 = new Sprite(spriteSheet[0][state]);
        sprite1.setPosition(sprite.getX(), sprite.getY());
        sprite1.setRotation(sprite.getRotation());
        sprite = sprite1;
    }

    public void updateStateTwo(float x, float y){
        this.state = 2;
        Sprite sprite1 = new Sprite(spriteSheet[0][state]);
        sprite1.setPosition(sprite.getX(), sprite.getY());
        sprite1.setRotation(0);
        sprite = sprite1;

        speechBubbleSprite = new Sprite(speechBubble);
        speechBubbleSprite.setPosition(x + sprite.getWidth(), y + sprite.getHeight() - 10);
    }

    public void update(){
        if(state == 1) {
            //If we're moving let him rotate
            if (goingLeft && (velocity.x > 0 || velocity.x < 0)) { rotation += 2; }
            else if((velocity.x > 0 || velocity.x < 0)){ rotation -= 2; }

            //If he's moving slow him down
            if (velocity.x > 0) { velocity.x -= 0.5f; }
            if (velocity.x < 0) { velocity.x += 0.5f; }

            //If we reach end of rotate, rotate the other way
            if (rotation >= 10) { goingLeft = false; }
            else if (rotation <= -10) { goingLeft = true; }

            //Update the vars
            hitBox.x += velocity.x;
            sprite.setX(hitBox.x);
            sprite.setRotation(rotation);
        }
        if(state == 2){
            //If we reach end of rotate, rotate the other way
            if (rotation >= 6) { goingLeft = true; }
            else if (rotation <= -6) { goingLeft = false; }

            if(goingLeft){ rotation -= 2; }
            else{ rotation += 2; }

            speechBubbleSprite.setRotation(rotation);
        }
    }

    public void checkCollision(Rectangle rectangle){
        //Horizontal
        //====================== On the LEft of colliding Platform ==================
        if(hitBox.overlaps(rectangle)) {
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
        }
    }

    public void drawCloseUp(SpriteBatch batch, float x, float y){
        batch.draw(texture, x, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        if(state == 2){speechBubbleSprite.draw(batch);}
    }
}
