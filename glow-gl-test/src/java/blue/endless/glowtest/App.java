package blue.endless.glowtest;

import static org.lwjgl.opengl.GL41.*;

import java.util.Optional;

import com.playsawdust.glow.gl.Texture;
import com.playsawdust.glow.gl.Window;
import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.SrgbImageData;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.io.resource.EmbeddedResourcePool;
import com.playsawdust.glow.io.resource.Identifier;
import com.playsawdust.glow.io.resource.Resource;
import com.playsawdust.glow.io.resource.ResourceManager;

public class App {
	public static final SrgbImageData MISSINGNO = new SrgbImageData(2,2);
	static {
		MISSINGNO.setPixel(0, 0, 0xFF_FF00FF);
		MISSINGNO.setPixel(1, 1, 0xFF_FF00FF);
		MISSINGNO.setPixel(0, 1, 0xFF_000000);
		MISSINGNO.setPixel(1, 0, 0xFF_000000);
	}
	
	public static void main(String... args) {
		Window window = new Window("Glow-GL Test");
		
		System.out.println("Loading resources...");
		
		ResourceManager manager = new ResourceManager();
		manager.addPool(new EmbeddedResourcePool("builtin", 0, App.class.getClassLoader(), ""));
		
		// findFirst with DESCENDING makes this a resource that can be overridden by higher-priority pools such as mods
		Optional<ImageData> imageResource = manager
				.findFirst(Identifier.of("test:test.png"), ResourceManager.PriorityOrder.DESCENDING)
				.flatMap(Resource::asImage);
		
		// Past here, make sure you have a Window constructed!!!
		Texture testTexture = new Texture();
		
		imageResource.ifPresentOrElse((image) -> {
			testTexture.setImage(image);
		}, () -> {
			testTexture.setImage(MISSINGNO);
		});
		
		window.setVisible(true);
		
		while(!window.shouldClose()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			
			
			window.paint(painter -> {
				painter.clear(new RGBColor(0x7777AA));
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
					painter.drawTintImage(testTexture, x, y, 0, 0, 64, 64, new RGBColor(0.25f,1,1,1));
				}
			});
			
			window.presentFrame();
		}
		
		Window.shutdown();
	}
}
