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
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			window.paint(painter -> {
				glClearColor(0, 1, 1, 0);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				painter.fillRect(0, 0, 25, 25, new RGBColor(0xFF00FF));
			});
			
			
			
			// Draw - the following is now repeatable
			//shaderProgram.bind();
			//vao.bind();
			//glDrawArrays(GL_TRIANGLES, 0, 3);
			
			window.presentFrame();
		}
		
		Window.shutdown();
	}
}
