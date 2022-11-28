package com.playsawdust.glow.gl;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.playsawdust.glow.gl.offheap.GLResource;
import com.playsawdust.glow.gl.offheap.MallocDataSlice;
import com.playsawdust.glow.io.DataSlice;

public class VertexArray implements GLResource {
	private final int buf;
	private final int handle;
	
	public VertexArray() {
		buf = GL30.glGenBuffers();
		handle = GL30.glGenVertexArrays();
	}
	
	public void set(float[] data, Usage usage) {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, buf);
		GL30.glBufferData(buf, data, usage.value);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	
	public void set(ByteBuffer data, Usage usage) {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, buf);
		GL30.glBufferData(buf, data, usage.value);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
	}
	
	public void set(DataSlice slice, Usage usage) {
		if (slice instanceof MallocDataSlice mds) {
			set(mds.getBackingBuffer(), usage);
		} else try {
			ByteBuffer buf = MemoryUtil.memAlloc((int) slice.length());
			//if malloc doesn't work use this:
			//ByteBuffer buf = ByteBuffer.allocateDirect((int) slice.length());
			buf.put(slice.toArray());
			buf.flip();
			set(buf, usage);
			MemoryUtil.memFree(buf);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public void bind() {
		GL30.glBindVertexArray(handle);
	}

	@Override
	public void destroy() {
		GL30.glDeleteBuffers(buf);
		GL30.glDeleteVertexArrays(handle);
	}
	
	public static enum Usage {
		/**
		 * Buffer contents will be copied from system memory to the GPU, and used many times for drawing commands.
		 */
		STATIC_DRAW(GL30.GL_STATIC_DRAW),
		/**
		 * Buffer contents will be copied from somewhere else on the GPU, and used many times for drawing commands.
		 */
		STATIC_COPY(GL30.GL_STATIC_COPY),
		/**
		 * Buffer contents will be copied from the GPU and returned to the system for repeated use in main memory.
		 */
		STATIC_READ(GL30.GL_STATIC_READ),
		/**
		 * Buffer contents will be repeatedly modified and used for drawing commands.
		 */
		DYNAMIC_DRAW(GL30.GL_DYNAMIC_DRAW),
		/**
		 * Buffer ccocntents will be repeatedly copied from somewhere else in the GPU and used for drawing commands.
		 */
		DYNAMIC_COPY(GL30.GL_DYNAMIC_COPY),
		/**
		 * Buffer contents will repeatedly be modified with data from the GPU and read out for use in the application.
		 */
		DYNAMIC_READ(GL30.GL_DYNAMIC_READ),
		/**
		 * When new data is uploaded to the buffer, it will be used for drawing at most a couple times before new data
		 * is sent.
		 */
		STREAM_DRAW(GL30.GL_STREAM_DRAW),
		/**
		 * When new data is copied into the buffer from somewhere else on the GPU, it will be used for drawing at most
		 * a couple times before new data is copied in.
		 */
		STREAM_COPY(GL30.GL_STREAM_COPY),
		/**
		 * When new data is poured into the buffer by the GPU, it will be read out by the application at most a couple
		 * times before new data is poured in.
		 */
		STREAM_READ(GL30.GL_STREAM_READ)
		;
		
		private final int value;
		
		Usage(int value) {
			this.value = value;
		}
		
		public int value() {
			return value;
		}
	}
}
