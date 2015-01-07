package com.pankrator.raytracer;


public class Ray {
	
	private Vector start;
	private Vector dir;
	
	private int depth;
	
	public Ray() {}
	
	public Ray(Vector start, Vector dir) {
		this.start = start;
		this.dir = dir;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Vector getStart() {
		return start;
	}

	public Vector getDir() {
		return dir;
	}

	public void setStart(Vector r) {
		this.start = r;
	}
	
	public void setDir(Vector d) {
		this.dir = d;
	}
}
