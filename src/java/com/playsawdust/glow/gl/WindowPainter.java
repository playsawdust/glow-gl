package com.playsawdust.glow.gl;

import com.playsawdust.glow.gl.shader.ShaderProgram;
import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.render.Painter;

import static org.lwjgl.opengl.GL41.*;

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
	private ShaderProgram shader;
	
	public WindowPainter(Window target) {
		this.target = target;
		this.shader = new ShaderProgram(VERT_SHADER_SRC, FRAG_SHADER_SRC);
	}
	
	public void startDrawing() {
		shader.bind();
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
	public void drawImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height, float opacity) {
		// TODO: FIXME THIS WILL BE VERY SLOW
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				RGBColor col = image.getLinearPixel(srcX + x, srcY + y);
				fillRect(destX + x, destY + y, 1, 1, col);
			}
		}
	}

	@Override
	public void drawTintImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height, RGBColor tintColor) {
		// TODO: FIXME THIS WILL NOT TINT THE IMAGE
		drawImage(image, destX, destY, srcX, srcY, width, height, 1.0f);
	}

	@Override
	public void drawPixel(int x, int y, RGBColor color) {
		fillRect(x, y, 1, 1, color);
	}
	
	@Override
	public void fillRect(int x, int y, int width, int height, RGBColor color) {
		ArrayBuffer buf = new ArrayBuffer();
		buf.setStaticData(new float[] {
				0, 0.5f, 0,
				0.5f, -0.5f, 0,
				-0.5f, -0.5f, 0
		});
		buf.bind(); // superfluous here for the moment because setStaticData binds it; useful later
		VertexArray vertexArray = new VertexArray();
		vertexArray.bind(); // superfluous here for the moment because we immediately bind a buffer which binds both
		vertexArray.bindBuffer(0, buf, 0, 3 * Float.BYTES);
		
		
		shader.bind();
		vertexArray.bind();
		glDrawArrays(GL_TRIANGLES, 0, 3);
		
		vertexArray.destroy();
		buf.destroy();
	}
	
}
