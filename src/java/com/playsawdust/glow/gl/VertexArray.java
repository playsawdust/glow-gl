package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

public class VertexArray implements GLResource {
	private int handle;
	
	public VertexArray() {
		handle = glGenVertexArrays();
	}
	
	public void bind() {
		glBindVertexArray(handle);
	}
	
	public void bindBuffer(int index, ArrayBuffer buffer, int offset, int stride) {
		bind();
		buffer.bind();
		glEnableVertexAttribArray(index);
		glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, offset);
	}
	
	@Override
	public void destroy() {
		glDeleteVertexArrays(handle);
	}

	@Override
	public int getHandle() {
		return handle;
	}
	
}
