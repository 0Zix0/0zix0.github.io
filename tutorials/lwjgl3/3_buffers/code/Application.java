package com.tutorial;

import org.lwjgl.opengl.GL11;

public class Application {
	
	private Settings settings;
	private Display display;
	
	public Application(Settings settings) {
		this.settings = settings;
	}
	
	private void init() {
		this.display = new Display(settings.title, settings.width, settings.height);
	}
	
	private void loop() {
		float[] vertices = {
				0.0f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f
		};
		VertexArray vao = new VertexArray();
		// Create our buffer, passing the vertices and the size of each vertex.
		Buffer buffer = new Buffer(vertices, 3);
		// Add the buffer into position 0.
		vao.addBuffer(buffer, 0);
		
		while(!display.shouldClose()) {
			display.clear();

			// Bind the VAO.
			vao.bind();
			// Tell OpenGL to render our vertices as a triangle
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / buffer.getComponentCount());
			// Unbind the VAO.
			vao.unbind();
			
			display.update();
		}
		clean();
	}
	
	private void clean() {
		display.delete();
	}
	
	public void run() {
		init();
		loop();
	}
}
