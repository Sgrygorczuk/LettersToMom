package com.mygdx.letterstomom.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.mygdx.letterstomom.Const.WORLD_WIDTH;

/**
 * Cut scene is pretty much a slide show of images
 */
public class CutScene {

    //==================================== Texture Data ============================================
    private final Texture background;           //The image that will wipe across
    private float backgroundX = WORLD_WIDTH;    //Position of the wipe image

    private final TextureRegion[][] cutSceneSpiteSheet; //The list of images that will be the slides
    private final int cutSceneLength;                   //Tells us how many slides there are

    //====================== Flags =================================================================
    private boolean transitionPaused = true;    //Tells us if the image is being paused
    private boolean switched = false;           //Tells us if the slide switch has occured or not
    private int place = 0;                      //Tells us on which slide we're on
    float x;                                    //Sets the x coordinate, if the player isn't in 0,0
    float y;                                    //Sets the y coordinate, if the player isn't in 0,0

    /**
     * Constructor
     * @param x position in the level
     * @param y position in the level
     * @param transitionPath texture name that is used to wipe
     * @param cutScenePath texture name of the cut scene
     * @param cutSceneLength amount of slides in the cut scene
     */
    public CutScene(float x, float y, String transitionPath, String cutScenePath, int cutSceneLength){
        this.x = x;
        this.y = y;

        background = new Texture(Gdx.files.internal(transitionPath));

        Texture cutSceneTexturePath = new Texture(Gdx.files.internal(cutScenePath));
        cutSceneSpiteSheet = new TextureRegion(cutSceneTexturePath).split(
                cutSceneTexturePath.getWidth(), cutSceneTexturePath.getHeight()/cutSceneLength);


        this.cutSceneLength = cutSceneLength;
    }

    /**
     * Moves us to the next slide if there is one, else tells us to exit cut
     * @return is there is another slide or if we're at the end of the cut scene
     */
    public boolean isThereANextSlide(){
        if(transitionPaused){ transitionPaused = false; }
        return place + 1 < cutSceneLength;
    }

    /**
     * Updates the x and y position on the level
     * @param x position on the level
     * @param y position on the level
     */
    public void updatePosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return tells us if the wipe is occuring or not
     */
    public boolean getPaused(){return transitionPaused;}

    /**
     * Updates the wipe effect when active
     */
    public void updateTransition(){
        if(!transitionPaused){
            //Moves the wipe
            backgroundX -= 7.5f;

            //If the wipe covers the previous screen switch the slide behind it
            if(backgroundX <= 0 && !switched){
                place++;
                switched = true;
            }

            //Once the wipe is off screen reset it to the right side of the screen,
            //readying for another wipe
            if(backgroundX <= -WORLD_WIDTH){
                backgroundX = WORLD_WIDTH;
                transitionPaused = true;
                switched = false;
            }
        }
    }

    /**
     * Draws the cut scene screen and the wipe screen
     * @param batch where it's drawn
     */
    public void draw(SpriteBatch batch){
        batch.draw(cutSceneSpiteSheet[place][0], x, y);

        batch.draw(background, x + backgroundX, y);
    }

}
