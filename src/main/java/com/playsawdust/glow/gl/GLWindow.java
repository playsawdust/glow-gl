/**
 * Glow - GL Object Wrapper
 * Copyright (C) 2020-2022 the Chipper developers
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.playsawdust.glow.gl;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import com.playsawdust.glow.gl.offheap.GLResource;
import com.playsawdust.glow.vecmath.Rect2d;
import com.playsawdust.glow.vecmath.Vector2d;

/**
 * Window and context are used interchangeably by GLFW. This object serves both functions.
 */
public class GLWindow implements GLResource {
	/** When the last window is closed, terminate GLFW. */
	private static long refCount = 0L;
	
	
	private final long handle;
	
	public GLWindow(int width, int height, GLVersion version) {
		GLFWErrorCallback.createPrint(System.out).set(); //System.out for now; callback later.
		
		//Safe to call multiple times for multiple windows, even if glfwTerminate has been called.
		if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to init GLFW.");
		refCount++;
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		version.setWindowHints();
		/*
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, version.api().value());
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, version.major());
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, version.minor());
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, version.profile().value());
		*/
		
		handle = GLFW.glfwCreateWindow(width, height, "Window", MemoryUtil.NULL, MemoryUtil.NULL);
		if (handle == MemoryUtil.NULL) throw new IllegalStateException("Could not create the window.");
		
		GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
			if ( key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE )
				GLFW.glfwSetWindowShouldClose(window, true);
		});
		
		
		//int profile = GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_OPENGL_PROFILE);
		//if (profile == GLFW.GLFW_OPENGL_CORE_PROFILE) System.out.println("OPENGL Core Profile Obtained");
		//else if (profile == GLFW.GLFW_OPENGL_COMPAT_PROFILE) System.out.println("OPENGL Compat Profile Obtained");
		//else if (profile == GLFW.GLFW_NO_API) System.out.println("No API!");
		//else System.out.println("Unknown Profile: "+profile);
	}
	
	public long handle() {
		return handle;
	}
	
	public GLVersion getVersion() {
		int profile = GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_OPENGL_PROFILE);
		int major = GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_CONTEXT_VERSION_MAJOR);
		int minor = GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_CONTEXT_VERSION_MINOR);
		int api = GLFW.glfwGetWindowAttrib(handle, GLFW.GLFW_CLIENT_API);
		
		return new GLVersion(GLVersion.API.of(api), GLVersion.Profile.of(profile), major, minor);
	}
	
	public Vector2d getPosition() {
		try (MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer xBuf = stack.mallocInt(1);
			IntBuffer yBuf = stack.mallocInt(1);
			
			GLFW.glfwGetWindowPos(handle, xBuf, yBuf);
			int x = xBuf.get();
			int y = yBuf.get();
			
			return new Vector2d(x, y); //TODO: Vec2i?
		}
	}
	
	public Vector2d getSize() {
		try (MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer xBuf = stack.mallocInt(1);
			IntBuffer yBuf = stack.mallocInt(1);
			
			GLFW.glfwGetWindowSize(handle, xBuf, yBuf);
			int width = xBuf.get();
			int height = yBuf.get();
			
			return new Vector2d(width, height); //TODO: Vec2i? Dimension?
		}
	}
	
	public Rect2d getRect() {
		try (MemoryStack stack = MemoryStack.stackPush() ) {
			IntBuffer xBuf = stack.mallocInt(1);
			IntBuffer yBuf = stack.mallocInt(1);
			
			GLFW.glfwGetWindowPos(handle, xBuf, yBuf);
			int x = xBuf.get();
			int y = yBuf.get();
			
			GLFW.glfwGetWindowSize(handle, xBuf, yBuf);
			int width = xBuf.get();
			int height = yBuf.get();
			
			return new Rect2d(x, y, width, height); //TODO: Rect2i?
		}
	}
	
	/**
	 * Sets the position of this window. Does not work in Wayland. Unpredictable for already-shown windows.
	 */
	public void setPosition(int x, int y) {
		GLFW.glfwSetWindowPos(handle, x, y);
	}
	
	public void centerOnScreen() {
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		Vector2d size = getSize();
		setPosition(
				(vidmode.width() - (int) size.x()) / 2,
				(vidmode.height() - (int) size.y()) / 2
				);
	}
	
	/**
	 * Sets the size of the window's *content area*. The window will be larger due to decorations. Doesn't work
	 * predictably for fullscreen windows - may change screen resolution, but won't on Wayland.
	 */
	public void setSize(int width, int height) {
		GLFW.glfwSetWindowSize(handle, width, height);
	}
	
	public void setRect(int x, int y, int width, int height) {
		setSize(width, height); //Set size first because it could dirty the position on some platforms.
		setPosition(x, y);
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			GLFW.glfwShowWindow(handle);
		} else {
			GLFW.glfwHideWindow(handle);
		}
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(handle, title);
	}
	
	/**
	 * Makes this window's context current, so that openGL calls will be directed here.
	 */
	public void makeCurrent() {
		GLFW.glfwMakeContextCurrent(handle);
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(handle);
	}
	
	public void poll() {
		GLFW.glfwPollEvents();
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(handle);
	}
	
	@Override
	public void destroy() {
		GLFW.glfwDestroyWindow(handle);
		refCount--;
		if (refCount <= 0L) GLFW.glfwTerminate();
	}
	
	/**
	 * Terminates / uninitializes the GLFW library, destroying all cursors and windows and releasing their resources.
	 */
	public static void destroyAll() {
		refCount = 0;
		GLFW.glfwTerminate();
	}

	
}
