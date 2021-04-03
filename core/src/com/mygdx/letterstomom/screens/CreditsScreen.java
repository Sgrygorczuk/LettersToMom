package com.mygdx.letterstomom.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.letterstomom.main.lettersToMom;
import com.mygdx.letterstomom.screens.textures.CreditsScreenTextures;
import com.mygdx.letterstomom.tools.MusicControl;
import com.mygdx.letterstomom.tools.TextAlignment;

import static com.mygdx.letterstomom.Const.TEXT_OFFSET;
import static com.mygdx.letterstomom.Const.WORLD_HEIGHT;
import static com.mygdx.letterstomom.Const.WORLD_WIDTH;


public class CreditsScreen extends ScreenAdapter{

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private final SpriteBatch batch = new SpriteBatch();
    private Viewport viewport;
    private Camera camera;

    //===================================== Tools ==================================================
    private final lettersToMom mom;
    private final TextAlignment textAlignment = new TextAlignment();
    private CreditsScreenTextures creditsScreenTextures;
    private MusicControl musicControl;            //Plays Music

    //====================================== Fonts =================================================
    private BitmapFont bitmapFont = new BitmapFont();

    private Array<String> credits = new Array<>();
    private float position =  -TEXT_OFFSET;
    private boolean collectedAll;
    private boolean stampTime = false;
    private boolean playedSound = false;
    private boolean rewardTime = false;

    //Timer counting down until we turn the draw function on/Off
    private static final float REWARD_TIME = 10f;
    private static final float STAMP_TIME = 2f;
    private float stampTimer = STAMP_TIME;



    /**
     * Purpose: The Constructor used when loading up the game for the first time showing off the logo
     * @param mom game object with data
     */
    public CreditsScreen(lettersToMom mom, boolean collectedAll) {
        System.out.println("Im in");
        this.mom = mom;
        this.collectedAll = collectedAll;
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
        showObjects();
        showCredits();           //Loads the stuff into the asset manager
        creditsScreenTextures = new CreditsScreenTextures();
        musicControl = new MusicControl(mom.getAssetManager());
        musicControl.showMusic(0);
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
        bitmapFont.setColor(Color.WHITE);
    }

    private void showCredits(){
        credits.add("Letters To Mom");
        credits.add(" ");
        credits.add("Programming");
        credits.add("Sebastian Grygorczuk");
        credits.add(" ");
        credits.add(" ");
        credits.add("Art");
        credits.add("Sebastian Grygorczuk");
        credits.add(" ");
        credits.add(" ");
        credits.add("Additional Art");
        credits.add("Irina Grygorczuk");
        credits.add(" ");
        credits.add(" ");
        credits.add("Music");
        credits.add("Yuriy Lehki");
        credits.add(" ");
        credits.add(" ");
        credits.add("SFX");
        credits.add("From Freesound.org");
        credits.add(" ");
        credits.add("Snoman");
        credits.add("gravel4.wav");
        credits.add("gravel1.wav");
        credits.add(" ");
        credits.add("Nathan_Lomeli");
        credits.add("mail slot shutting.wav");
        credits.add(" ");
        credits.add("F.M.Audio");
        credits.add("Crumpling Piece of Paper.wav");
        credits.add(" ");
        credits.add("vixuxx");
        credits.add("crow.wav");
        credits.add(" ");
        credits.add("nknverpacker");
        credits.add("watersplash04.wav");
        credits.add(" ");
        credits.add("Fupicat");
        credits.add("Videogame Menu Highlight");
        credits.add(" ");
        credits.add("BitzHawk");
        credits.add("Draw.wav");
        credits.add(" ");
        credits.add("David2317");
        credits.add("13_soplo.wav");
        credits.add(" ");
        credits.add(" ");
        credits.add("Thank You");
        credits.add("College Game Jam");
        credits.add(" ");
        credits.add(" ");
        credits.add("Thank You");
        credits.add("Mom");

    }


    //=================================== Execute Time Methods =====================================

    /**
     Purpose: Central function for the game, everything that updates run through this function
     */
    @Override
    public void render(float delta) {
        update(delta);
        //Once time is done go back to main screen


        draw();
    }

    //=================================== Updating Methods =========================================

    /**
     * Purpose: Updates the variable of the progress bar, when the whole thing is load it turn on game screen
     * @param delta timing variable
     */
    private void update(float delta) {

        position += 1;

        if(position - TEXT_OFFSET * credits.size > WORLD_HEIGHT){
            stampTime = true;
        }

        if(stampTime) {
            if(!playedSound){
                musicControl.playSFX(2, 0.5f);
                playedSound = true;
            }
            stampTimer -= delta;
            if (stampTimer < 0) {
                stampTimer = REWARD_TIME;
                if(collectedAll){
                    rewardTime = true;
                    stampTime = false;
                }
                else{
                    mom.setScreen(new MenuScreen(mom));
                }
            }
        }

        if(rewardTime){
            stampTimer -= delta;
            if (stampTimer < 0) {
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

        batch.begin();
        bitmapFont.getData().setScale(0.5f);
        for(int i = 0; i < credits.size; i++){
            textAlignment.centerText(batch, bitmapFont, credits.get(i), WORLD_WIDTH/2f, position - TEXT_OFFSET * i);
        }
        bitmapFont.getData().setScale(0.5f);
        if(stampTime){
            batch.draw(creditsScreenTextures.stampTexture, WORLD_WIDTH/2f - creditsScreenTextures.stampTexture.getWidth(),
                    WORLD_HEIGHT/2f - creditsScreenTextures.stampTexture.getHeight(),
                    creditsScreenTextures.stampTexture.getWidth()*2, creditsScreenTextures.stampTexture.getHeight()*2);
            String collected = "Stamps in the Wind";
            if(collectedAll){ collected = "Collected and Stamped";}
            textAlignment.centerText(batch, bitmapFont, collected, WORLD_WIDTH/2f, WORLD_HEIGHT/2f - creditsScreenTextures.stampTexture.getHeight() * 2f);
        }

        if(rewardTime){
            batch.draw(creditsScreenTextures.backgroundTexture, 0,0);
        }

        batch.end();
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