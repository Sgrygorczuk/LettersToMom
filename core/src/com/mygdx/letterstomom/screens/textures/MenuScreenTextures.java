package com.mygdx.letterstomom.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuScreenTextures {

    //============================================= Textures =======================================
    public Texture backgroundTexture;
    public Texture buttonTexture;

    public TextureRegion[][] flowerSpriteSheet;
    public TextureRegion[][] logoSpriteSheet;

    public MenuScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        backgroundTexture = new Texture(Gdx.files.internal("UI/Background.png"));
        buttonTexture = new Texture(Gdx.files.internal("UI/Button.png"));


        Texture flowerTexturePath = new Texture(Gdx.files.internal("UI/Flower.png"));
        flowerSpriteSheet = new TextureRegion(flowerTexturePath).split(
                flowerTexturePath.getWidth()/4, flowerTexturePath.getHeight());

        Texture logoTexturePath = new Texture(Gdx.files.internal("UI/Logo.png"));
        logoSpriteSheet = new TextureRegion(logoTexturePath).split(
                logoTexturePath.getWidth()/6, logoTexturePath.getHeight());
    }

}