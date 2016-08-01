package com.wolfkhan66.wastedwhale;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.wolfkhan66.wastedwhale.game.Assets;
import com.wolfkhan66.wastedwhale.game.screens.MenuScreen;

/**
 * Created by Cai Lehwald on 24/07/2016.
 */

public class WastedWhaleMain extends Game {
	@Override
	public void create(){
		// Set LibGDX log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load Assets
		Assets.instance.init(new AssetManager());
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}