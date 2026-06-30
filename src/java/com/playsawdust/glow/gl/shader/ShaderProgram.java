package com.playsawdust.glow.gl.shader;

import com.playsawdust.glow.gl.GLResource;
import static org.lwjgl.opengl.GL41.*;

public class ShaderProgram implements GLResource {
	private final int handle;
	private final int vertHandle;
	private final int fragHandle;
	
	
	
	public ShaderProgram(String vert, String frag) {
		handle = glCreateProgram();
		vertHandle = glCreateShader(GL_VERTEX_SHADER);
		fragHandle = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertHandle, vert);
		glCompileShader(vertHandle);
		if (glGetShaderi(vertHandle, GL_COMPILE_STATUS) != GL_TRUE) {
			String err = glGetShaderInfoLog(vertHandle);
			throw new RuntimeException("Vertex shader compile error: " + err);
		}
		
		String log = glGetShaderInfoLog(vertHandle);
		if (log != null && !log.isBlank()) System.out.println(log);
		
		glShaderSource(fragHandle, frag);
		glCompileShader(fragHandle);
		if (glGetShaderi(fragHandle, GL_COMPILE_STATUS) != GL_TRUE) {
			String err = glGetShaderInfoLog(fragHandle);
			throw new RuntimeException("Fragment shader compile error: " + err);
		}
		
		log = glGetShaderInfoLog(fragHandle);
		if (log != null && !log.isBlank()) System.out.println(log);
		
		glAttachShader(handle, vertHandle);
		glAttachShader(handle, fragHandle);
		glLinkProgram(handle);
		if (glGetProgrami(handle, GL_LINK_STATUS) != GL_TRUE) {
			String err = glGetProgramInfoLog(handle);
			throw new RuntimeException("Shader program link error: "+err);
		}
		
		log = glGetProgramInfoLog(handle);
		if (log != null && !log.isBlank()) System.out.println(log);
	}
	
	public void bind() {
		glUseProgram(handle);
	}
	
	
	
	@Override
	public void destroy() {
		if (handle != 0) {
			glDeleteProgram(handle);
			glDeleteShader(vertHandle);
			glDeleteShader(fragHandle);
		}
	}

	@Override
	public int getHandle() {
		return handle;
	}
	
	public void setUniform(String name, int value) {
		int index = glGetUniformIndices(handle, name);
		if (index != -1) {
			glUniform1i(index, value);
		} else {
			System.out.println("Can't set uniform - it doesn't exist");
		}
	}
	
}
