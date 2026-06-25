package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL31.*;

public class ArrayBuffer implements GLResource {

	private final int handle;
	
	public ArrayBuffer() {
		handle = glGenBuffers();
	}
	
	public void setStaticData(float[] data) {
		bind();
		glBufferData(handle, data, GL_STATIC_DRAW);
	}
	
	public void setDynamicData(float[] data) {
		bind();
		glBufferData(handle, data, GL_DYNAMIC_DRAW);
	}
	
	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, handle);
	}
	
	@Override
	public void destroy() {
		glDeleteBuffers(handle);
	}

	@Override
	public int getHandle() {
		return handle;
	}
	
}
