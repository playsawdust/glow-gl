package blue.endless.glowtest;

import static org.lwjgl.opengl.GL41.*;

import com.playsawdust.glow.gl.Texture;
import com.playsawdust.glow.gl.Window;
import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.SrgbImageData;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.image.io.PngImageIO;
import com.playsawdust.glow.io.DataSlice;

public class App {
	public static void main(String... args) {
		System.out.println("Loading resources...");
		ImageData testImage;
		try {
			byte[] bytes = App.class.getClassLoader().getResourceAsStream("test.png").readAllBytes();
			testImage = PngImageIO.load(DataSlice.of(bytes));
		} catch (Throwable t) {
			System.out.println(t.getMessage());
			System.out.println(t.getStackTrace());
			testImage = new SrgbImageData(2,2);
			testImage.setPixel(0, 0, 0xFF_FF00FF);
			testImage.setPixel(1, 1, 0xFF_FF00FF);
			testImage.setPixel(0, 1, 0xFF_000000);
			testImage.setPixel(1, 0, 0xFF_000000);
		}
		
		
		Window window = new Window("Glow-GL Test");
		window.setVisible(true);
		
		Texture testImageFinal = new Texture();
		testImageFinal.setImage(testImage);
		
		while(!window.shouldClose()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			
			
			window.paint(painter -> {
				painter.clear(new RGBColor(0xFF00FF));
				for(int y=0; y<32; y++) {
					for(int x=0; x<32; x++) {
						float red = (float) Math.random() * 0.5f + 0.5f;
						float green = (float) Math.random() * 0.5f + 0.5f;
						float blue = (float) Math.random() * 0.5f + 0.5f;
						
						painter.fillRect(8*x, 8*y, 8, 8, new RGBColor(1, red, green, blue));
					}
				}
				
				for(int i=0; i<128; i++) {
					int x = (int) (Math.random() * window.getWidth());
					int y = (int) (Math.random() * window.getHeight());
					//painter.drawImage(testImageFinal, x, y);
					painter.drawTintImage(testImageFinal, x, y, 8, 8, 16, 16, new RGBColor(1,1,1,1));
				}
			});
			
			window.presentFrame();
		}
		
		Window.shutdown();
	}
}
