package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL31.*;

public class VertexBuffer implements GLResource {
	private BufferTarget target;
	private final int handle;
	
	public VertexBuffer() {
		target = null;
		handle = glGenBuffers();
	}
	
	public void setStaticData(BufferTarget target, float[] data) {
		this.target = target;
		bind();
		glBufferData(target.value(), data, GL_STATIC_DRAW);
	}
	
	public void setDynamicData(BufferTarget target, float[] data) {
		this.target = target;
		bind();
		glBufferData(target.value(), data, GL_DYNAMIC_DRAW);
	}
	
	public void setStaticData(BufferTarget target, int[] data) {
		this.target = target;
		bind();
		glBufferData(target.value(), data, GL_STATIC_DRAW);
	}
	
	public void bind() {
		glBindBuffer(target.value(), handle);
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
