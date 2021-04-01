package com.mygdx.lettersToMom.screens;

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
import com.mygdx.lettersToMom.main.lettersToMom;
import com.mygdx.lettersToMom.objects.animatedObjects.Flower;
import com.mygdx.lettersToMom.objects.genericObjects.Cloud;
import com.mygdx.lettersToMom.objects.genericObjects.Platform;
import com.mygdx.lettersToMom.objects.genericObjects.RespawnPoint;
import com.mygdx.lettersToMom.objects.genericObjects.Water;
import com.mygdx.lettersToMom.objects.staticObjects.Bird;
import com.mygdx.lettersToMom.objects.staticObjects.Stamp;
import com.mygdx.lettersToMom.objects.spriteObject.Letter;
import com.mygdx.lettersToMom.objects.spriteObject.Person;
import com.mygdx.lettersToMom.objects.spriteObject.UmbrellaMan;
import com.mygdx.lettersToMom.screens.textures.MainScreenTextures;
import com.mygdx.lettersToMom.tools.CutScene;
import com.mygdx.lettersToMom.tools.DebugRendering;
import com.mygdx.lettersToMom.tools.MusicControl;
import com.mygdx.lettersToMom.tools.TextAlignment;
import com.mygdx.lettersToMom.tools.TiledSetUp;

import static com.mygdx.lettersToMom.Const.WORLD_HEIGHT;
import static com.mygdx.lettersToMom.Const.WORLD_WIDTH;

class MainScreen extends ScreenAdapter {

    //=========================================== Variable =========================================

    //================================== Image processing ===========================================
    private Viewport viewport;			                     //The screen where we display things
    private Camera camera;				                     //The camera viewing the viewport
    private final SpriteBatch batch = new SpriteBatch();	 //Batch that holds all of the textures

    //===================================== Tools ==================================================
    private final lettersToMom lettersToMom;      //Game object that holds the settings
    private DebugRendering debugRendering;        //Draws debug hit-boxes
    private MusicControl musicControl;            //Plays Music
    private final TextAlignment textAlignment = new TextAlignment();
    private CutScene cutSceneStart;
    private CutScene cutSceneEnd;

    //=========================================== Text =============================================
    //Font used for the user interaction
    private BitmapFont bitmapFont = new BitmapFont();             //Font used for the user interaction
    private final BitmapFont bitmapFontDeveloper = new BitmapFont();    //Font for viewing phone stats in developer mode
    private MainScreenTextures mainScreenTextures;
    private TiledSetUp tiledSetUp;               //Takes all the data from tiled

    //============================================= Flags ==========================================
    private boolean developerMode = false;      //Developer mode shows hit boxes and phone data
    private boolean endFlag = false;            //Tells us game has been lost
    private boolean inCutScene = true;
    private boolean cameraPan = false;          //Tells us the camera is paning and player can't move
    private float xCameraDelta = 0;             //Keeps track of how far the camera has moved (to update menus)
    private float yCameraDelta = 0;             //Keeps track of how far the camera has moved (to update menus)
    private float intilaxX = 0;
    private boolean isLetter = true;
    private int inControl = 0;     //0 - Umbrella, 1 - Grandma, 2 - Police, 3 - Painter, 4 - Mailman

    //=================================== Miscellaneous Vars =======================================
    private final String[] menuButtonText = new String[]{"Restart", "Help", "Sound Off", "Main Menu", "Back", "Sound On"};
    private Array<String> levelNames = new Array<>(); //Names of all the lvls in order
    private int tiledSelection;                       //Which tiled map is loaded in


    //================================ Set Up ======================================================

    //============================== Player ============================
    private Letter letter;

    //=========================== Physical Objects =====================
    private final Array<Platform> platforms = new Array<>();
    private final Array<Person> people = new Array<>();
    private final Array<Cloud> clouds = new Array<>();
    private final Array<Stamp> stamps = new Array<>();
    private final Array<Flower> flowers = new Array<>();
    private final Array<Bird> birds = new Array<>();
    private final Array<Water> waters = new Array<>();
    private final Array<RespawnPoint> respawnPoints = new Array<>();
    private RespawnPoint currentRespawnPoint;

