# Vertex Arrays
In this tutorial we will begin sending data to the GPU in order to be processed and output onto the screen. But before we get into writing any code we have to discuss the theory behind buffers in OpenGL. You may hear the term "immediate mode" thrown around a little but for the sake of this tutorial, forget all about it. Immediate mode is deprecated and slow, we will be using the newer, faster method for drawing to the screen.

Say we wanted to draw a triangle to the screen, we have to send some data to the GPU so it knows what to draw and where to draw it. The data we send could be 3 vertices that, when connected, form a triangle on the screen.
![triangle]
But how do we actually go about sending this data to the GPU? Should we continually tell the GPU what this data is so that it can draw it each frame?
The anwser is to use Vertex Arrays as well as Buffers. Vertex arrays allow us to send the data to the GPU only once, then invoke a call which tells the GPU to draw our vertices.
So think of vertex arrays as containers for our data, as they can hold many different types of data, such as vertex positions, texture coordinates, normals and more.

Now lets get to some code, create a new class in your project and call it `VertexArray`.
```java
public class VertexArray {

	private int id;
	
	public VertexArray() {
		
	}
}
```
As you can see, we have declared an integer to store the ID of our vertex array object (VAO). We have to ask OpenGL for this ID, and we will use it later on when we want to add data to our array.
```java
public VertexArray() {
	this.id = GL30.glGenVertexArrays();
}
```
Now that we have created a VAO, we can add some data to it. As we are going for an object-oriented approach, we will create a new class to represent some buffer that we can store in our vertex array. For now, all of our buffers will be hard coded to only be able to store float values. Our `Buffer` class will take in the array of data we want to send to the GPU as well as how many floats each element takes up. For example, a vertex position requires three floats, so the component count would be three and the total number of floats in the array should be multiple of three.
```java
public class Buffer {

	private int id;
	private int componentCount;
	private int size;
	
	public Buffer(float[] data, int componentCount) {
		this.componentCount = componentCount;
		this.size = data.length;
		id = GL15.glGenBuffers();
	}
}
```
Just as before we have to ask OpenGL to create a buffer for us, we also have to keep track of the size of the data.
But we are yet to associate our array of float values with our buffer. In order to operate on any OpenGL object, we have to tell OpenGL which object we want to work with, this process is often known as 'binding'.
We will add our data with the following 3 lines of code, placed just under `glGenBuffers`.
```java
// Tell OpenGL which buffer we want to work with.
GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
// Add this data to the buffer.
GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
// Unbind the buffer by passing zero.
GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
```
Note how we do not store our array of data, that data has now been sent the the GPU where it will live until the end of our program.
We will finish our `Buffer` class off with some utility methods.
```java
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
```
We will switch back to our `VertexArray` class where we will create a list to store all of the buffers associated with a given VAO.
```java
private ArrayList<Buffer> buffers = new ArrayList<>();
```
Adding a buffer object to our vertex array object involves a couple of steps, so we will create a new method to deal with it.
```java
public void addBuffer(Buffer buffer, int index) {
	
}
```
Note that this function takes in an integer index, if you think of the VAO as an array of buffers, think of this index as the index in the array which the buffer will be stored in.
The process for adding a buffer object to a vertex array is as follows;
- Bind both the VAO and the buffer
- Tell the VAO that the supplied index is now being used for holding data
- Tell the VAO the required information about the buffer
- Unbind the VAO and buffer

When we translate this to code, it looks like this:
```java
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
```
The meat of this function is the call to `glVertexAttribPointer` which takes in the index of our buffer, how many individual components are in the buffer, the type of data which in our case a float as well as 3 extra parameters which we do not need to concern ourselves with at the moment.
Just as we did in the `Buffer` class, we will create some methods to bind and unbind the vertex array.
```java
public void bind() {
	GL30.glBindVertexArray(id);
}
public void unbind() {
	GL30.glBindVertexArray(0);
}
```
We are all ready to render a triangle to the screen, but what vertex posisions do we add to our buffer? To find out we need to look at the screen not as an array of pixels, but as a cartesian plane.
![screenspace]
As we see here the coordinates `0, 0` are the center of the screen and the left, right, top and bottoms of it are a single 'unit' away from the center.
I have marked our desired positions on the graph below.
![trianglepos]
Now we can go into our applications loop method and create the array that will hold our positions.
```java
float[] vertices = {
		0.0f, 0.5f, 0.0f,
		-0.5f, -0.5f, 0.0f,
		0.5f, -0.5f, 0.0f
};
```
Make note of the fact that these are 3D coordinates, not 3D coordinates.
Now we create a `VertexArray` and add a buffer to it.
```java
VertexArray vao = new VertexArray();
// Create our buffer, passing the vertices and the size of each vertex.
Buffer buffer = new Buffer(vertices, 3);
// Add the buffer into position 0.
vao.addBuffer(buffer, 0);
```
Lastly, inside of the actual loop we can bind the VAO and call the `glDrawArrays` method.
```java
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
```
The first argument of `glDrawArrays` is the type of primitive we are drawing, currently we are only drawing triangles. If you change this argument to `GL_LINE_LOOP` you should see the outline of a triangle. The second argument is where in the array we want to begin drawing from, we want to draw from the start, so we pass zero. The final argument is the size of each vertex. We calculate this by dividing the total amount of vertices by how many vertices are in the array. For this example we get `9 / 3 = 3`.
![render]
In the next tutorial we will extend our code to draw a rectangle, we will also introduce an important optimitation, index buffers.

[triangle]: https://i.imgur.com/WUd1wOp.png
[screenspace]: https://i.imgur.com/ZHecaYK.png
[trianglepos]: https://i.imgur.com/w30j7Tx.png
[render]: https://i.imgur.com/BcFJ488.png