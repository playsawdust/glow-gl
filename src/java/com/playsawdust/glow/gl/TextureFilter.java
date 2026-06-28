package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

public enum TextureFilter {
	/** Returns the value of the texture element that is nearest (in Manhattan distance) to the specified texture coordinates. */
	NEAREST(GL_NEAREST),
	/**
	 * Returns the weighted average of the four texture elements that are closest to the specified texture coordinates.
	 * These can include items wrapped or repeated from other parts of a texture, depending on the values of
	 * GL_TEXTURE_WRAP_S and GL_TEXTURE_WRAP_T, and on the exact mapping. 
	 */
	LINEAR(GL_LINEAR),
	/**
	 * Chooses the mipmap that most closely matches the size of the pixel being textured and uses the GL_NEAREST
	 * criterion (the texture element closest to the specified texture coordinates) to produce a texture value. 
	 */
	NEAREST_ON_SINGLE_MIPMAP(GL_NEAREST_MIPMAP_NEAREST),
	/**
	 * Chooses the mipmap that most closely matches the size of the pixel being textured and uses the GL_LINEAR
	 * criterion (a weighted average of the four texture elements that are closest to the specified texture coordinates)
	 * to produce a texture value. 
	 */
	LINEAR_ON_SINGLE_MIPMAP(GL_LINEAR_MIPMAP_NEAREST),
	/**
	 * Chooses the two mipmaps that most closely match the size of the pixel being textured and uses the GL_NEAREST
	 * criterion (the texture element closest to the specified texture coordinates ) to produce a texture value from
	 * each mipmap. The final texture value is a weighted average of those two values. 
	 */
	NEAREST_ON_TWO_MIPMAPS(GL_NEAREST_MIPMAP_LINEAR),
	/**
	 * Chooses the two mipmaps that most closely match the size of the pixel being textured and uses the GL_LINEAR
	 * criterion (a weighted average of the texture elements that are closest to the specified texture coordinates) to
	 * produce a texture value from each mipmap. The final texture value is a weighted average of those two values. 
	 */
	LINEAR_ON_TWO_MIPMAPS(GL_LINEAR_MIPMAP_LINEAR)
	;
	
	
	private final int value;
	
	TextureFilter(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
	
	public TextureFilter of(int value) {
		for(TextureFilter filter : values()) {
			if (value == filter.value) return filter;
		}
		
		return null;
	}
}
