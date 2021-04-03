package com.mygdx.lettersToMom.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainScreenTextures {

    //============================================= Textures =======================================
    public Texture backgroundTexture;
    public Texture menuBackgroundTexture;   //Pop up menu to show menu buttons and Help screen
    public Texture letterTexture;
    public Texture rainDropTexture;
    public Texture stampTexture;
    public Texture birdTexture;
    public Texture buttonTexture;
    public Texture closeUpButtonTexture;
    public Texture textBackgroundTexture;
    public Texture paintingTexture;

    public Texture closeUpOneTexture;
    public Texture closeUpTwoTexture;
    public Texture closeUpThreeTexture;
    public Texture closeUpFourTexture;

    public Texture speechBubbleOneTexture;
    public Texture speechBubbleTwoTexture;
    public Texture speechBubbleThreeTexture;
    public Texture speechBubbleFourTexture;


    public Texture backgroundBack;
    public Texture backgroundMid;
    public Texture backgroundFront;

    public TextureRegion[][] umbrellaSpriteSheet;
    public TextureRegion[][] grannySpriteSheet;
    public TextureRegion[][] painterSpriteSheet;
    public TextureRegion[][] mailmanSpriteSheet;
    public TextureRegion[][] flowerSpriteSheet;

    public MainScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        backgroundTexture = new Texture(Gdx.files.internal("UI/Background.png"));
        menuBackgroundTexture = new Texture(Gdx.files.internal("UI/Background.png"));
        letterTexture = new Texture(Gdx.files.internal("Sprites/Letter.png"));
        rainDropTexture = new Texture(Gdx.files.internal("Sprites/RainDrop.png"));
        stampTexture = new Texture(Gdx.files.internal("Sprites/Stamp.png"));
        birdTexture = new Texture(Gdx.files.internal("Sprites/Bird.png"));
        closeUpButtonTexture = new Texture(Gdx.files.internal("UI/CloseUpButton.png"));
        textBackgroundTexture = new Texture(Gdx.files.internal("UI/TextBackground.png"));
        buttonTexture = new Texture(Gdx.files.internal("UI/CutSceneButton.png"));
        paintingTexture = new Texture(Gdx.files.internal("Sprites/Painting.png"));

        closeUpOneTexture = new Texture(Gdx.files.internal("Sprites/LetterCloseUpsOne.png"));
        closeUpTwoTexture = new Texture(Gdx.files.internal("Sprites/LetterCloseUpsTwo.png"));
        closeUpThreeTexture = new Texture(Gdx.files.internal("Sprites/LetterCloseUpsThree.png"));
        closeUpFourTexture = new Texture(Gdx.files.internal("Sprites/LetterCloseUpsFour.png"));

        speechBubbleOneTexture = new Texture(Gdx.files.internal("Sprites/SpeechBubbleOne.png"));
        speechBubbleTwoTexture = new Texture(Gdx.files.internal("Sprites/SpeechBubbleTwo.png"));
        speechBubbleThreeTexture = new Texture(Gdx.files.internal("Sprites/SpeechBubbleThree.png"));
        speechBubbleFourTexture = new Texture(Gdx.files.internal("Sprites/SpeechBubbleFour.png"));

        backgroundBack = new Texture(Gdx.files.internal("Sprites/BackgroundBack.png"));
        backgroundMid = new Texture(Gdx.files.internal("Sprites/BackgroundMid.png"));
        backgroundFront = new Texture(Gdx.files.internal("Sprites/BackgroundFront.png"));

        Texture umbrellaTexturePath = new Texture(Gdx.files.internal("Sprites/UmbrellaMan.png"));
        umbrellaSpriteSheet = new TextureRegion(umbrellaTexturePath).split(
                umbrellaTexturePath.getWidth()/3, umbrellaTexturePath.getHeight());

        Texture grannyTexturePath = new Texture(Gdx.files.internal("Sprites/Granny.png"));
        grannySpriteSheet = new TextureRegion(grannyTexturePath).split(
                grannyTexturePath.getWidth()/3, grannyTexturePath.getHeight());

        Texture painterTexturePath = new Texture(Gdx.files.internal("Sprites/Painter.png"));
        painterSpriteSheet = new TextureRegion(painterTexturePath ).split(
                painterTexturePath .getWidth()/3, painterTexturePath .getHeight());

        Texture mailmanTexturePath = new Texture(Gdx.files.internal("Sprites/MailMan.png"));
        mailmanSpriteSheet = new TextureRegion(mailmanTexturePath ).split(
                mailmanTexturePath .getWidth()/3, mailmanTexturePath .getHeight());

        Texture flowerTexturePath = new Texture(Gdx.files.internal("UI/Flower.png"));
        flowerSpriteSheet = new TextureRegion(flowerTexturePath).split(
                flowerTexturePath.getWidth()/4, flowerTexturePath.getHeight());

    }

}
