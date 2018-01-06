package com.tutorial;

import org.lwjgl.opengl.GL15;

public class Buffer {

	private int id;
	private int componentCount;
	private int size;
	
	public Buffer(float[] data, int componentCount) {
		this.componentCount = componentCount;
		this.size = data.length;
		id = GL15.glGenBuffers();
		// Tell OpenGL which buffer we want to work with.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
		// Add this data to the buffer.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		// Unbind the buffer by passing zero.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void bind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
	}
	
	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public int getComponentCount() {
		return componentCount;
	}
	
	public int getSize() {
		return size;
	}
	
	public void delete() {
		// Delete our data from the GPU.
		GL15.glDeleteBuffers(id);
	}
}
