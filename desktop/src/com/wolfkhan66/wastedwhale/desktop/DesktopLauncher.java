package com.wolfkhan66.wastedwhale.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.wolfkhan66.wastedwhale.WastedWhaleMain;


public class DesktopLauncher {

	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = true;

	public static void main (String[] arg) {

		if (rebuildAtlas){
			TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "C:/Users/caile_000/Documents/GitHub/Wasted-Whale/desktop/assets-raw/images", "images", "wasted_whale.pack");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new WastedWhaleMain(), config);
		config.width = 800;
		config.height = 480;
	}
}
