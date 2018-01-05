package com.tutorial;

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
		while(!display.shouldClose()) {
			display.clear();

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
