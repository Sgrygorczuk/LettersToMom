package com.mygdx.lettersToMom.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.mygdx.lettersToMom.Const.WORLD_WIDTH;

public class CutScene {

    private Texture background;
    private float backgroundX = WORLD_WIDTH;
    private boolean transitionPaused = true;
    private boolean switched = false;

    float x;
    float y;

    private TextureRegion[][] cutSceneSpiteSheet;
    private int cutSceneLength;

    private int place = 0;

    public CutScene(float x, float y, String transitionPath, String cutScenePath, int cutSceneLength){
        this.x = x;
        this.y = y;

        background = new Texture(Gdx.files.internal(transitionPath));

        Texture cutSceneTexturePath = new Texture(Gdx.files.internal(cutScenePath));
        cutSceneSpiteSheet = new TextureRegion(cutSceneTexturePath).split(
                cutSceneTexturePath.getWidth(), cutSceneTexturePath.getHeight()/cutSceneLength);


        this.cutSceneLength = cutSceneLength;
    }

    public boolean nextSlide(float delta){
        if(transitionPaused){
            transitionPaused = false;
        }
        return place + 1 < cutSceneLength;
    }

    public void updatePosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public boolean getPaused(){return transitionPaused;}

    public int getPlace() { return place; }

    public void updateTransition(float delta){
        if(!transitionPaused){
            backgroundX -= 7.5f;
            if(backgroundX <= 0 && !switched){
                place++;
                switched = true;
            }
            if(backgroundX <= -WORLD_WIDTH){
                backgroundX = WORLD_WIDTH;
                transitionPaused = true;
                switched = false;
            }
        }
    }

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

    public void draw(SpriteBatch batch){
        batch.draw(cutSceneSpiteSheet[place][0], x, y);

        batch.draw(background, x + backgroundX, y);
    }

}
