package com.mygdx.lettersToMom.objects.animatedObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.lettersToMom.objects.genericObjects.GenericObjects;

public class AnimatedObjects extends GenericObjects {

    protected TextureRegion[][] spriteSheet;
    protected Animation<TextureRegion> animation;
    float animationFrameTime = 4f;
    float animationTime = 0;

    boolean isFacingRight = false;

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
