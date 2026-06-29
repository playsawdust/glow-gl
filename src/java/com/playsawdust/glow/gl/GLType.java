package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL20.GL_BOOL;
import static org.lwjgl.opengl.GL30.GL_HALF_FLOAT;
import static org.lwjgl.opengl.GL41.*;

public enum GLType {
	BOOLEAN(GL_BOOL, GLPrimitiveType.BOOLEAN, 1),
	BYTE(GL_BYTE, GLPrimitiveType.BYTE, 1),
	UNSIGNED_BYTE(GL_UNSIGNED_BYTE, GLPrimitiveType.UNSIGNED_BYTE, 1),
	SHORT(GL_SHORT, GLPrimitiveType.SHORT, 1),
	UNSIGNED_SHORT(GL_UNSIGNED_SHORT, GLPrimitiveType.UNSIGNED_SHORT, 1),
	INT(GL_INT, GLPrimitiveType.INT, 1),
	
	UNSIGNED_INT(GL_UNSIGNED_INT, GLPrimitiveType.UNSIGNED_INT, 1),
	UNSIGNED_INT_VEC2(GL_UNSIGNED_INT_VEC2, GLPrimitiveType.UNSIGNED_INT, 2),
	UNSIGNED_INT_VEC3(GL_UNSIGNED_INT_VEC3, GLPrimitiveType.UNSIGNED_INT, 3),
	UNSIGNED_INT_VEC4(GL_UNSIGNED_INT_VEC4, GLPrimitiveType.UNSIGNED_INT, 4),
	
	/** 16.16 fixed-point */
	FIXED(GL_FIXED, GLPrimitiveType.FIXED, 1),
	
	HALF(GL_HALF_FLOAT, GLPrimitiveType.HALF_FLOAT, 1),
	HALF_VEC2(-1, GLPrimitiveType.HALF_FLOAT, 2),
	HALF_VEC3(-1, GLPrimitiveType.HALF_FLOAT, 3),
	HALF_VEC4(-1, GLPrimitiveType.HALF_FLOAT, 4),
	
	FLOAT(GL_FLOAT, GLPrimitiveType.FLOAT, 1),
	FLOAT_VEC2(GL_FLOAT_VEC2, GLPrimitiveType.FLOAT, 2),
	FLOAT_VEC3(GL_FLOAT_VEC3, GLPrimitiveType.FLOAT, 3),
	FLOAT_VEC4(GL_FLOAT_VEC4, GLPrimitiveType.FLOAT, 4),
	FLOAT_MAT2(GL_FLOAT_MAT2, GLPrimitiveType.FLOAT, 4),
	FLOAT_MAT3(GL_FLOAT_MAT3, GLPrimitiveType.FLOAT, 9),
	FLOAT_MAT4(GL_FLOAT_MAT4, GLPrimitiveType.FLOAT,16),
	
	DOUBLE(GL_DOUBLE, GLPrimitiveType.DOUBLE, 1),
	DOUBLE_VEC2(GL_DOUBLE_VEC2, GLPrimitiveType.DOUBLE, 2),
	DOUBLE_VEC3(GL_DOUBLE_VEC2, GLPrimitiveType.DOUBLE, 3),
	
	
	DOUBLE_VEC4(GL_DOUBLE_VEC2, GLPrimitiveType.DOUBLE, 4),
	DOUBLE_MAT2(GL_DOUBLE_VEC2, GLPrimitiveType.DOUBLE, 4),
	DOUBLE_MAT3(GL_DOUBLE_VEC2, GLPrimitiveType.DOUBLE, 9),
	DOUBLE_MAT4(GL_DOUBLE_VEC2, GLPrimitiveType.DOUBLE,16),
	/*
	DOUBLE_MAT2x3(GL_DOUBLE_MAT2x3, Double.class, 6, 8, DOUBLE),
	DOUBLE_MAT2x4(GL_DOUBLE_MAT2x4, Double.class, 8, 8, DOUBLE),
	DOUBLE_MAT3x2(GL_DOUBLE_MAT3x2, Double.class, 6, 8, DOUBLE),
	DOUBLE_MAT3x4(GL_DOUBLE_MAT3x4, Double.class, 12, 8, DOUBLE),
	DOUBLE_MAT4x2(GL_DOUBLE_MAT4x2, Double.class, 8, 8, DOUBLE),
	DOUBLE_MAT4x3(GL_DOUBLE_MAT4x3, Double.class, 12, 8, DOUBLE),*/
	;
	
	private final int value;
	private final GLPrimitiveType primitiveType;
	private final int primitiveCount;
	
	GLType(int value, GLPrimitiveType primitive, int primitiveCount) {
		this.value = value;
		this.primitiveType = primitive;
		this.primitiveCount = primitiveCount;
	}
	
	public int value() {
		return value;
	}
	
	public GLPrimitiveType primitiveType() {
		return primitiveType;
	}
	
	public int primitiveCount() {
		return primitiveCount;
	}
	
	public int stride() {
		return primitiveCount * primitiveType.bytes();
	}
	
	public static GLType of(int value) {
		for(GLType type : values()) {
			if (type.value == value) return type;
		}
		
		return null;
	}
}
