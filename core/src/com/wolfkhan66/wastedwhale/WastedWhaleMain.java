package com.wolfkhan66.wastedwhale;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Cai Lehwald on 24/07/2016.
 */

public class WastedWhaleMain implements ApplicationListener {
	private static final String TAG = WastedWhaleMain.class.getName();

	private com.wolfkhan66.wastedwhale.game.WorldController worldController;
	private com.wolfkhan66.wastedwhale.game.WorldRenderer worldRenderer;
	private boolean paused;

	@Override public void create() {
		// Set LibGDX log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Load assets
		com.wolfkhan66.wastedwhale.game.Assets.instance.init(new AssetManager());
		// Initialize controller and renderer
		worldController = new com.wolfkhan66.wastedwhale.game.WorldController();
		worldRenderer = new com.wolfkhan66.wastedwhale.game.WorldRenderer(worldController);

		// Game world is active on start
		paused = false;
	}

	@Override public void render() {
		// Do not update the game world when paused.
		if(!paused) {
			// Update game world by the time that has passed
			// since the last rendered frame
			worldController.update(Gdx.graphics.getDeltaTime());
		}

		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);

		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render game world to screen
		worldRenderer.render();
	}

	@Override public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override public void pause() {
		paused = true;
	}

	@Override public void resume() {
		paused = false;
	}

	@Override public void dispose() {
		worldRenderer.dispose();
		com.wolfkhan66.wastedwhale.game.Assets.instance.dispose();
	}
}
