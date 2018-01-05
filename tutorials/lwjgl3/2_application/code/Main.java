package com.tutorial;

public class Main {

	public static void main(String[] args) {
		Settings settings = new Settings();
		settings.title = "LWJGL Tutorial";
		
		Application app = new Application(settings);
		app.run();
	}
}
