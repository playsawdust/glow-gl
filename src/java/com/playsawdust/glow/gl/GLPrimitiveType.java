package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

public enum GLPrimitiveType {
	BOOLEAN(GL_BOOL, 1),
	BYTE(GL_BYTE, 1),
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE, 1),
	SHORT(GL_SHORT, 2),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT, 2),
	INT(GL_INT, 4),
	UNSIGNED_INT(GL_UNSIGNED_INT, 4),
	/** 16.16 fixed-point */
	FIXED(GL_FIXED, 4),
	HALF_FLOAT(GL_HALF_FLOAT, 2),
	FLOAT(GL_FLOAT, 4),
	DOUBLE(GL_DOUBLE, 8);
	
	private final int value;
	private final int bytes;
	
	GLPrimitiveType(int value, int bytes) {
		this.value = value;
		this.bytes = bytes;
	}
	
	public int value() { return value; }
	public int bytes() { return bytes; }
}
