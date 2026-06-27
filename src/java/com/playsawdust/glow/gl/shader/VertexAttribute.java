package com.playsawdust.glow.gl.shader;

import com.playsawdust.glow.gl.GLType;

public interface VertexAttribute<T> {
	public int index();
	public String name();
	public GLType glType();
	public Class<T> javaType();
}
