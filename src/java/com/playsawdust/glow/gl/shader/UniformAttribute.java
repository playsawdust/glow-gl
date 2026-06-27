package com.playsawdust.glow.gl.shader;

import com.playsawdust.glow.gl.GLType;

public interface UniformAttribute<T> {
	public int index();
	public String name();
	public GLType glType();
	public Class<T> javaType();
	
	public void set(T value);
}
