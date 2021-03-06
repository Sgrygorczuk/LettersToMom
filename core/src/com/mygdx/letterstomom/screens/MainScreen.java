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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.letterstomom.main.lettersToMom;
import com.mygdx.letterstomom.objects.animatedObjects.Flower;
import com.mygdx.letterstomom.objects.genericObjects.Cloud;
import com.mygdx.letterstomom.objects.genericObjects.Platform;
import com.mygdx.letterstomom.objects.genericObjects.RespawnPoint;
import com.mygdx.letterstomom.objects.genericObjects.TimedCloud;
import com.mygdx.letterstomom.objects.genericObjects.Water;
import com.mygdx.letterstomom.objects.spriteObject.Letter;
import com.mygdx.letterstomom.objects.spriteObject.Painting;
import com.mygdx.letterstomom.objects.spriteObject.Person;
import com.mygdx.letterstomom.objects.staticObjects.Bird;
import com.mygdx.letterstomom.objects.staticObjects.Stamp;
import com.mygdx.letterstomom.screens.textures.MainScreenTextures;
import com.mygdx.letterstomom.tools.CutScene;
import com.mygdx.letterstomom.tools.DebugRendering;
import com.mygdx.letterstomom.tools.MusicControl;
import com.mygdx.letterstomom.tools.TextAlignment;
import com.mygdx.letterstomom.tools.TiledSetUp;

import static com.mygdx.letterstomom.Const.WORLD_HEIGHT;
import static com.mygdx.letterstomom.Const.WORLD_WIDTH;


class MainScreen extends ScreenAdapter {

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private Viewport viewport;			                     //The screen where we display things
    private Camera camera;				                     //The camera viewing the viewport
    private final SpriteBatch batch = new SpriteBatch();	 //Batch that holds all of the textures

    //===================================== Tools ==================================================
    private final lettersToMom mom;      //Game object that holds the settings
    private DebugRendering debugRendering;        //Draws debug hit-boxes
    private MusicControl musicControl;            //Plays Music
    private final TextAlignment textAlignment = new TextAlignment();    //The text alignment
    private CutScene cutSceneStart;         //Cut scene that plays at the start
    private CutScene cutSceneEnd;           //Cut scene that plays at the end of the game

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();  //Font used for the user interaction
    private MainScreenTextures mainScreenTextures;     //Collection of textures used
    private TiledSetUp tiledSetUp;                     //Takes all the data from tiled

    //============================================= Flags ==========================================
    private final boolean developerMode = false;      //Developer mode shows hit boxes and phone data
    private boolean endFlag = false;            //Tells us game has been lost
    private boolean inCutScene = true;          //Tells us if we're in a cut scene or not
    private boolean cameraPan = false;          //Tells us the camera is paning and player can't move
    private float xCameraDelta = 0;             //Keeps track of how far the camera has moved (to update menus)
    private float yCameraDelta = 0;             //Keeps track of how far the camera has moved (to update menus)
    private float initialX = 0;                 //Tells us where the letter initially took off from
    private boolean isLetter = true;            //Tells us if we're a letter or a person
    private int inControl = 0;                  //0 - Umbrella, 1 - Grandma, 3 - Painter, 4 - Mailman
    private boolean isCloseUp = false;          //Tells us if we're zoomed in an the letter the person is holding

    private static final float WALK_TIME = 1F;  //Used to play the walking SFX when a person
    private float walkTime = 0;                 //Timer to play the walking SFX when a person



    //=================================== Miscellaneous Vars =======================================
    private final Array<String> levelNames = new Array<>(); //Names of all the lvls in order
    private final int tiledSelection;                       //Which tiled map is loaded in


    //================================ Set Up ======================================================

    //============================== Player ============================
    private Letter letter;          //The player

    //=========================== Physical Objects =====================
    private final Array<Platform> platforms = new Array<>();    //All the platforms
    private final Array<Person> people = new Array<>();         //All the people
    private Painting painting;                                  //Paining that artist draws

    private final Array<Cloud> clouds = new Array<>();          //Clouds that don't stop raining
    private final Array<TimedCloud> timedClouds = new Array<>(); //Clouds that rain on a timer

