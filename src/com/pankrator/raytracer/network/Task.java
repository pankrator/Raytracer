package com.pankrator.raytracer.network;

public class Task {
	
	private int startX;
	private int startY;
	private int width;
	private int height;
	
	private long startTime;
	
	public Task(int x, int y, int width, int height) {
		this.startX = x;
		this.startY = y;
		this.width = width;
		this.height = height;
		this.startTime = System.currentTimeMillis();
	}
	
	@Override
	public boolean equals(Object obj) {
		Task o = (Task)obj;
		if (this == o) {
			return true;
		}
		
		if (this.startX == o.startX && 
			this.startY == o.startY &&
			this.width == o.width && 
			this.height == o.height) {
			
			return true;
		}
		
		return false;
	}
	
	

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
