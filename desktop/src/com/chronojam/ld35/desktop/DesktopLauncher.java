package com.chronojam.ld35.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chronojam.ld35.LD35;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "The Creation";
		config.width = 1280;
		config.height = 720;
		config.resizable = true;
		new LwjglApplication(new LD35(), config);
	}
}
