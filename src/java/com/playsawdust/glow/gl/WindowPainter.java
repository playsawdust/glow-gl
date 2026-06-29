package com.playsawdust.glow.gl;

import com.playsawdust.glow.gl.shader.ShaderProgram;
import com.playsawdust.glow.image.ImageData;
import com.playsawdust.glow.image.color.RGBColor;
import com.playsawdust.glow.offheap.Destroyable;
import com.playsawdust.glow.render.Painter;
import com.playsawdust.glow.vecmath.HalfFloat;
import com.playsawdust.glow.vecmath.Matrix4;
import com.playsawdust.glow.vecmath.Vector2d;
import com.playsawdust.glow.vecmath.Vector2i;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL41.*;
import static org.lwjgl.system.MemoryStack.stackGet;
import static org.lwjgl.system.MemoryUtil.memASCII;
import static org.lwjgl.system.MemoryUtil.memAddress;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class WindowPainter implements Painter, Destroyable {
	public static final String VERT_SHADER_SRC = """
			#version 330
			
			uniform mat4 transform;
			
			layout(location=0) in vec2 position;
			layout(location=1) in vec4 color;
			layout(location=2) in vec2 uv;
			out vec4 vertexColor;
			out vec2 vertexUv;
			
			void main() {
				vertexColor = color;
				vertexUv = uv;
				gl_Position = transform * vec4(position, 0.0, 1.0);
			}
			""";
	
	public static final String FRAG_SHADER_SRC = """
			#version 330
			
			uniform sampler2D materialTexture;
			
			in vec4 vertexColor;
			in vec2 vertexUv;
			out vec4 fragColor;
			
			void main() {
				vec4 texColor = texture(materialTexture, vertexUv);
				fragColor = vertexColor; // * texColor;
			}
			""";
	
	private final VertexArray vertexArray;
	private final Window target;
	private ShaderProgram shader;
	
	public WindowPainter(Window target) {
		this.target = target;
		this.shader = new ShaderProgram(VERT_SHADER_SRC, FRAG_SHADER_SRC);
		this.vertexArray = new VertexArray();
	}
	
	public void startDrawing() {
		glDisable(GL_CULL_FACE);
		shader.bind();
		Vector2i windowSize = target.getSize();
		int transformLoc = glGetUniformLocation(shader.getHandle(), "transform");
		glUniformMatrix4fv(transformLoc, true, orthoMatrix(windowSize.x(), windowSize.y()));
	}
	
	public void clear(RGBColor color) {
		glClearColor(color.r(), color.g(), color.b(), 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	@Override
	public int getWidth() {
		return target.getWidth();
	}

	@Override
	public int getHeight() {
		return target.getHeight();
	}

	@Override
	public void drawImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height, float opacity) {
		// TODO: FIXME THIS WILL BE VERY SLOW
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				RGBColor col = image.getLinearPixel(srcX + x, srcY + y);
				fillRect(destX + x, destY + y, 1, 1, col);
			}
		}
	}

	@Override
	public void drawTintImage(ImageData image, int destX, int destY, int srcX, int srcY, int width, int height, RGBColor tintColor) {
		// TODO: FIXME THIS WILL NOT TINT THE IMAGE
		drawImage(image, destX, destY, srcX, srcY, width, height, 1.0f);
	} 

	@Override
	public void drawPixel(int x, int y, RGBColor color) {
		fillRect(x, y, 1, 1, color);
	}
	
	public static String unformInfo(ShaderProgram program) {
		MemoryStack stack = MemoryStack.stackGet();
		
		StringBuilder result = new StringBuilder();
		int count = glGetProgrami(program.getHandle(), GL_ACTIVE_ATTRIBUTES);
		if (count > 0) {
			result.append("Uniforms:\n");
			for(int i=0; i<count; i++) {
				stack.push();
				try {
					int nameLength = glGetActiveUniformsi(program.getHandle(), i, GL_UNIFORM_NAME_LENGTH);
					IntBuffer lengthBuf = stack.ints(0);
					IntBuffer sizeBuf = stack.ints(0);
					IntBuffer typeBuf = stack.ints(0);
					ByteBuffer nameBuf = stack.malloc(nameLength);
					glGetActiveUniform(program.getHandle(), i, lengthBuf, sizeBuf, typeBuf, nameBuf);
					String name = MemoryUtil.memASCII(nameBuf, nameLength).trim();
					
					result.append("  ");
					result.append(name);
					result.append(": ");
					result.append("len = ");
					result.append(lengthBuf.get(0));
					result.append(", size = ");
					result.append(sizeBuf.get(0));
					result.append(", type = ");
					result.append(GLType.of(typeBuf.get(0)));
					result.append('\n');
				} finally {
					stack.pop();
				}
				
				
				
			}
		}
		count = glGetProgrami(program.getHandle(), GL_ACTIVE_ATTRIBUTES);
		if (count > 0) {
			result.append("Attributes:\n");
			
			for(int i=0; i<count; i++) {
				stack.push();
				try {
					//int nameLength = glGetActiveAttribsi(program.getHandle(), i, GL_ATTRIB_);
					IntBuffer lengthBuf = stack.ints(16);
					IntBuffer sizeBuf = stack.ints(0);
					IntBuffer typeBuf = stack.ints(0);
					ByteBuffer nameBuf = stack.malloc(16);
					glGetActiveAttrib(program.getHandle(), i, lengthBuf, sizeBuf, typeBuf, nameBuf);
					
					String name = MemoryUtil.memASCII(nameBuf, lengthBuf.get(0));
					
					result.append("  ");
					result.append(name);
					result.append(": ");
					result.append("len = ");
					result.append(lengthBuf.get(0));
					result.append(", size = ");
					result.append(sizeBuf.get(0));
					result.append(", type = ");
					result.append(GLType.of(typeBuf.get(0)));
				} finally {
					stack.pop();
				}
				
				
				
			}
		}
		return result.toString();
	}
	
	public static float[] orthoMatrix(float left, float right, float bottom, float top, float near, float far) {
		float tx = -(right+left) / (right-left);
		float ty = -(top+bottom) / (top-bottom);
		float tz = -(far+near) / (far-near);
		
		return new float[] {
			2f / (right-left), 0,                 0,               tx,
			0,                 2f / (top-bottom), 0,               ty,
			0,                 0,                -2f / (far-near), tz,
			0,                 0,                 0,                1
		};
	}
	
	public static float[] orthoMatrix(int width, int height) {
		return orthoMatrix(0, width, height, 0, -1, 1);
	}
	
	@Override
	public void fillRect(int x, int y, int width, int height, RGBColor color) {
		shader.bind();
		vertexArray.bind();
		vertexArray.bindData(0,
			new float[] {
				x, y,
				x+width, y,
				x, y+height,
				x+width, y+height,
			},
			GLType.FLOAT_VEC2);
		vertexArray.bindData(1,
			new float[] {
				color.r(), color.g(), color.b(), 1,
				color.r(), color.g(), color.b(), 1,
				color.r(), color.g(), color.b(), 1,
				color.r(), color.g(), color.b(), 1
			},
			GLType.FLOAT_VEC4);
		vertexArray.bindData(2,
			new int[] {
				HalfFloat.vecToHalfVec(new Vector2d(0, 0)),
				HalfFloat.vecToHalfVec(new Vector2d(1, 0)),
				HalfFloat.vecToHalfVec(new Vector2d(0, 1)),
				HalfFloat.vecToHalfVec(new Vector2d(1, 1)),
			},
			GLType.HALF_VEC2);
		vertexArray.bindIndices(3, new int[] { 0, 1, 3, 0, 3, 2 });
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}

	@Override
	public void destroy() {
		vertexArray.destroy();
		shader.destroy();
	}
	
}
