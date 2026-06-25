package blue.endless.glowtest;

import com.playsawdust.glow.gl.Window;
import com.playsawdust.glow.render.Painter;

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
			
			Painter p = window.getPainter();
			System.out.println(p.getWidth()+"x"+p.getHeight());
			
			window.presentFrame();
		}
		
		Window.shutdown();
	}
}
