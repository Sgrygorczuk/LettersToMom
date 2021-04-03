package com.mygdx.lettersToMom.screens.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CreditsScreenTextures {
    //============================================= Textures =======================================
    public Texture backgroundTexture;
    public Texture stampTexture;


    public CreditsScreenTextures(){ showTextures(); }

    /**
     * Purpose: Sets up all of the textures
     */
    private void showTextures(){
        stampTexture = new Texture(Gdx.files.internal("Sprites/Stamp.png"));
        backgroundTexture = new Texture(Gdx.files.internal("Sprites/Reward.jpg"));
    }
}
