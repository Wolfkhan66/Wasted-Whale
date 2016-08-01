package com.wolfkhan66.wastedwhale.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.wolfkhan66.wastedwhale.game.Assets;

/**
 * Created by Cai Lehwald on 01/08/2016.
 */
public abstract class AbstractGameScreen implements Screen {
    protected Game game;

    public AbstractGameScreen (Game game){
        this.game = game;
    }

    public abstract void render (float deltaTime);
    public abstract void resize (int width, int height);
    public abstract void show ();
    public abstract void hide ();
    public abstract void pause ();

    public void resume () {
        Assets.instance.init(new AssetManager());
    }

    public void dispose () {
        Assets.instance.dispose();
    }
}
