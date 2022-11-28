package com.playsawdust.glow.gl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.color.BlendMode;
import com.playsawdust.glow.image.color.Colors;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.render.Painter;

public class FramebufferPainter implements Painter {
	
	
	private int width;
	private int height;
	
	public FramebufferPainter() {
		
	}
	
	public void startPainting(int width, int height) {
		//TODO: Attach shaders
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void drawImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height, float opacity) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				RGBColor src = image.getLinearPixel(srcX + x, srcY + y);
				drawPixel(destX + x, destY + y, src);
			}
		}
	}

	@Override
	public void drawTintImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height, RGBColor tintColor) {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				RGBColor src = image.getLinearPixel(srcX + x, srcY + y);
				RGBColor tintedSrc = BlendMode.NORMAL.blend(tintColor, src);
				drawPixel(destX + x, destY + y, tintedSrc);
			}
		}
	}

	@Override
	public void drawPixel(int x, int y, RGBColor color) {
		//TODO: Implement - we're going to try clearing the screen or drawing a fullscreen quad with a scissor to the
		//indicated pixel, and see how it performs.
		
		
		float[] quad = {
			x, y, 0.0f,
			x+1, y, 0.0f,
			x+1,  y+1, 0.0f,
			x, y, 0.0f,
			x+1,  y+1, 0.0f,
			x,  y+1, 0.0f
		};
		
		int buf = GL20.glGenBuffers();
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, buf);
		GL20.glBufferData(GL20.GL_ARRAY_BUFFER, buf, GL20.GL_STATIC_DRAW);
		GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
		
		int arr = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(arr);
		/*
		int vertexAttrib = GL30.glGetAttribLocation(program,"vertex")));
		*/
		
		GL20.glEnableVertexAttribArray(vertexAttrib);
		  glBindBuffer(GL_ARRAY_BUFFER,vBuffer);
		  glVertexAttribPointer(vertexLocation, 2, GL_FLOAT, GL_FALSE, 0, (const GLvoid*)0 );
		  glBindBuffer(GL_ARRAY_BUFFER,0);                                                           
		  glBindVertexArray(0);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
		
		GL30.glBindVertexArray(0);
		
		GL30.glDeleteVertexArrays(arr);
		GL20.glDeleteBuffers(buf);
		
	}
	
	@Override
	public void clear(RGBColor color) {
		GL11.glClearColor(
				(float) Colors.linearElementToGamma(color.r()),
				(float) Colors.linearElementToGamma(color.g()),
				(float) Colors.linearElementToGamma(color.b()),
				1f);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

}
