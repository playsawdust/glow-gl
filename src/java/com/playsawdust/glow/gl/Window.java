package com.playsawdust.glow.gl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import com.playsawdust.glow.offheap.Destroyable;
import com.playsawdust.glow.render.Painter;
import com.playsawdust.glow.vecmath.Vector2i;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.function.Consumer;

/*
 * A Note About Compatibility:
 * 
 * IF I was making a game for Linux and Windows alone, OpenGL 4.6 is nearly universally supported across Windows PCs,
 * the Steam Deck and RoG-style handhelds, laptops, linux computers of all stripes, the gabecube, VR headsets, etc.. I
 * could potentially scale out to OpenGL ES 3.2 ish with minimal code disruption, and we can always circle back to the
 * ES topic if it seems like we want that portability.
 * 
 * However, MacOS X 10.9 (Mavericks), released in 2013, is a significant milestone for OpenGL portability. Before
 * Mavericks, they're stuck on GL 3.2. After Mavericks, they can handle GL 4.1 but no higher. So if we want to land on
 * all three platforms on machines newer than 2013, we can safely pick GL 4.1 - this actually gives us quite a lot of
 * expressive freedom to make visually appealing yet performant renders.
 * 
 * Moving forward, if Apple discontinues support for GL 4.1, we will respond by discontinuing MacOS support and moving
 * to OpenGL 4.6 to maximize value for our remaining users.
 */

public class Window implements Destroyable {
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
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		handle = GLFW.glfwCreateWindow(640, 480, title, NULL, NULL);
		
		if (handle == NULL) {
			throw new RuntimeException("Couldn't create the window.");
		}
		
		GLFW.glfwMakeContextCurrent(handle);
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

	public void paint(Consumer<Painter> paintMethod) {
		painter.startDrawing();
		paintMethod.accept(painter);
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
	
	public long getHandle() {
		return handle;
	}
}
