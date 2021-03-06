package com.mygdx.letterstomom.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.letterstomom.main.lettersToMom;
import com.mygdx.letterstomom.screens.textures.LoadingScreenTextures;
import com.mygdx.letterstomom.tools.DebugRendering;

import static com.mygdx.letterstomom.Const.LOGO_HEIGHT;
import static com.mygdx.letterstomom.Const.LOGO_WIDTH;
import static com.mygdx.letterstomom.Const.WORLD_HEIGHT;
import static com.mygdx.letterstomom.Const.WORLD_WIDTH;

public class LoadingScreen extends ScreenAdapter{

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private final SpriteBatch batch = new SpriteBatch();
    private Viewport viewport;
    private Camera camera;

    //===================================== Tools ==================================================
    private final lettersToMom mom;
    private LoadingScreenTextures loadingScreenTextures;

    //====================================== Fonts =================================================
    private BitmapFont bitmapFont = new BitmapFont();

    //=================================== Miscellaneous Vars =======================================
    private final int screenPath; //Tells us which screen to go to from here

    //Timing variables, keeps the logo on for at least 2 second
    private boolean loadingFirstTime = false;
    private boolean logoDoneFlag = false;
    private static final float LOGO_TIME = 2F;
    private float logoTimer = LOGO_TIME;

    private Sprite sun;
    private int rotation = 0;


    /**
     * Purpose: The Constructor used when loading up the game for the first time showing off the logo
     * @param mom game object with data
     */
    public LoadingScreen(lettersToMom mom) {
        this.mom = mom;
        this.screenPath = 0;
        this.loadingFirstTime = true;
    }

    /**
     * Purpose: General Constructor for moving between screens
     * @param mom game object with data
     * @param screenPath tells us which screen to go to from here
     */
    public LoadingScreen(lettersToMom mom, int screenPath) {
        this.mom = mom;
        this.screenPath = screenPath;
    }

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
        //Sets up the camera
        showCamera();           //Sets up camera through which objects are draw through
        loadingScreenTextures = new LoadingScreenTextures();
        DebugRendering debugRendering = new DebugRendering(camera);
        debugRendering.setShapeRendererBackgroundShapeType(ShapeRenderer.ShapeType.Filled);
        showObjects();
        loadAssets();           //Loads the stuff into the asset manager
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
     */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);		//
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
     */
    private void showObjects(){
        bitmapFont.setColor(Color.BLACK);
        if(mom.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = mom.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(0.45f);

        sun = new Sprite(loadingScreenTextures.sunTexture);
        sun.setPosition(WORLD_WIDTH - sun.getWidth(), 0);

    }

    /**
     * Purpose: Loads all the data needed for the asset manager, and set up logo to be displayed
    */
    private void loadAssets(){
        //===================== Load Fonts to Asset Manager ========================================
        BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "font_assets.atlas";
        mom.getAssetManager().load("Fonts/Font.fnt", BitmapFont.class, bitmapFontParameter);

        //=================== Load Music to Asset Manager ==========================================
        mom.getAssetManager().load("Music/MenuMusic.mp3", Music.class);
        mom.getAssetManager().load("Music/GameMusic.mp3", Music.class);
        mom.getAssetManager().load("Music/PersonMusic.mp3", Music.class);
        mom.getAssetManager().load("Music/CutSceneMusic.mp3", Music.class);

        //========================== Load SFX to Asset Manager =====================================
        mom.getAssetManager().load("SFX/Water.wav", Sound.class);
        mom.getAssetManager().load("SFX/Wind.wav", Sound.class);
        mom.getAssetManager().load("SFX/Paper.wav", Sound.class);
        mom.getAssetManager().load("SFX/Collected.wav", Sound.class);
        mom.getAssetManager().load("SFX/Bird.wav", Sound.class);
        mom.getAssetManager().load("SFX/Pencil.wav", Sound.class);
        mom.getAssetManager().load("SFX/Mailbox.wav", Sound.class);
        mom.getAssetManager().load("SFX/Footsteps.wav", Sound.class);

        //========================= Load Tiled Maps ================================================
        mom.getAssetManager().load("Tiled/Map.tmx", TiledMap.class);
    }

    //=================================== Execute Time Methods =====================================

    /**
     Purpose: Central function for the game, everything that updates run through this function
     */
    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    //=================================== Updating Methods =========================================

    /**
     * Purpose: Updates the variable of the progress bar, when the whole thing is load it turn on game screen
     * @param delta timing variable
     */
    private void update(float delta) {
        //If everything is loaded go to the new screen
        if (mom.getAssetManager().update() && logoDoneFlag) { goToNewScreen();}
        //Else keep loading
        else { mom.getAssetManager().getProgress(); }

        updateTimer(delta);
        sun.setRotation(rotation);

        rotation++;
    }

    /**
     * Purpose: Counts down until the logo has been on screen long enough
     * @param delta timer to count down
     */
    private void updateTimer(float delta) {
        logoTimer -= delta;
        if (logoTimer <= 0) {
            logoTimer = LOGO_TIME;
            logoDoneFlag = true;
        }
    }

    /**
     * Purpose: Allows us to go to a different screen each time we enter the LoadingScreen
     */
    private void goToNewScreen(){
        switch (screenPath){
            case 0:{
                mom.setScreen(new MenuScreen(mom));
                break;
            }
            case 1:{
                mom.setScreen(new MainScreen(mom, 0));
                break;
            }
            default:{
                mom.setScreen(new MenuScreen(mom));
            }
        }
    }

    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing Function
    */
    private void draw() {
        //================== Clear Screen =======================
        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        //======================== Draws ==============================
        //Logo First Time We boot up
        if(loadingFirstTime){
            batch.begin();
            batch.draw(loadingScreenTextures.logoTexture, WORLD_WIDTH/2f - LOGO_WIDTH/2f, WORLD_HEIGHT/2f - LOGO_HEIGHT/2f,   LOGO_WIDTH, LOGO_HEIGHT);
            batch.end();
        }
        //Loading Screen with Progress bar
        else{
            batch.begin();
            batch.draw(loadingScreenTextures.backgroundTexture, 0, 0);
            sun.draw(batch);
            batch.draw(loadingScreenTextures.loadingTexture, WORLD_WIDTH -loadingScreenTextures.loadingTexture.getWidth() - sun.getWidth() - 5f, 0);
            batch.end();
        }

    }

    /**
     *  Purpose: Sets screen color
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Purpose: Gets rid of all visuals
    */
    @Override
    public void dispose() {
    }
}