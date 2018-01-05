# The Application Class
Having all of our game code in the main method is sloppy and generally considered a bad idea, in this article we will be moving this code out into our own seperate `Application` class.
We will start by creating this class in our project.
```java
public class Application {
	
	public Application() {
	}
	
	private void init() {
	}
	
	private void loop() {
	    clean();
	}
	
	private void clean() {
	}
	
	public void run() {
		init();
		loop();
	}
}
```
Rather than passing a large amount of arguments to our constructor, we will create a new class which we will name `Settings`, this settings class will allow us to easily change properties of our application such as the window width, height and title, as well as other things down the track.
```java
public class Settings {

	public String title = "My App";
	public int width = 1280;
	public int height = 720;
}
```
Now we can modify our application class to make use of this settings object.
```java
private Settings settings;

public Application(Settings settings) {
	this.settings = settings;
}
```
Next, in the application `init()` method, we can create our window, just as before. Dont forgot to declare a variable to hold the `Display` instance.
```java
this.display = new Display(settings.title, settings.width, settings.height);
```
We can also move our looping code from the main method into the Applications `loop()` method and the clean up code into the `clean()` method.
```java
private void loop() {
	while(!display.shouldClose()) {
		display.clear();
		
		display.update();
	}
	clean();
}

private void clean() {
	display.delete();
}
```
All that is left to do is to clean up our main method to make use of the new application and settings classes.
```java
public static void main(String[] args) {
	Settings settings = new Settings();
	settings.title = "LWJGL Tutorial";
	
	Application app = new Application(settings);
	app.run();
}
```
If all went well the program should run exactly the same as last time, if not, feel free to consult the code on GitHub. TOOD: link the code