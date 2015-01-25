package com.pankrator.raytracer.network;

import com.pankrator.raytracer.Color;

public class TaskResult {
	
	private Color[][] pixels;
	
	public TaskResult() {
		pixels = new Color[Scheduler.TASK_WIDTH][Scheduler.TASK_HEIGHT];
	}

	public Color[][] getPixels() {
		return pixels;
	}

	public void setPixels(Color[][] pixels) {
		this.pixels = pixels;
	}
	
	
}
