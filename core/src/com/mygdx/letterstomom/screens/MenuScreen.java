package com.mygdx.letterstomom.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.letterstomom.main.lettersToMom;
import com.mygdx.letterstomom.objects.animatedObjects.AnimatedObjects;
import com.mygdx.letterstomom.screens.textures.MenuScreenTextures;
import com.mygdx.letterstomom.tools.MusicControl;
import com.mygdx.letterstomom.tools.TextAlignment;

import static com.mygdx.letterstomom.Const.WORLD_HEIGHT;
import static com.mygdx.letterstomom.Const.WORLD_WIDTH;

public class MenuScreen extends ScreenAdapter{

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private final SpriteBatch batch = new SpriteBatch();
    private Viewport viewport;
    private Camera camera;

    //===================================== Tools ==================================================
    private MusicControl musicControl;
    private MenuScreenTextures menuScreenTextures;
    private final lettersToMom mom;
    private final TextAlignment textAlignment = new TextAlignment();

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();

    //============================================= Misc ===========================================
    private AnimatedObjects flowerRight;
    private AnimatedObjects flowerLeft;
    private AnimatedObjects logo;

    //================================ Set Up ======================================================

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: BasicTemplet
     */
    MenuScreen(lettersToMom mom) { this.mom = mom;}

    /**
     Purpose: Updates the dimensions of the screen
     Input: The width and height of the screen
     */
    @Override
    public void resize(int width, int height) { viewport.update(width, height); }

    //===================================== Show ===================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();           //Sets up camera through which objects are draw through
        menuScreenTextures = new MenuScreenTextures();
        showObjects();          //Sets up the font
        musicControl.showMusic(0);
        musicControl.setMusicVolume(0.1f);
        musicControl.playSFX(4, 0.5f);
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
     */
    private void showCamera(){
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
     */
    private void showObjects(){
        musicControl = new MusicControl(mom.getAssetManager());

        if(mom.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = mom.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(0.55f);
        bitmapFont.setColor(Color.BLACK);

        flowerLeft = new AnimatedObjects(0, 0, menuScreenTextures.flowerSpriteSheet, 4f, Animation.PlayMode.LOOP);
        flowerLeft.setFacingRight(true);
        flowerRight = new AnimatedObjects(WORLD_WIDTH - menuScreenTextures.flowerSpriteSheet[0][0].getRegionWidth(),
                0, menuScreenTextures.flowerSpriteSheet, 4f, Animation.PlayMode.LOOP);

        logo = new AnimatedObjects(WORLD_WIDTH/2f - (menuScreenTextures.logoSpriteSheet[0][0].getRegionWidth() *0.9f)/2f,
                WORLD_HEIGHT/4f, menuScreenTextures.logoSpriteSheet, 10f, Animation.PlayMode.NORMAL);
        logo.setHeight(menuScreenTextures.logoSpriteSheet[0][0].getRegionHeight() * 0.9f);
        logo.setWidth(menuScreenTextures.logoSpriteSheet[0][0].getRegionWidth() * 0.9f);
    }

    //=================================== Execute Time Methods =====================================

    /**
     Purpose: Central function for the game, everything that updates run through this function
     */
    @Override
    public void render(float delta) {
        update(delta);       //Update the variables
        draw();
    }

    /**
     Purpose: Updates all the moving components and game variables
     Input: @delta - timing variable
     */
    private void update(float delta){
        flowerRight.update(delta);
        flowerLeft.update(delta);
        logo.update(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
            mom.setScreen(new LoadingScreen(mom, 1));
            musicControl.stopMusic();
        }
    }

    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing function, from here everything gets drawn
     */
    private void draw() {
        clearScreen();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(menuScreenTextures.backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        flowerRight.drawAnimations(batch);
        flowerLeft.drawAnimations(batch);
        logo.drawAnimations(batch);
        batch.draw(menuScreenTextures.buttonTexture,
                WORLD_WIDTH/2f - menuScreenTextures.buttonTexture.getWidth()/2f, 10);
        batch.end();
    }

    /**
     *  Purpose: Clear the screen
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Purpose: Gets rid of all visuals
    */
    @Override
    public void dispose(){}
}