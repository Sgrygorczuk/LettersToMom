package com.mygdx.letterstomom.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.letterstomom.objects.genericObjects.GenericObjects;

public class AnimatedObjects extends GenericObjects {

    protected TextureRegion[][] spriteSheet;        //The sprite sheet the animation is based on
    protected Animation<TextureRegion> animation;   //The animation
    float animationFrameTime = 4f;                  //How long each frame lasts
    float animationTime = 0;                        //What is the time
    boolean isFacingRight = false;                  //Which way it the object facing

    /**
     * Animated object has one animation and doesn't do much else
     * @param x position
     * @param y position
     * @param spriteSheet sprite sheet
     * @param time length of each frame
     * @param playMode type of loop we want
     */
    public AnimatedObjects(float x, float y, TextureRegion[][] spriteSheet, float time, Animation.PlayMode playMode) {
        super(x, y);
        this.spriteSheet = spriteSheet;
        this.hitBox.height = spriteSheet[0][0].getRegionHeight();
        this.hitBox.width = spriteSheet[0][0].getRegionWidth();
        setUpAnimations(playMode);
        this.animationFrameTime = time;
    }

    /**
     * Purpose: Sets up the animation loops in all of the directions
     */
    protected void setUpAnimations(Animation.PlayMode playMode) {
        animation = setUpAnimation(spriteSheet, 1/animationFrameTime, 0, playMode);
    }

    /**
     * Sets which way the animation is facing
     * @param is facing left or right
     */
    public void setFacingRight(boolean is) {isFacingRight = is;}

    /**
     * Animates a row of the texture region
     * @param textureRegion, the sprite sheet
     * @param duration, how long each frame lasts
     * @param row, which row of the sprite sheet
     * @param playMode, how will the animation play out
     * @return full animation set
     */
    protected Animation<TextureRegion> setUpAnimation(TextureRegion[][] textureRegion, float duration, int row, Animation.PlayMode playMode){
        Animation<TextureRegion> animation = new Animation<>(duration, textureRegion[row]);
        animation.setPlayMode(playMode);
        return animation;
    }

    /**
     * Update the timing var
     * @param delta timing var
     */
    public void update(float delta) {
        animationTime += delta;
    }

    /**
     * Draws the animations
     * @param batch where the animation will be drawn
     */
    public void drawAnimations(SpriteBatch batch){
        TextureRegion currentFrame = spriteSheet[0][0];

        currentFrame = animation.getKeyFrame(animationTime);

        batch.draw(currentFrame, isFacingRight ? hitBox.x + currentFrame.getRegionWidth() : hitBox.x, hitBox.y, isFacingRight ? -hitBox.width : hitBox.width, hitBox.height);
    }

}
