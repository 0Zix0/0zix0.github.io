package com.tutorial;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Display {
	
	private String title;
	private int width;
	private int height;
	
	private long window;
	
	public Display(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		init();
	}
	
	private void init() {
		if(!glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW.");
		}
		
		window = glfwCreateWindow(width, height, title, 0, 0);
		if(window == 0) {
			// Note how we make a call to glfwTerminate(), this is to
			// clean up and resources that glfwInit() may have allocated.
			glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window.");
		}
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void clear() {
		// Clear the screen
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void update() {
		// Swap the front and  back buffers
		glfwSwapBuffers(window);
		// Check for input events
		glfwPollEvents();
	}
}