    private final Array<Stamp> stamps = new Array<>();          //All of the stamps
    private int counterTotal;                                   //Initial size of the stamp array

    private final Array<Flower> flowers = new Array<>();        //All the flowers
    private final Array<Bird> birds = new Array<>();            //All the birds
    private final Array<Water> waters = new Array<>();          //All the bodies of water
    private final Array<RespawnPoint> respawnPoints = new Array<>();    //All the respawn points
    private RespawnPoint currentRespawnPoint;                   //Where the player currently respawn from

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: BasicTemplet
    */
    MainScreen(lettersToMom mom, int tiledSelection) {
        this.mom = mom;

        this.tiledSelection = tiledSelection;
        levelNames.add("Tiled/Map.tmx");
    }


    /**
    Purpose: Updates the dimensions of the screen
    Input: The width and height of the screen
    */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    //===================================== Show ===================================================

    /**
     * Purpose: Central function for initializing the screen
     */
    @Override
    public void show() {
        showCamera();       //Set up the camera
        showObjects();      //Sets up player and font
        mainScreenTextures = new MainScreenTextures();
        showTiled();
        musicControl.showMusic(3);
        musicControl.setMusicVolume(0.1f);
        if(developerMode){debugRendering.showRender();}    //If in developer mode sets up the redners
    }


    /**
     * Purpose: Sets up all the objects imported from tiled
     */
    private void showTiled() {
        tiledSetUp = new TiledSetUp(mom.getAssetManager(), batch, levelNames.get(tiledSelection));

        //================================= Letter =======================================
        Array<Vector2> letterPositions = tiledSetUp.getLayerCoordinates("Letter");
        for(int i = 0; i < letterPositions.size; i++){
            letter =  new Letter(letterPositions.get(i).x, letterPositions.get(i).y, mainScreenTextures.letterTexture);
        }

        //================================= Platforms =======================================
        Array<Vector2> platformsPositions = tiledSetUp.getLayerCoordinates("Platforms");
        Array<Vector2> platformsDimensions = tiledSetUp.getLayerDimensions("Platforms");
        for(int i = 0; i < platformsPositions.size; i++){
            platforms.add(new Platform(platformsPositions.get(i).x, platformsPositions.get(i).y, platformsDimensions.get(i).x, platformsDimensions.get(i).y));
        }

        //================================= Water =======================================
        Array<Vector2> waterPositions = tiledSetUp.getLayerCoordinates("Water");
        Array<Vector2> waterDimensions = tiledSetUp.getLayerDimensions("Water");
        for(int i = 0; i < waterPositions.size; i++){
            waters.add(new Water(waterPositions.get(i).x, waterPositions.get(i).y, waterDimensions.get(i).x, waterDimensions.get(i).y));
        }

        //================================= Stamps =======================================
        Array<Vector2> stampPositions = tiledSetUp.getLayerCoordinates("Stamp");
        for(int i = 0; i < stampPositions.size; i++){
            stamps.add(new Stamp(stampPositions.get(i).x, stampPositions.get(i).y, mainScreenTextures.stampTexture));
        }
        counterTotal = stamps.size;

        //================================= Bird =======================================
        Array<Vector2> birdPositions = tiledSetUp.getLayerCoordinates("Bird");
        for(int i = 0; i < birdPositions.size; i++){
            birds.add(new Bird(birdPositions.get(i).x, birdPositions.get(i).y, mainScreenTextures.birdTexture));
        }

        //================================= Flowers =======================================
        Array<Vector2> flowerPositions = tiledSetUp.getLayerCoordinates("Flower");
        for(int i = 0; i < flowerPositions.size; i++){
            flowers.add(new Flower(flowerPositions.get(i).x, flowerPositions.get(i).y, mainScreenTextures.flowerSpriteSheet, Animation.PlayMode.LOOP));
        }

        //================================= Respawn Points =======================================
        Array<Vector2> respawnPointPositions = tiledSetUp.getLayerCoordinates("RespawnPoint");
        Array<Vector2> respawnPointDimensions = tiledSetUp.getLayerDimensions("RespawnPoint");
        for(int i = 0; i < respawnPointPositions.size; i++){
            respawnPoints.add(new RespawnPoint(respawnPointPositions.get(i).x, respawnPointPositions.get(i).y,
                    respawnPointDimensions.get(i).x, respawnPointDimensions.get(i).y));
        }
        currentRespawnPoint = respawnPoints.get(0);

        //================================= People =======================================
        Array<Vector2> peoplePositions = tiledSetUp.getLayerCoordinates("People");
        Array<String> peopleNames = tiledSetUp.getLayerNames("People");
        for(int i = 0; i < peoplePositions.size; i++){
            peopleAdded(peoplePositions.get(i), peopleNames.get(i));
        }

        //================================= Painting =======================================
        Array<Vector2> paintingPositions = tiledSetUp.getLayerCoordinates("Painting");
        painting = new Painting(paintingPositions.get(0).x, paintingPositions.get(0).y,
                mainScreenTextures.paintingTexture);

        //================================= Clouds =======================================
        Array<Vector2> cloudPositions = tiledSetUp.getLayerCoordinates("Cloud");
        Array<Vector2> cloudDimensions = tiledSetUp.getLayerDimensions("Cloud");
        for(int i = 0; i < cloudPositions.size; i++){
            clouds.add(new Cloud(cloudPositions.get(i).x, cloudPositions.get(i).y,
                    cloudDimensions.get(i).x, cloudDimensions.get(i).y, mainScreenTextures.rainDropTexture));
        }


        //================================= Clouds =======================================
        Array<Vector2> timedCloudPositions = tiledSetUp.getLayerCoordinates("TimedCloud");
        Array<Vector2> timedCloudDimensions = tiledSetUp.getLayerDimensions("TimedCloud");
        for(int i = 0; i < timedCloudPositions.size; i++){
            timedClouds.add(new TimedCloud(timedCloudPositions.get(i).x, timedCloudPositions.get(i).y,
                    timedCloudDimensions.get(i).x, timedCloudDimensions.get(i).y, mainScreenTextures.rainDropTexture));
        }

    }

