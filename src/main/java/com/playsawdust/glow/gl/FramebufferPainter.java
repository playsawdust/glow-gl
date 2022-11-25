package com.playsawdust.glow.gl;

import org.lwjgl.opengl.GL11;

import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.color.BlendMode;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.render.Painter;

public class FramebufferPainter implements Painter {
	
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
		
		/*
		float[] screenQuad = {
			-1.0f, -1.0f, 0.0f,
			1.0f, -1.0f, 0.0f,
			1.0f,  1.0f, 0.0f,
			-1.0f, -1.0f, 0.0f,
			1.0f,  1.0f, 0.0f,
			-1.0f,  1.0f, 0.0f
		};
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);*/
	}

}
