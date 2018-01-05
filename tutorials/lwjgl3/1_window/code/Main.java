package com.tutorial;

public class Main {

	public static void main(String[] args) {
		Display display = new Display("My Engine", 1280, 720);
		while(!display.shouldClose()) {
			display.clear();

			display.update();
		}
	}
}
