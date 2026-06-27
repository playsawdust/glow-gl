package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

public enum BufferTarget {
	/**
	 * Array Buffers, also known as Vertex Buffer Objects, are used for storing parameters that vary per-vertex.
	 * @since OpenGL 2.0
	 */
	ARRAY(GL_ARRAY_BUFFER),
	/**
	 * Atomic counters are "opaque" objects requiring 32 bits of storage each. All counters come from a buffer such
	 * as this.
	 * @since OpenGL 4.2
	 */
	ATOMIC_COUNTER_BUFFER(0x92C0),
	/**
	 * This designates a source for glCopyBufferSubData without disturbing other OpenGL state such as ArrayBuffer
	 * binding.
	 * @since OpenGL 3.1
	 */
	COPY_READ(GL_COPY_READ_BUFFER),
	/**
	 * This designates a destination for glCopyBufferSubData without disturbing other OpenGL state such as ArrayBuffer
	 * binding.
	 * @since OpenGL 3.1
	 */
	COPY_WRITE(GL_COPY_WRITE_BUFFER),
	/**
	 * This is a compute dispatch buffer For use with glDispatchComputeIndirect.
	 * @since OpenGL 4.3
	 */
	DISPATCH_INDIRECT(0x90EE),
	/**
	 * Indirect Draw Buffers are lists of geometric primitives to draw with glDrawElementsIndirect. In combination with
	 * an Element (index) Buffer and one or more Array Buffers, this can draw many sub-ranges from a VAO with one draw
	 * call.
	 * 
	 * <p>These buffers can also be used by glMultiDrawElementsIndirect to draw multiple waves of geometry lists - the
	 * geometry from these lists are then altered by the vertex shader using the builtin gl_InstanceID value.
	 * @since OpenGL 4.0
	 */
	DRAW_INDIRECT(GL_DRAW_INDIRECT_BUFFER),
	/**
	 * Element Array Buffers are also known as Index Buffers. These pick which order vertices are rendered in, allowing
	 * for vertex reuse (and lower storage and bandwidth costs) in a mesh.
	 * 
	 * <p>Note: Index Buffers can ONLY be used with UNSIGNED_BYTE, UNSIGNED_SHORT, or UNSIGNED_INT data!
	 * @since OpenGL 2.0
	 */
	ELEMENT_ARRAY(GL_ELEMENT_ARRAY_BUFFER),
	PIXEL_PACK(GL_PIXEL_PACK_BUFFER),
	PIXEL_UNPACK(GL_PIXEL_UNPACK_BUFFER),
	/**
	 * @since OpenGL 4.4
	 */
	QUERY(0x9192),
	/**
	 * @since OpenGL 4.3
	 */
	SHADER_STORAGE(0x90D2),
	TEXTURE(GL_TEXTURE_BUFFER),
	TRANSFORM_FEEDBACK(GL_TRANSFORM_FEEDBACK_BUFFER),
	UNIFORM(GL_UNIFORM_BUFFER)
	;
	
	private int value;
	
	BufferTarget(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	public static BufferTarget of(int value) {
		for(BufferTarget target : values()) {
			if (target.value == value) return target;
		}
		
		return null;
	}
}
