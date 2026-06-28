package blue.endless.glowtest;

import static org.lwjgl.opengl.GL46.*;

import com.playsawdust.glow.gl.Window;
import com.playsawdust.glow.image.color.RGBColor;

public class App {
	public static void main(String... args) {
		Window window = new Window("Glow-GL Test");
		window.setVisible(true);
		
		while(!window.shouldClose()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			
			
			window.paint(painter -> {
				glClearColor(0, 1, 1, 0);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				for(int y=0; y<128; y++) {
					for(int x=0; x<128; x++) {
						float red = (float) Math.random() * 0.5f + 0.5f;
						float green = (float) Math.random() * 0.5f + 0.5f;
						float blue = (float) Math.random() * 0.5f + 0.5f;
						
						painter.fillRect(8*x, 8*y, 8, 8, new RGBColor(1, red, green, blue));
					}
				}
			});
			
			window.presentFrame();
		}
		
		Window.shutdown();
	}
}
