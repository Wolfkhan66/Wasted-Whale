package com.wolfkhan66.wastedwhale.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wolfkhan66.wastedwhale.WastedWhaleMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new WastedWhaleMain(), config);
		config.width = 800;
		config.height = 480;
	}
}