    /**
     * Given the name a different type of person is added, their textures are different
     * @param position where the person is at the map
     * @param name what is their distinction
     */
    private void peopleAdded(Vector2 position, String name){
        switch (name){
            case "Umbrella":{
                people.add(new Person(position.x, position.y, mainScreenTextures.umbrellaSpriteSheet,
                        mainScreenTextures.closeUpOneTexture, mainScreenTextures.speechBubbleOneTexture));
                break;
            }
            case "Granny":{
                people.add(new Person(position.x, position.y, mainScreenTextures.grannySpriteSheet,
                        mainScreenTextures.closeUpTwoTexture, mainScreenTextures.speechBubbleTwoTexture));
                break;
            }
            case "Painter":{
                people.add(new Person(position.x, position.y, mainScreenTextures.painterSpriteSheet,
                        mainScreenTextures.closeUpThreeTexture, mainScreenTextures.speechBubbleThreeTexture));
                break;
            }
            case "Mailman":{
                people.add(new Person(position.x, position.y, mainScreenTextures.mailmanSpriteSheet,
                        mainScreenTextures.closeUpFourTexture, mainScreenTextures.speechBubbleFourTexture));
                break;
            }
        }
    }

    /**
     * Purpose: Sets up the camera through which all the objects are view through
    */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);  //Stretches the image to fit the screen
    }

    /**
     * Purpose: Sets up objects such as debugger, musicControl, fonts and others
    */
    private void showObjects(){
        debugRendering = new DebugRendering(camera);
        musicControl = new MusicControl(mom.getAssetManager());

        if(mom.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = mom.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(1f);

        cutSceneStart = new CutScene(0,0, "UI/Background.png",
                "Sprites/CutScene1.png", 7);

        cutSceneEnd = new CutScene(0, 0, "UI/Background.png",
                "Sprites/CutScene2.png", 5);
    }

    //=================================== Execute Time Methods =====================================

    /**
    Purpose: Central function for the game, everything that updates run through this function
    */
    @Override
    public void render(float delta) {
        //Updates Cut scene
        if(inCutScene){ updateCutScenes(); }
        //Updates close up
        else if(isCloseUp){ updateCloseUp(); }
        //Updates game play
        else{
            update(delta);     //If the game is not paused update the variables
            draw();                                 //Draws everything
            if (developerMode) { debugRender(); }   //If developer mode is on draws hit-boxes
        }
    }

    //===================================== Debug ==================================================

    /**
     Purpose: Draws hit-boxes for all the objects
     */
    private void debugRender(){
        debugRendering.startEnemyRender();
        debugRendering.endEnemyRender();

        debugRendering.startUserRender();
        letter.drawDebug(debugRendering.getShapeRendererUser());
        debugRendering.endUserRender();

        debugRendering.startBackgroundRender();
        debugRendering.setShapeRendererBackgroundColor(Color.YELLOW);
        for(Platform platform : platforms){platform.drawDebug(debugRendering.getShapeRendererBackground());}
        for(Person person : people){person.drawDebug(debugRendering.getShapeRendererBackground());}
        debugRendering.endBackgroundRender();

        debugRendering.startCollectibleRender();
        for(RespawnPoint respawnPoint : respawnPoints){respawnPoint.drawDebug(debugRendering.getShapeRendererCollectible());}
        debugRendering.endCollectibleRender();
    }
    //=================================== Updating Methods =========================================

    /**
     * Performs the updating and drawing of cut scenes
     */
    private void updateCutScenes(){
        //Gets a generic cut scene object that will be updated
        CutScene cutScene;

        //Gets the data from the cut scene we're looking at
        if(endFlag){ cutScene = cutSceneEnd; }
        else{ cutScene = cutSceneStart; }

        //Update the wipe
        cutScene.updateTransition();

        //If cut scene is not being wiped user can click to get to the next slide
        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && cutSceneEnd.getPaused()){
            inCutScene = cutScene.isThereANextSlide();
        }

        //User can skip cut scene with ESC
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){ inCutScene = false; }

        //Saves the data from the generic cut scene to the world objects
        if(endFlag){
            cutSceneEnd = cutScene;
            //If we reach end send user to credits
            if(!inCutScene){
                musicControl.stopMusic();
                mom.setScreen(new CreditsScreen(mom, stamps.size == 0));
            }
        }
        else{
            cutSceneStart = cutScene;
            //If we reach end start playing in game music
            if(!inCutScene){ musicControl.switchMusic(1); }
        }

        drawCutScene(cutScene);
    }

    /**
     * Central cut scene drawing function
     * @param cutScene the cutscene who's image we're gonna take
     */
    private void drawCutScene(CutScene cutScene){
        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        //======================== Draws ==============================
        batch.begin();
        cutScene.draw(batch);
        if(cutScene.getPaused()){
            batch.draw(mainScreenTextures.buttonTexture, WORLD_WIDTH - mainScreenTextures.buttonTexture.getWidth() - 5 + xCameraDelta, yCameraDelta + 5);
        }
        batch.end();
    }

    /**
     * Updates and draws the close up of a letter being held
     */
    private void updateCloseUp(){

        //If user clicks anything the leave the letter
        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
            isCloseUp = false;
            musicControl.playSFX(5, 0.5f);
        }

        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        //======================== Draws ==============================
        batch.begin();
        people.get(inControl).drawCloseUp(batch, xCameraDelta, yCameraDelta);
        batch.draw(mainScreenTextures.closeUpButtonTexture, xCameraDelta + WORLD_WIDTH - mainScreenTextures.buttonTexture.getWidth() - 5, yCameraDelta + 5);
        batch.end();
    }

    /**
    Purpose: Updates all the moving components and game variables
    Input: @delta - timing variable
    */
    private void update(float delta){
        //===================================== Camera Updates ====================================
        if(!cameraPan){handleInput(delta);}
        if(isLetter){ updateLetterCamera(); }
        else{ updatePeopleCamera(); }

        //=============================== Object Updates ===========================================
        letter.update(tiledSetUp.getLevelWidth(), tiledSetUp.getLevelHeight());
        for(Person person : people){ person.update(); }
        for(Cloud cloud : clouds){ cloud.update(); }
        for(TimedCloud timedCloud : timedClouds){ timedCloud.update(delta); }
        for(Flower flower : flowers){flower.update(delta);}
        for(Bird bird : birds){bird.update();}
        painting.update(people.get(2).getX() + people.get(2).getWidth());

        //================================ Respawn =================================================
        if(currentRespawnPoint.getX() == respawnPoints.get(respawnPoints.size - 1).getX()){
            endFlag = true;
            inCutScene = true;
            cutSceneEnd.updatePosition(xCameraDelta, yCameraDelta);
        }

        //================================ Collision ===============================================
        updateCollision();
    }


    /**
     * Purpose: Central Input Handling function
     */
    private void handleInput(float delta) {
        //If the user is currently controlling a letter
        if(isLetter) {
            //If the player moves left, right or down make the wind sound
            if(Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
                    Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                musicControl.playSFX(0, 0.5f);
            }

            //If the user is moving left move and build up momentum
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                letter.move(initialX);
                initialX -= 0.5;
            }
            //If the user is moving right move right and build up momentum
            else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                letter.move(initialX);
                initialX += 0.5;
            }
            //If user is not moving in either way kill the momentum
            else { initialX = 0; }

            //Caps the max speed at being +/- 7
            if (initialX > 7) { initialX = 7; }
            if (initialX < -7) { initialX = -7; }

            //If user pushes down stop momentum and send letter flying down
            if(Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                initialX = 0;
                letter.moveDown();
            }
        }
        //If the player is a person
        else{
            //If player is walking left
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                people.get(inControl).move(-4);
                walkTime -= delta;
                //Counts down till we play the walking SFX again
                if (walkTime <= 0) {
                    walkTime = WALK_TIME;
                    musicControl.playSFX(7, 0.1f);
                }
            }
            //If play is walking right
            else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                people.get(inControl).move(4);
                walkTime -= delta;
                //Counts down till we play the walking SFX again
                if (walkTime <= 0) {
                    walkTime = WALK_TIME;
                    musicControl.playSFX(7, 0.1f);
                }
            }
        }

        //Used to enter Dev Mode
        //if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) { developerMode = !developerMode; }
    }

    /**
     * Central collsion funtion
     */
    private void updateCollision(){
        isCollidingPlatform();
        isCollidingPeople();
        isLetterCollidingEnemy();
        isStampColliding();
        isRespawnPointColliding();
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isCollidingPlatform() {
        for (int i = 0; i < platforms.size; i++) {
            letter.updateCollision(platforms.get(i).getHitBox());

            //if(letter.getIsHit()){musicControl.playSFX(8, 0.1f);}

            for (Cloud cloud : clouds) { cloud.isRainColliding(platforms.get(i).getHitBox()); }

            for (TimedCloud timedCloud : timedClouds) { timedCloud.isRainColliding(platforms.get(i).getHitBox()); }

            for (Bird bird : birds) { bird.checkCollision(platforms.get(i).getHitBox()); }

            for (Person person : people) { person.checkCollision(platforms.get(i).getHitBox()); }
        }

    }

    /**
     * Purpose: Check if the letter is touching any person, and if any rain drop is touching person
     */
    private void isCollidingPeople() {
        for (int i = 0; i < people.size; i++) {
            if(people.get(i).isColliding(letter.getHitBox()) && people.get(i).getState() == 0){
                isLetter = false;
                isCloseUp = true;
                inControl = i;
                people.get(i).updateStateOne();
                musicControl.playSFX(5, 0.5f);
                musicControl.switchMusic(2);
            }
            for (Cloud cloud : clouds) {
                cloud.isRainColliding(people.get(i).getHitBox());
            }
        }
    }

    /**
     * Purpose: Check the letter is touching a stamp, if so remove it and play sound
     */
    private void isStampColliding() {
        if(isLetter) {
            for (Stamp stamp : stamps) {
                if(stamp.isColliding(letter.getHitBox())){
                    stamps.removeValue(stamp, true);
                    musicControl.playSFX(2, 0.5f);
                }
            }
        }
    }

    /**
     * Purpose: Check if Letter is touching something that would hurt it and send it back
     */
    private void isLetterCollidingEnemy() {
        if(isLetter) {
            for (Cloud cloud : clouds) {
                if(cloud.isLetterColliding(letter.getHitBox())){
                    respawn();
                    musicControl.playSFX(1, 0.5f);
                }
            }

            for (TimedCloud timedCloud : timedClouds) {
                if(timedCloud.isLetterColliding(letter.getHitBox())){
                    respawn();
                    musicControl.playSFX(1, 0.5f);
                }
            }


            for (int i = 0; i < waters.size; i++) {
                if (waters.get(i).isColliding(letter.getHitBox())) {
                    respawn();
                    musicControl.playSFX(1, 0.5f);
                }
            }

            for (int i = 0; i < birds.size; i++) {
                if (birds.get(i).isColliding(letter.getHitBox())) {
                    respawn();
                    musicControl.playSFX(3, 0.5f);
                }
            }
        }
    }

    /**
     * Resets the letter to be at it's last respawn point
     */
    private void respawn(){
        cameraPan = true;
        letter.setX(currentRespawnPoint.getX());
        letter.setY(currentRespawnPoint.getY());
        camera.position.y = currentRespawnPoint.getY() + 5;
        letter.stop();
    }

    /**
     * Purpose: Checks if a person or letter touches a respawn point
     */
    private void isRespawnPointColliding() {
        //If person touches respawn point lose control of the person
        if(!isLetter) {
            for(RespawnPoint respawnPoint : respawnPoints){
                if(respawnPoint.isColliding(people.get(inControl).getHitBox())){
                    people.get(inControl).updateStateTwo(xCameraDelta, yCameraDelta);
                    currentRespawnPoint.setX(respawnPoint.getX());
                    currentRespawnPoint.setY(respawnPoint.getY());
                    letter.setX(respawnPoint.getX());
                    letter.setY(respawnPoint.getY());
                    isLetter = true;
                    cameraPan = true;
                    musicControl.playSFX(6, 0.5f);
                    if(respawnPoint.getX() != respawnPoints.get(respawnPoints.size - 1).getX()){
                        musicControl.switchMusic(1);
                    }
                    else{
                        musicControl.switchMusic(3);
                    }
                }
            }
        }
        //If letter hits small hit box save the location and ring a bell
        else{
            for(RespawnPoint respawnPoint : respawnPoints){
                if(letter.isColliding(respawnPoint.getHitBox()) && currentRespawnPoint.getX() != respawnPoint.getX()){
                    currentRespawnPoint.setX(respawnPoint.getX());
                    currentRespawnPoint.setY(respawnPoint.getY());
                    musicControl.playSFX(6, 0.5f);
                }
            }
        }
    }

    /**
     * Purpose: Resize the menuStage viewport in case the screen gets resized (Desktop)
     *          Moving the camera if that's part of the game
     */
    public void updateLetterCamera() {
        //============================= Camera following player ============================
        if(!cameraPan) {
            float cameraX = camera.position.x;
            float cameraY;
            //Updates Camera if the X positions has changed
            if(letter.getX() >= camera.position.x + WORLD_WIDTH/2f){
                cameraX = tiledSetUp.getLevelWidth() - WORLD_WIDTH/2;
            }
            else if (((letter.getX() > WORLD_WIDTH / 2f)
                    && (letter.getX() < tiledSetUp.getLevelWidth() - WORLD_WIDTH / 2f))) {
                cameraX = letter.getX();
            }

            //Always follow letter
            cameraY = letter.getY();
            //If we hit the bounds of the map stop at the world/2f distance from the world end
            if(cameraY < WORLD_HEIGHT/2f){ cameraY = WORLD_HEIGHT/2f; }
            else if(cameraY > tiledSetUp.getLevelHeight() - WORLD_HEIGHT/2f){ cameraY = tiledSetUp.getLevelHeight() - WORLD_HEIGHT/2f; }

            camera.position.set(cameraX, cameraY, camera.position.z);
            camera.update();
            tiledSetUp.updateCamera(camera);

            //Updates the change of camera to keep the UI moving with the player
            xCameraDelta = camera.position.x - WORLD_WIDTH / 2f;
            yCameraDelta = camera.position.y - WORLD_HEIGHT / 2f;
        }
        //=========================== Camera panning to player after death ========================
        else{
            float cameraX;
            float cameraY;

            //===================================== X ==============================================
            //If we reached the end and the Y still needs to move just stay still
            if(Math.round(letter.getX()) == Math.round(camera.position.x) ||
                    Math.round(letter.getX()) >= Math.round(camera.position.x - 2f) &&
                    Math.round(letter.getX()) <= Math.round(camera.position.x + 2f)){
                cameraX = camera.position.x;
            }
            else if((camera.position.x - letter.getX())/100 > 5 && letter.getX() < camera.position.x){
                //If we're to the left of the camera move left
                cameraX = camera.position.x - (camera.position.x - letter.getX()) / 100;
            }
            else if(letter.getX() < camera.position.x){
                cameraX = camera.position.x - 5;
            }
            else if((letter.getX() - camera.position.x)/ 100 > 5 && letter.getX() > camera.position.x){
                cameraX = camera.position.x + (letter.getX() - camera.position.x) / 100;
            }
            else{
                cameraX = camera.position.x + 5;
            }

            //If we reached the boarder of the screen don't go further
            if(cameraX < WORLD_WIDTH/2f){ cameraX = WORLD_WIDTH/2f; }
            else if(cameraX > tiledSetUp.getLevelWidth() - WORLD_WIDTH/2f){ cameraX = tiledSetUp.getLevelWidth() - WORLD_WIDTH/2f;}


            //===================================== Y ===============================================
            //If we reached the end and the X still needs to move just stay still
            if(Math.round(letter.getY()) == Math.round(camera.position.y) ||
                    Math.round(letter.getY()) == Math.round(camera.position.y - 1) ||
                    Math.round(letter.getY()) == Math.round(camera.position.y + 1)){
                cameraY = camera.position.y;
            }
            //If we're to the below of the camera move below
            else if(letter.getY() < camera.position.y){ cameraY = camera.position.y - 3; }
            //If we're to the above of the camera move above
            else { cameraY = camera.position.y + 3;}

            //If we reach the boarder of the screen to go further
            if(cameraY < WORLD_HEIGHT/2f){ cameraY = WORLD_HEIGHT/2f; }
            else if(cameraY > tiledSetUp.getLevelHeight() - WORLD_HEIGHT/2f){ cameraY = tiledSetUp.getLevelHeight() - WORLD_HEIGHT/2f; }

            //================================= Exit Pan ===========================================
            //Once we reach roughly player position stop panning
            if(camera.position.x == cameraX && camera.position.y == cameraY) { cameraPan = false; }

            //================================= Update =============================================
            camera.position.set(cameraX, cameraY, camera.position.z);
            camera.update();
            tiledSetUp.updateCamera(camera);

            xCameraDelta = camera.position.x - WORLD_WIDTH / 2f;
            yCameraDelta = camera.position.y - WORLD_HEIGHT / 2f;

        }
    }

    /**
     * Purpose: Resize the menuStage viewport in case the screen gets resized (Desktop)
     *          Moving the camera if that's part of the game
     */
    public void updatePeopleCamera() {
        float cameraX = camera.position.x;
        float cameraY;
        //Updates Camera if the X positions has changed
        if(people.get(inControl).getX() >= camera.position.x + WORLD_WIDTH/2f){
            cameraX = tiledSetUp.getLevelWidth() - WORLD_WIDTH/2;
        }
        else if (((people.get(inControl).getX() > WORLD_WIDTH / 2f)
                && (people.get(inControl).getX() < tiledSetUp.getLevelWidth() - WORLD_WIDTH / 2f))) {
            cameraX = people.get(inControl).getX();
        }

        //Always follow people.get(inControl)
        cameraY = people.get(inControl).getY();
        //If we hit the bounds of the map stop at the world/2f distance from the world end
        if(cameraY < WORLD_HEIGHT/2f){ cameraY = WORLD_HEIGHT/2f; }
        else if(cameraY > tiledSetUp.getLevelHeight() - WORLD_HEIGHT/2f){ cameraY = tiledSetUp.getLevelHeight() - WORLD_HEIGHT/2f; }

        camera.position.set(cameraX, cameraY, camera.position.z);
        camera.update();
        tiledSetUp.updateCamera(camera);

        //Updates the change of camera to keep the UI moving with the player
        xCameraDelta = camera.position.x - WORLD_WIDTH / 2f;
        yCameraDelta = camera.position.y - WORLD_HEIGHT / 2f;
    }

    //========================================== Drawing ===========================================

    /**
     * Purpose: Central drawing function, from here everything gets drawn
    */
    private void draw(){
        //================== Clear Screen =======================
        clearScreen();

        //==================== Set Up Camera =============================
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);


        //======================== Draws ==============================
        batch.begin();
        drawBackground();
        batch.end();

        tiledSetUp.drawTiledMap();

        batch.begin();
        painting.draw(batch);
        for(Person person : people){ person.draw(batch); }
        for(Flower flower : flowers){ flower.drawAnimations(batch); }
        if(isLetter){letter.draw(batch);}
        for(Bird bird : birds){ bird.draw(batch); }
        for(Cloud cloud : clouds){ cloud.draw(batch); }
        for(TimedCloud timedCloud : timedClouds){ timedCloud.draw(batch); }
        for(Stamp stamp : stamps){ stamp.draw(batch); }
        drawStamps();
        batch.end();
    }


    /**
     * Purpose: Draws the parallax background
     */
    private void drawBackground(){
        for(int i = 0; i < tiledSetUp.getLevelWidth()/WORLD_WIDTH + 1; i++){
            batch.draw(mainScreenTextures.backgroundBack, xCameraDelta - xCameraDelta * 0.1f + WORLD_WIDTH *i, yCameraDelta);
            batch.draw(mainScreenTextures.backgroundMid, xCameraDelta - xCameraDelta * 0.2f + WORLD_WIDTH *i, yCameraDelta);
            batch.draw(mainScreenTextures.backgroundFront, xCameraDelta - xCameraDelta * 0.3f + WORLD_WIDTH *i, yCameraDelta);

        }
    }

    /**
     * Draws the collection of stamps in the top left of the screen
     */
    private void drawStamps(){
        bitmapFont.setColor(Color.BLACK);
        bitmapFont.getData().setScale(1);
        batch.draw(mainScreenTextures.textBackgroundTexture, xCameraDelta + 3 * mainScreenTextures.stampTexture.getWidth() - 50,
                yCameraDelta + WORLD_HEIGHT - mainScreenTextures.textBackgroundTexture.getHeight() + 25, mainScreenTextures.textBackgroundTexture.getWidth() * 2/3f,
                mainScreenTextures.textBackgroundTexture.getHeight()*2/3f);
        batch.draw(mainScreenTextures.stampTexture, 5 + xCameraDelta, WORLD_HEIGHT - mainScreenTextures.stampTexture.getHeight() * 2 - 5 + yCameraDelta,
                mainScreenTextures.stampTexture.getWidth() * 2, mainScreenTextures.stampTexture.getHeight() * 2);
        textAlignment.centerText(batch, bitmapFont, counterTotal - stamps.size + "/" + counterTotal,
                xCameraDelta + 40 + 3 * mainScreenTextures.stampTexture.getWidth(),
                yCameraDelta + WORLD_HEIGHT - mainScreenTextures.stampTexture.getHeight() + 5);
    }

    /**
     * Purpose: Set the screen to black so we can draw on top of it again
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a); //Sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);										 //Sends it to the buffer
    }

    /**
     * Purpose: Destroys everything once we move onto the new screen
    */
    @Override
    public void dispose(){
    }
}
