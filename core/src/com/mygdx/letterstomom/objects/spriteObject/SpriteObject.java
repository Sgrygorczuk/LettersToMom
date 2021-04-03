package com.mygdx.letterstomom.objects.spriteObject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.letterstomom.objects.genericObjects.GenericObjects;

public class SpriteObject extends GenericObjects {

    protected Vector2 velocity;
    protected Texture texture;
    protected TextureRegion[][] spriteSheet;
    protected Sprite sprite;
    protected int rotation;


    public SpriteObject(float x, float y, Texture texture) {
        super(x, y);
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.velocity = new Vector2(0,0);
    }

    public SpriteObject(float x, float y, TextureRegion[][] spriteSheet) {
        super(x, y);
        hitBox.width = spriteSheet[0][0].getRegionWidth();
        hitBox.height = spriteSheet[0][0].getRegionHeight();
        this.spriteSheet = spriteSheet;
        this.sprite = new Sprite(spriteSheet[0][0]);
        this.sprite.setPosition(x, y);
        this.velocity = new Vector2(0,0);
    }

    protected void updateVelocity(float x, float y){
        velocity.x = x;
        velocity.y = y;
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }
}
