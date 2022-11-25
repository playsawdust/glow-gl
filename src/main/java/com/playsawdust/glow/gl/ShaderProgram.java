package com.playsawdust.glow.gl;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import com.playsawdust.glow.gl.offheap.GLResource;
import com.playsawdust.glow.vecmath.Matrix2;
import com.playsawdust.glow.vecmath.Matrix3;
import com.playsawdust.glow.vecmath.Vector2d;
import com.playsawdust.glow.vecmath.Vector3d;
import com.playsawdust.glow.vecmath.Vector4d;

public class ShaderProgram implements GLResource {
	private final int programHandle;
	private final int vertHandle;
	private final int fragHandle;
	
	/* Note: An attribute's binding is its "location" in the list, which usually corresponds to its binding location */
	private ArrayList<AttributeEntry> attributes = new ArrayList<>();
	private HashMap<String, Integer> attributeBindings = new HashMap<>();
	
	private ArrayList<AttributeEntry> uniforms = new ArrayList<>();
	private HashMap<String, Integer> uniformBindings = new HashMap<>();
	
	/**
	 * Creates and compiles a shader program from GLSL source.
	 * @param vert
	 * @param frag
	 * @throws ShaderException if there is a problem with the compilation or linking. If this exception is thrown, this
	 *                         object is already destroyed.
	 */
	public ShaderProgram(String vert, String frag) throws ShaderException {
		programHandle = GL20.glCreateProgram();
		vertHandle = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragHandle = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		
		GL20.glShaderSource(vertHandle, vert);
		compile(vertHandle);
		
		GL20.glShaderSource(fragHandle, frag);
		compile(fragHandle);
		
		GL20.glAttachShader(programHandle, vertHandle);
		GL20.glAttachShader(programHandle, fragHandle);
		GL20.glLinkProgram(programHandle);
		
		int linkStatus = GL20.glGetProgrami(programHandle, GL20.GL_LINK_STATUS);
		if (linkStatus == GL20.GL_FALSE) {
			String error = GL20.glGetProgramInfoLog(programHandle);
			destroy();
			throw new ShaderException(error);
		}
		
		//TODO: Consider saving or sharing non-error shader logs
		
		try (MemoryStack stackFrame = MemoryStack.stackPush()) {
			IntBuffer sizeBuf = stackFrame.mallocInt(1);
			IntBuffer typeBuf = stackFrame.mallocInt(1);
			
			//Find out about varying attributes
			int attributeCount = GL20.glGetProgrami(programHandle, GL20.GL_ACTIVE_ATTRIBUTES);
			for(int i=0; i<attributeCount; i++) {
				String name = GL20.glGetActiveAttrib(programHandle, i, sizeBuf, typeBuf);
				int location = GL20.glGetAttribLocation(programHandle, name);
				
				AttributeEntry entry = new AttributeEntry(name, location, typeBuf.get());
				attributes.add(entry);
				attributeBindings.put(name, location);
			}
			
			
			//Find out about uniforms
			int uniformCount = GL20.glGetProgrami(programHandle, GL20.GL_ACTIVE_UNIFORMS);
			for(int i=0; i<uniformCount; i++) {
				String name = GL20.glGetActiveUniform(programHandle, i, sizeBuf, typeBuf);
				int location = GL20.glGetUniformLocation(programHandle, name);
				
				AttributeEntry entry = new AttributeEntry(name, location, typeBuf.get());
				uniforms.add(entry);
				uniformBindings.put(name, location);
			}
		}
	}
	
	private void compile(int handle) throws ShaderException {
		GL20.glCompileShader(handle);
		int status = GL20.glGetShaderi(handle, GL20.GL_COMPILE_STATUS);
		if (status == GL20.GL_FALSE) {
			String error = GL20.glGetShaderInfoLog(handle);
			destroy();
			throw new ShaderException(error);
		}
	}
	
	public void bind() {
		GL20.glUseProgram(programHandle);
	}
	
	public int getAttributeBinding(String name) {
		Integer result = attributeBindings.get(name);
		return (result==null) ? -1 : result;
	}
	
	public int getUniformBinding(String name) {
		Integer result = uniformBindings.get(name);
		return (result==null) ? -1 : result;
	}
	
	public void setUniform(String name, int value) {
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniform1i(binding, value);
	}
	
	public void setUniform(String name, float value) {
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniform1f(binding, value);
	}
	
	public void setUniform(String name, Vector2d value) {
		//TODO: Coerce types
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniform2f(binding, (float) value.x(), (float) value.y());
	}
	
	public void setUniform(String name, Vector3d value) {
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniform3f(binding, (float) value.x(), (float) value.y(), (float) value.z());
	}
	
	public void setUniform(String name, Vector4d value) {
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniform4f(binding, (float) value.x(), (float) value.y(), (float) value.z(), (float) value.w());
	}
	
	public void setUniform(String name, Matrix2 value) {
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniformMatrix2fv(binding, false, new float[] {
				(float) value.a(), (float) value.b(),
				(float) value.c(), (float) value.d()
		});
	}
	
	public void setUniform(String name, Matrix3 value) {
		Integer binding = uniformBindings.get(name);
		if (binding==null) return;
		GL20.glUniformMatrix3fv(binding, false, new float[] {
				(float) value.a(), (float) value.b(), (float) value.c(),
				(float) value.d(), (float) value.e(), (float) value.f(),
				(float) value.g(), (float) value.h(), (float) value.i()
		});
	}
	
	@Override
	public void destroy() {
		GL20.glDeleteProgram(programHandle);
		GL20.glDeleteShader(vertHandle);
		GL20.glDeleteShader(fragHandle);
	}
	
	private static class AttributeEntry {
		public String name;
		public int binding;
		public int type;
		
		public AttributeEntry(String name, int binding, int type) {
			this.name = name;
			this.type = type;
			this.binding = binding;
		}
	}
}
