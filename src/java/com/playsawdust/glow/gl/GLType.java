package com.playsawdust.glow.gl;

import static org.lwjgl.opengl.GL41.*;

public enum GLType {
	FLOAT(GL_FLOAT, Float.class, 1, 4),
	FLOAT_VEC2(GL_FLOAT_VEC2, Float.class, 2, 4, FLOAT),
	FLOAT_VEC3(GL_FLOAT_VEC3, Float.class, 3, 4, FLOAT),
	FLOAT_VEC4(GL_FLOAT_VEC4, Float.class, 4, 4, FLOAT),
	FLOAT_MAT2(GL_FLOAT_MAT2, Float.class, 4, 4, FLOAT),
	FLOAT_MAT3(GL_FLOAT_MAT3, Float.class, 9, 4, FLOAT),
	FLOAT_MAT4(GL_FLOAT_MAT4, Float.class, 16, 4, FLOAT),
	
	UNSIGNED_INT(GL_UNSIGNED_INT, Integer.class, 1, 4),
	UNSIGNED_INT_VEC2(GL_UNSIGNED_INT_VEC2, Integer.class, 2, 4, UNSIGNED_INT),
	UNSIGNED_INT_VEC3(GL_UNSIGNED_INT_VEC3, Integer.class, 3, 4, UNSIGNED_INT),
	UNSIGNED_INT_VEC4(GL_UNSIGNED_INT_VEC4, Integer.class, 4, 4, UNSIGNED_INT),
	
	DOUBLE(GL_DOUBLE, Double.class, 1, 8),
	DOUBLE_VEC2(GL_DOUBLE_VEC2, Double.class, 2, 8, DOUBLE),
	DOUBLE_VEC3(GL_DOUBLE_VEC3, Double.class, 3, 8, DOUBLE),
	DOUBLE_VEC4(GL_DOUBLE_VEC4, Double.class, 4, 8, DOUBLE),
	DOUBLE_MAT2(GL_DOUBLE_MAT2, Double.class, 4, 8, DOUBLE),
	DOUBLE_MAT3(GL_DOUBLE_MAT3, Double.class, 9, 8, DOUBLE),
	DOUBLE_MAT4(GL_DOUBLE_MAT4, Double.class, 16, 8, DOUBLE),
	DOUBLE_MAT2x3(GL_DOUBLE_MAT2x3, Double.class, 6, 8, DOUBLE),
	DOUBLE_MAT2x4(GL_DOUBLE_MAT2x4, Double.class, 8, 8, DOUBLE),
	DOUBLE_MAT3x2(GL_DOUBLE_MAT3x2, Double.class, 6, 8, DOUBLE),
	DOUBLE_MAT3x4(GL_DOUBLE_MAT3x4, Double.class, 12, 8, DOUBLE),
	DOUBLE_MAT4x2(GL_DOUBLE_MAT4x2, Double.class, 8, 8, DOUBLE),
	DOUBLE_MAT4x3(GL_DOUBLE_MAT4x3, Double.class, 12, 8, DOUBLE)
	;
	
	private final int value;
	private final Class<?> javaType;
	private final GLType primitiveType;
	private final int primitiveCount;
	private final int primitiveSize;
	
	GLType(int value, Class<?> javaType, int primitiveCount, int primitiveSize) {
		this.value = value;
		this.javaType = javaType;
		this.primitiveType = this;
		this.primitiveCount = primitiveCount;
		this.primitiveSize = primitiveSize;
	}
	
	GLType(int value, Class<?> javaType, int primitiveCount, int primitiveSize, GLType primitiveType) {
		this.value = value;
		this.javaType = javaType;
		this.primitiveType = primitiveType;
		this.primitiveCount = primitiveCount;
		this.primitiveSize = primitiveSize;
	}
	
	public int value() {
		return value;
	}
	
	public Class<?> javaType() {
		return javaType;
	}
	
	public GLType primitiveType() {
		return primitiveType;
	}
	
	public int primitiveCount() {
		return primitiveCount;
	}
	
	/** Returns the size of each primitive, in bytes */
	public int primitiveSize() {
		return primitiveSize;
	}
	
	public int stride() {
		return primitiveCount * primitiveSize;
	}
	
	public static GLType of(int value) {
		for(GLType type : values()) {
			if (type.value == value) return type;
		}
		
		return null;
	}
}