    /**
     * Purpose: Grabs the info from main screen that holds asset manager
     * Input: BasicTemplet
    */
    MainScreen(lettersToMom lettersToMom, int tiledSelection) {
        this.lettersToMom = lettersToMom;

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
        musicControl.showMusic(0);
        if(developerMode){debugRendering.showRender();}    //If in developer mode sets up the redners
    }


    /**
     * Purpose: Sets up all the objects imported from tiled
     */
    private void showTiled() {
        tiledSetUp = new TiledSetUp(lettersToMom.getAssetManager(), batch, levelNames.get(tiledSelection));

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
        for(int i = 0; i < respawnPointPositions.size; i++){
            respawnPoints.add(new RespawnPoint(respawnPointPositions.get(i).x, respawnPointPositions.get(i).y));
        }
        currentRespawnPoint = respawnPoints.get(0);

        //================================= People =======================================
        Array<Vector2> peoplePositions = tiledSetUp.getLayerCoordinates("People");
        Array<String> peopleNames = tiledSetUp.getLayerNames("People");
        for(int i = 0; i < peoplePositions.size; i++){
            peopleAdded(peoplePositions.get(i), peopleNames.get(i));
        }

        //================================= Clouds =======================================
        Array<Vector2> cloudPositions = tiledSetUp.getLayerCoordinates("Cloud");
        Array<Vector2> cloudDimensions = tiledSetUp.getLayerDimensions("Cloud");
        for(int i = 0; i < cloudPositions.size; i++){
            clouds.add(new Cloud(cloudPositions.get(i).x, cloudPositions.get(i).y,
                    cloudDimensions.get(i).x, cloudDimensions.get(i).y, mainScreenTextures.rainDropTexture));
        }

    }

    private void peopleAdded(Vector2 position, String name){
        switch (name){
            case "Umbrella":{
                people.add(new UmbrellaMan(position.x, position.y, mainScreenTextures.umbrellaSpriteSheet));
                break;
            }
            case "Granny":{
                people.add(new Person(position.x, position.y, mainScreenTextures.grannySpriteSheet));
                break;
            }
            case "Painter":{

                break;
            }
            case "Mailman":{

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
        musicControl = new MusicControl(lettersToMom.getAssetManager());

        if(lettersToMom.getAssetManager().isLoaded("Fonts/Font.fnt")){bitmapFont = lettersToMom.getAssetManager().get("Fonts/Font.fnt");}
        bitmapFont.getData().setScale(1f);

        cutSceneStart = new CutScene("UI/Background.png",
                "Sprites/CutScene1.png", 7);
    }

    //=================================== Execute Time Methods =====================================

    /**
    Purpose: Central function for the game, everything that updates run through this function
    */
    @Override
    public void render(float delta) {
        //In Game Updates
        if(inCutScene){
            updateCutScenes(delta);
        }
        //Live Game Updates
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
        //TODO set up enemies to render
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

    private void updateCutScenes(float delta){
        //End Cut Scene
        if(endFlag){

        }
        //Start Cut Scene
        else{
            cutSceneStart.updateTransition(delta);

            if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) && cutSceneStart.getPaused()){
                //Updates the cut Scene or takes us out of it
                inCutScene = cutSceneStart.nextSlide(delta);
            }


            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
                inCutScene = false;
            }

            clearScreen();

            //==================== Set Up Camera =============================
            batch.setProjectionMatrix(camera.projection);
            batch.setTransformMatrix(camera.view);

            //======================== Draws ==============================
            batch.begin();
            cutSceneStart.draw(batch);
            batch.end();
        }
    }

    /**
    Purpose: Updates all the moving components and game variables
    Input: @delta - timing variable
    */
    private void update(float delta){
        if(!cameraPan){handleInput();}
        if(isLetter){ updateLetterCamera(); }
        else{ updatePeopleCamera(); }
        letter.update(tiledSetUp.getLevelWidth(), tiledSetUp.getLevelHeight());
        for(Person person : people){ person.update(); }
        for(Cloud cloud : clouds){ cloud.update(); }
        for(Flower flower : flowers){flower.update(delta);}
        for(Bird bird : birds){bird.update();}
        updateCollision();
    }


    /**
     * Purpose: Central Input Handling function
     */
    private void handleInput() {
        //Allows user to turn on dev mode
        if(isLetter) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                letter.move(intilaxX);
                intilaxX -= 0.5;
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                letter.move(intilaxX);
                intilaxX += 0.5;
            } else {
                intilaxX = 0;
            }

