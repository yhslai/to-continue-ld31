package cc.raintomorrow.desktop;

import cc.raintomorrow.EcgApp;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 960;
        config.height = 720;
		new LwjglApplication(new EcgApp(), config);
	}
}
