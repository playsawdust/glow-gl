package com.playsawdust.glow.gl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import com.playsawdust.glow.render.Painter;
import com.playsawdust.glow.vecmath.Vector2i;

import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements GLResource {
	static {
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		GLFWErrorCallback.createPrint(System.out).set();
	}
	
	public static void shutdown() {
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null);
	}
	
	private final long handle;
	private final WindowPainter painter;
	
	public Window(String title) {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		handle = GLFW.glfwCreateWindow(640, 480, title, NULL, NULL);
		
		if (handle == NULL) {
			throw new RuntimeException("Couldn't create the window.");
		}
		
		GL.createCapabilities();
		painter = new WindowPainter(this);
	}
	
	public Window() {
		this("");
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			GLFW.glfwShowWindow(handle);
		} else {
			GLFW.glfwHideWindow(handle);
		}
	}
	
	/**
	 * Presents the frame being assembled by this Window's Painter and/or Renderer. Polls for events immediately after
	 * the swap.
	 */
	public void presentFrame() {
		GLFW.glfwSwapBuffers(handle);
		GLFW.glfwPollEvents();
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}

	public Painter getPainter() {
		return painter;
	}
	
	public Vector2i getSize() {
		int[] x = new int[1];
		int[] y = new int[1];
		GLFW.glfwGetWindowSize(handle, x, y);
		return new Vector2i(x[0], y[0]);
	}
	
	public int getWidth() {
		int[] x = new int[1];
		GLFW.glfwGetWindowSize(handle, x, null);
		return x[0];
	}
	
	public int getHeight() {
		int[] y = new int[1];
		GLFW.glfwGetWindowSize(handle, null, y);
		return y[0];
	}
	
	@Override
	public void destroy() {
		GLFW.glfwDestroyWindow(handle);
	}

	@Override
	public long getHandle() {
		return handle;
	}
}
