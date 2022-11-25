package com.playsawdust.glow.gl;

/**
 * Thrown by shaders when something goes wrong with their compilation or assembly.
 */
public class ShaderException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ShaderException() {
		super();
	}
	
	public ShaderException(String message) {
		super(message);
	}
	
	public ShaderException(String message, Throwable t) {
		super(message, t);
	}
}
