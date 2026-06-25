package blue.endless.glowtest;

import org.lwjgl.opengl.GL41;

import com.playsawdust.glow.gl.Window;
import com.playsawdust.glow.image.color.RGBColor;

public class App {
	public static void main(String... args) {
		Window window = new Window("Glow-GL Test");
		window.setVisible(true);
		while(!window.shouldClose()) {
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			window.paint(painter -> {
				GL41.glClearColor(0, 1, 1, 0);
				GL41.glClear(GL41.GL_COLOR_BUFFER_BIT | GL41.GL_DEPTH_BUFFER_BIT);
				painter.fillRect(0, 0, 200, 200, new RGBColor(0xFF00FF));
			});
			
			
			window.presentFrame();
		}
		
		Window.shutdown();
	}
}
