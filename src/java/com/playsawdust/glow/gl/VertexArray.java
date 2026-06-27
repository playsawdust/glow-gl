package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

import java.util.HashMap;
import java.util.Map;

public class VertexArray implements GLResource {
	private int handle;
	// TODO: Switch to FastUtil?
	private Map<Integer, VertexBuffer> managedBuffers = new HashMap<>();
	private VertexBuffer indexBuffer = null;
	
	public VertexArray() {
		handle = glGenVertexArrays();
	}
	
	public void bind() {
		glBindVertexArray(handle);
	}
	
	/**
	 * Binds a non-interleaved buffer to this VertexArray
	 * @param index  The vertex attribute location to bind this buffer to
	 * @param buffer The buffer to bind
	 * @param type   How to interpret the bound data
	 */
	public void bindBuffer(int index, VertexBuffer buffer, GLType type) {
		bind();
		buffer.bind();
		glVertexAttribPointer(index, type.primitiveCount(), type.primitiveType().value(), false, type.stride(), 0);
		glEnableVertexAttribArray(index);
	}
	
	public void bindData(int index, float[] data, GLType dataType) {
		VertexBuffer buf = new VertexBuffer();
		buf.setStaticData(BufferTarget.ARRAY, data);
		VertexBuffer oldBuf = managedBuffers.put(index, buf);
		if (oldBuf != null) {
			oldBuf.destroy();
		}
		bindBuffer(index, buf, dataType);
	}
	
	/**
	 * Bind index data to this VertexArray. Obviously you must have the VertexArray bound before doing this!
	 * @param indices
	 */
	public void bindIndices(int index, int[] indices) {
		VertexBuffer buf = new VertexBuffer();
		buf.setStaticData(BufferTarget.ELEMENT_ARRAY, indices);
		if (indexBuffer != null) indexBuffer.destroy();
		indexBuffer = buf;
		bindBuffer(index, buf, GLType.UNSIGNED_INT);
	}
	
	@Override
	public void destroy() {
		glDeleteVertexArrays(handle);
		for(VertexBuffer buf : managedBuffers.values()) {
			buf.destroy();
		}
		managedBuffers.clear();
	}

	@Override
	public int getHandle() {
		return handle;
	}
	
}
