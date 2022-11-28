package com.playsawdust.glow.gl;

public class StandardShaders {
	public static final String SIMPLE_VERTEX = """
			#version 330 core
			layout (location = 0) in vec3 position;
			void main()
			{
				gl_Position = vec4(position.x, position.y, position.z, 1.0);
			}
			""";
	
	public static final String SIMPLE_FRAGMENT = """
			#version 330 core
			out vec4 FragColor;
			uniform vec4 diffuse_color;
			void main()
			{
				FragColor = diffuse_color;
			}
			""";
}
