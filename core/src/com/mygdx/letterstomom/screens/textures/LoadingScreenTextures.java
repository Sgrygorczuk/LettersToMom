package com.mygdx.letterstomom.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadingScreenTextures {
    //============================================= Textures =======================================
    public Texture backgroundTexture;
    public Texture sunTexture;
    public Texture logoTexture;
    public Texture loadingTexture;

    public LoadingScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        backgroundTexture = new Texture(Gdx.files.internal("UI/Background.png"));
        sunTexture = new Texture(Gdx.files.internal("UI/Sun.png"));
        loadingTexture = new Texture(Gdx.files.internal("UI/Loading.png"));

        logoTexture = new Texture(Gdx.files.internal("Sprites/Logo.png"));
    }

}