            if (intilaxX > 7) {
                intilaxX = 7;
            }
            if (intilaxX < -7) {
                intilaxX = -7;
            }
        }
        else{
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                people.get(inControl).move(-4);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                people.get(inControl).move(4);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) { developerMode = !developerMode; }
    }

    private void updateCollision(){
        isCollidingPlatform();
        isCollidingPeople();
        isLetterColliding();
        isStampColliding();
        isRespawnPointColliding();
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isCollidingPlatform() {
        if(isLetter) {
            //Checks if there is ground below him
            boolean hasGround = false;
            for (int i = 0; i < platforms.size; i++) {
                if (letter.updateCollision(platforms.get(i).getHitBox())) {
                    hasGround = true;                //Tells us that he's standing
                }

                for(Cloud cloud : clouds){ cloud.isRainColliding(platforms.get(i).getHitBox()); }

                for(Bird bird : birds){ bird.checkCollision(platforms.get(i).getHitBox()); }

            }
            //If there is no ground below Cole he should fall
            if (!hasGround) {
                letter.setFalling(true);
            }
        }
        else{
            for (int i = 0; i < platforms.size; i++) {
                people.get(inControl).checkCollision(platforms.get(i).getHitBox());

                for(Cloud cloud : clouds){ cloud.isRainColliding(platforms.get(i).getHitBox()); }
            }
        }

    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isCollidingPeople() {
        for (int i = 0; i < people.size; i++) {
            if(people.get(i).isColliding(letter.getHitBox()) && people.get(i).getState() == 0){
                isLetter = false;
                inControl = i;
                people.get(i).updateStateOne();
            }
            for (Cloud cloud : clouds) {
                cloud.isRainColliding(people.get(i).getHitBox());
            }
        }
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isStampColliding() {
        if(isLetter) {
            for (Stamp stamp : stamps) {
                if(stamp.isColliding(letter.getHitBox())){
                    stamps.removeValue(stamp, true);
                }
            }
        }
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isLetterColliding() {
        if(isLetter) {
            for (Cloud cloud : clouds) {
                if(cloud.isLetterColliding(letter.getHitBox())){
                    letter.setX(currentRespawnPoint.getX());
                    letter.setY(currentRespawnPoint.getY());
                    cameraPan = true;
                    letter.stop();
                }
            }
            for (int i = 0; i < waters.size; i++) {
                if (waters.get(i).isColliding(letter.getHitBox())) {
                    cameraPan = true;
                    letter.setX(currentRespawnPoint.getX());
                    letter.setY(currentRespawnPoint.getY());
                    camera.position.y = currentRespawnPoint.getY() + 5;
                    letter.stop();
                }
            }
        }
    }

    /**
     * Purpose: Check if it's touching any platforms
     */
    private void isRespawnPointColliding() {
        if(!isLetter) {
            for(RespawnPoint respawnPoint : respawnPoints){
                if(respawnPoint.isColliding(people.get(inControl).getHitBox())){
                    people.get(inControl).updateStateTwo();
                    currentRespawnPoint.setX(respawnPoint.getX());
                    currentRespawnPoint.setY(respawnPoint.getY());
                    letter.setX(respawnPoint.getX());
                    letter.setY(respawnPoint.getY());
                    isLetter = true;
                    cameraPan = true;
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
                    Math.round(letter.getX()) == Math.round(camera.position.x - 1) ||
                    Math.round(letter.getX()) == Math.round(camera.position.x + 1)){
                cameraX = camera.position.x;
            }
            //If we're to the left of the camera move left
            else if(letter.getX() < camera.position.x){ cameraX = camera.position.x - 3; }
            //If we're to the right of the camera move right
            else { cameraX = camera.position.x + 3;}

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


    /**
    Purpose: Puts the game in end game state
    */
    private void endGame(){ endFlag = true; }

    /**
     Purpose: Restarts the game to it's basic settings
     */
    private void restart(){
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
        for(Person person : people){ person.draw(batch); }
        for(Flower flower : flowers){ flower.drawAnimations(batch); }
        if(isLetter){letter.draw(batch);}
        for(Bird bird : birds){ bird.draw(batch); }
        for(Cloud cloud : clouds){ cloud.draw(batch); }
        for(Stamp stamp : stamps){ stamp.draw(batch); }
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
