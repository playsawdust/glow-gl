package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.SrgbImageData;
import com.playsawdust.glow.image.color.RGBColor;

public class Texture implements ImageData, GLResource {
	private final int handle;
	private int width = 0;
	private int height = 0;
	
	public Texture() {
		handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}
	
	public void setFilter(TextureFilter minFilter, TextureFilter magFilter) {
		if (magFilter != TextureFilter.NEAREST && magFilter != TextureFilter.LINEAR) throw new IllegalArgumentException("Texture magnification filter can only be NEAREST or LINEAR.");
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter.value());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter.value());
	}
	
	/**
	 * Sets the image data for this texture. If there is existing data, but the image sizes match, the texture storage
	 * will be reused.
	 * @param image the new image to set.
	 */
	public void setImage(ImageData image) {
		int oldWidth = width;
		int oldHeight = height;
		bind();
		width = image.getWidth();
		height = image.getHeight();
		int[] data;
		
		if (image instanceof SrgbImageData s) {
			data = s.getData();
		} else {
			data = new int[width * height];
			for(int y=0; y<height; y++) {
				for(int x=0; x<width; x++) {
					data[width * y + x] = image.getSrgbPixel(x, y);
				}
			}
		}
		
		if (width == oldWidth && height == oldHeight) {
			glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8, data);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA_INTEGER, GL_UNSIGNED_INT_8_8_8_8, data);
		}
	}
	
	/**
	 * Gets a copy of the texture from the GPU and returns it as an editable, CPU-side SrgbImageData.
	 * @return The image data for this Texture
	 */
	public SrgbImageData getImage() {
		if (width == 0 && height == 0) return new SrgbImageData(0, 0);
		
		int[] imageData = new int[width * height];
		glGetTexImage(handle, 0, GL_BGRA_INTEGER, GL_UNSIGNED_INT_8_8_8_8, imageData);
		return new SrgbImageData(width, height, imageData);
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the srgb pixel at the specified coordinates.
	 * 
	 * <p>NOTE: This is an unnecessarily expensive operation for Textures!!!
	 * 
	 * @see #getImage()
	 * @see #setImage(ImageData)
	 */
	@Override
	public int getSrgbPixel(int x, int y) {
		return getImage().getSrgbPixel(x, y);
	}

	/**
	 * Sets the pixel at the specified coordinates.
	 * 
	 * <p>NOTE: This is an unnecessarily expensive operation for Textures!!
	 * 
	 * @see #getImage()
	 * @see #setImage(ImageData)
	 */
	@Override
	public void setPixel(int x, int y, int srgb) {
		SrgbImageData im = getImage();
		im.setPixel(x, y, srgb);
		setImage(im);
	}

	/**
	 * Gets the pixel at the specified coordinates.
	 * 
	 * <p>NOTE: This is an unnecessarily expensive operation for Textures!!
	 * 
	 * @see #getImage()
	 * @see #setImage(ImageData)
	 */
	@Override
	public RGBColor getLinearPixel(int x, int y) {
		return getImage().getLinearPixel(x, y);
	}

	/**
	 * Sets the pixel at the specified coordinates.
	 * 
	 * <p>NOTE: This is an unnecessarily expensive operation for Textures!!
	 * 
	 * @see #getImage()
	 * @see #setImage(ImageData)
	 */
	@Override
	public void setPixel(int x, int y, RGBColor color) {
		SrgbImageData im = getImage();
		im.setPixel(x, y, color);
		setImage(im);
	}

	@Override
	public void destroy() {
		glDeleteTextures(handle);
	}

	@Override
	public int getHandle() {
		return this.handle;
	}
	
}
