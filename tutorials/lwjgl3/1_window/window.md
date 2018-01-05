# Setting up GLFW

We will start off by statically importing GLFW, paste this line of code at the top of your file.
```java
import static org.lwjgl.glfw.GLFW.*;
```
Next we will create a main method and initialize GLFW with a simple function call.

```java
public static void main(String[] args) {
	glfwInit();
}
```
However, initialization of GLFW can fail in some cases, if `glfwInit()` fails, it will return false.  We will add a check to make sure that everything went smoothly.
```java
if(!glfwInit()) {
	throw new RuntimeException("Failed to initialize GLFW.");
}
```
If you run this code as-is, nothing exciting will happen. Unless you manage to throw our little exception, but lets hope that doesn't happen. In order to actually see a window we have to create one. We will keep track of our window instance by declaring `long` value which GLFW will create for us. This handle will have to be kept for later on in execution if we wish to modify any of our windows properties or perform operations on it.
Add this code under the GLFW initialization code.
```java
long window;
window = glfwCreateWindow(1280, 720, "My Window", 0, 0);
```
The first two parameters passed to `glfwCreateWindow` are the width and height of the window, the second parameter is the title of the window. The final two parameters are not important at this stage, so no need to worry about them.
However just as before, the call to `glfwCreateWindow` can fail, if this is the case it will return a value of 0.
We will add some error checking code after the creation of the window just to ensure that the window was created successfully.
```java
if(window == 0) {
	// Note how we make a call to glfwTerminate(), this is to
	// clean up and resources that glfwInit() may have allocated.
	glfwTerminate();
	throw new RuntimeException("Failed to create GLFW window.");
}
```
You may be dissapointed to realize that the code that we have written so far is not enough to get anything shown on screen yet, but don't worry! We will get there in time.
Next we will set up GLFW to work with OpenGL.
```java
glfwMakeContextCurrent(window);
GL.createCapabilities();
glfwShowWindow(window);
```
The first two methods set up OpenGL and the third one makes the window visible, although only for a brief moment.
The solution to this is to introduce a loop which we can use to render to the screen.
Throw this code in underneath `glfwShowWindow(window);`.
```java
// While the user has not closed the window
while(!glfwWindowShouldClose(window)) {
	// Clear the screen
	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	
	// Swap the front and  back buffers
	glfwSwapBuffers(window);
	// Check for input events
	glfwPollEvents();
}
glfwTerminate();
```
Here is an explanation of what is going on here;
`while(!glfwWindowShouldClose(window)) {` Loops for as long as our window is alive (not been closed in some way).
`GL11.glClear(..);` Tells OpenGL to clear the screen.
`glfwSwapBuffers(window);` Tells GLFW to [swap the front and back buffers](https://en.wikipedia.org/wiki/Multiple_buffering]).
`glfwPollEvents();` Checks for any input.
`glfwTerminate();` Once our program as closed, we should clean up GLFW.

If you run the code now you should see a window with a black screen. It may not look like much but this is the base for any sort of graphics application that you want to make.
## Clean up
This code is all well and good, but it would be much better if we could pack it into its own class.
```java
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
	
	public void delete() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}
}
```
Now we can simplify our existing main method by removing everything and creating a new instance of this new `Display` class.
```java
public static void main(String[] args) {
	Display display = new Display("My Engine", 1280, 720);
	while(!display.shouldClose()) {
		display.clear();
		
		display.update();
	}
	display.delete();
}
```
## That's it!
That is all the windowing code that we need in order to get a simple window to display.
Next time we will extend our existing code to be able to render simple shapes.
