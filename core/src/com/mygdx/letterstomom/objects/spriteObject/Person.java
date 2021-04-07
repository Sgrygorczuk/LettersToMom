package com.mygdx.letterstomom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Person extends SpriteObject{

    private int state = 0;              //0 - Inactive, 1 - Active, 2 - Talking, 3 - Close up
    private boolean goingLeft = false;  //Which way we're wobbling in
    private final Texture speechBubble; //The speech bubble texturee
    private Sprite speechBubbleSprite;  //The speech bubble sprite

    /**
     * Person has two textures, the close up image and the speech bubble, and then a sprite sheet
     * for the person. The are a controllable unit
     * @param x position
     * @param y position
     * @param spriteSheet sprite sheet
     * @param closeUp close up texture
     * @param speechBubble speech bubble texture
     */
    public Person(float x, float y, TextureRegion[][] spriteSheet, Texture closeUp, Texture speechBubble) {
        super(x, y, spriteSheet);
        texture = closeUp;
        this.speechBubble = speechBubble;
    }

    /**
     * @return the state that the person is in
     */
    public int getState(){return state;}

    /**
     * Moves the person
     * @param velocity speed at which they move
     */
    public void move(float velocity){ this.velocity.x = velocity; }

    /**
     * Move the character from idle standing to user control state
     */
    public void updateStateOne(){
        this.state = 1;
        Sprite sprite1 = new Sprite(spriteSheet[0][state]);
        sprite1.setPosition(sprite.getX(), sprite.getY());
        sprite1.setRotation(sprite.getRotation());
        sprite = sprite1;
    }

    /**
     * Moves the character from user controlled to idle standing while chatting on phone
     * @param x position
     * @param y position
     */
    public void updateStateTwo(float x, float y){
        this.state = 2;
        Sprite sprite1 = new Sprite(spriteSheet[0][state]);
        sprite1.setPosition(sprite.getX(), sprite.getY());
        sprite1.setRotation(0);
        sprite = sprite1;

        speechBubbleSprite = new Sprite(speechBubble);
        speechBubbleSprite.setPosition(x + sprite.getWidth(), y + sprite.getHeight() - 10);
    }

    /**
     * General update function
     */
    public void update(){
        //If user is controlling the character
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
        //If character is ideally chatting
        if(state == 2){
            //If we reach end of rotate, rotate the other way
            if (rotation >= 6) { goingLeft = true; }
            else if (rotation <= -6) { goingLeft = false; }

            //Move rotation
            if(goingLeft){ rotation -= 2; }
            else{ rotation += 2; }

            speechBubbleSprite.setRotation(rotation);
        }
    }

    /**
     * Checks for collsion with any platfroms left or right of the person
     * @param rectangle platform
     */
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

    /**
     * Draws the up close texture
     * @param batch where it's drawn
     * @param x current lvl position
     * @param y current lvl position
     */
    public void drawCloseUp(SpriteBatch batch, float x, float y){
        batch.draw(texture, x, y);
    }

    /**
     * Draws the person, and if in state 2 draws the speech bubble
     * @param batch where it's drawn
     */
    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        if(state == 2){speechBubbleSprite.draw(batch);}
    }
}
