package com.playsawdust.glow.gl;

import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.render.Painter;

import static org.lwjgl.opengl.GL31.*;

public class WindowPainter implements Painter {
	public static final String VERT_SHADER_SRC = """
			#version 330
			
			layout(location=0) in vec3 inPosition;
			
			void main() {
				gl_Position = vec4(inPosition, 1.0);
				
			}
			""";
	
	public static final String FRAG_SHADER_SRC = """
			#version 330
			
			out vec4 fragColor;
			
			void main() {
				fragColor = vec4(1.0, 0.0, 0.0, 1.0);
			}
			""";
	
	private final Window target;
	
	public WindowPainter(Window target) {
		this.target = target;
	}
	
	@Override
	public int getWidth() {
		return target.getWidth();
	}

	@Override
	public int getHeight() {
		return target.getHeight();
	}

	@Override
	public void drawImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height,
			float opacity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawTintImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height,
			RGBColor tintColor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPixel(int x, int y, RGBColor color) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void fillRect(int x, int y, int width, int height, RGBColor color) {
		glColor3d(color.r(), color.g(), color.b());
		
		// TODO Auto-generated method stub
		//Painter.super.fillRect(x, y, width, height, color);
	}
	
}
