package com.playsawdust.glow.gl;

import org.lwjgl.glfw.GLFW;

public record GLVersion(API api, Profile profile, int major, int minor) {
	
	public static GLVersion gl(int major, int minor) {
		return new GLVersion(API.OPENGL, Profile.ANY, major, minor);
	}
	
	public static GLVersion glCompat(int major, int minor) {
		return new GLVersion(API.OPENGL, Profile.COMPAT, major, minor);
	}
	
	public static GLVersion es(int major, int minor) {
		return new GLVersion(API.OPENGL_ES, Profile.CORE, major, minor);
	}
	
	public String toString() {
		return api.toString() + " " + profile.toString() + " " + major + "." + minor;
	}
	
	public void setWindowHints() {
		if (api == API.OPENGL) {
			
			if ((major==3 && minor<2) || major<3) {
				if (profile == Profile.CORE) throw new IllegalArgumentException("Versions under 3.2 cannot be Core Profiles");
				
				GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, api.value());
				GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, major);
				GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minor);
				
				return;
			}
		}
		
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, api.value());
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, major);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, minor);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, profile.value());
	}
	
	public static enum API {
		NO_API   ("(No API)",  GLFW.GLFW_NO_API),
		OPENGL   ("OpenGL",    GLFW.GLFW_OPENGL_API),
		OPENGL_ES("OpenGL ES", GLFW.GLFW_OPENGL_ES_API);
		
		private final String name;
		private final int value;
		
		API(String name, int value) {
			this.name = name;
			this.value = value;
		}
		
		public int value() {
			return value;
		}
		
		public static API of(int value) {
			for(API api : values()) {
				if (api.value == value) return api;
			}
			
			return null;
		}
		
		public String toString() { return name; }
	}
	
	public static enum Profile {
		CORE  ("Core",   GLFW.GLFW_OPENGL_CORE_PROFILE),
		COMPAT("Compat", GLFW.GLFW_OPENGL_COMPAT_PROFILE),
		ANY   ("Any",    GLFW.GLFW_OPENGL_ANY_PROFILE)
		;
		
		private final String name;
		private final int value;
		
		Profile(String name, int value) {
			this.name = name;
			this.value = value;
		}
		
		public int value() {
			return value;
		}
		
		public static Profile of(int value) {
			for(Profile profile : values()) {
				if (profile.value == value) return profile;
			}
			
			return null;
		}
		
		public String toString() { return name; }
	}
}
