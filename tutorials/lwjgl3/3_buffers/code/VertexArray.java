package com.tutorial;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArray {

	private int id;
	private ArrayList<Buffer> buffers = new ArrayList<>();
	
	public VertexArray() {
		this.id = GL30.glGenVertexArrays();
	}
	
	public void addBuffer(Buffer buffer, int index) {
		// Bind our VAO and buffer.
		GL30.glBindVertexArray(id);
		buffer.bind();
		
		// Our index now stores data.
		GL20.glEnableVertexAttribArray(index);
		// Tell the VAO about our data.
		GL20.glVertexAttribPointer(index, buffer.getComponentCount(), GL11.GL_FLOAT, false, 0, 0);
		
		// Unbind the buffer and VAO.
		buffer.unbind();
		GL30.glBindVertexArray(0);
		
		// Add  the buffer to our list so we still have it around.
		buffers.add(buffer);
	}

	public void bind() {
		GL30.glBindVertexArray(id);
	}
	
	public void unbind() {
		GL30.glBindVertexArray(0);
	}
}
