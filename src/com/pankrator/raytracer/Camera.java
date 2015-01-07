package com.pankrator.raytracer;

import java.awt.Dimension;


public class Camera {
	
	private static final double FOV = 130;
	
	private Vector position;
	private double aspect;
	private Vector upLeft;
	private Vector upRight;
	private Vector downLeft;
	private Dimension viewPlane;
	
	public Camera(Dimension viewPlane) {
		this.position = new Vector(0, 0, 0);
		this.aspect = 4. / 3.0;
		this.viewPlane = viewPlane;
		
		computeUVW();
	}
	
	
	public Ray getScreenRay(double x, double y) {
		Ray result = new Ray();
		
		result.setStart(new Vector(this.position));
		
		Vector target = new Vector(this.upLeft);
		Vector t1 = Vector.subtract(upRight, upLeft).scale(x / viewPlane.width);
		Vector t2 = Vector.subtract(downLeft, upLeft).scale(y / viewPlane.height);
		Vector t3 = Vector.add(t1, t2);
		target.add(t3);
		target.subtract(this.position).normalize();
		result.setDir(target);
		
		return result;
	}
	
	private void computeUVW() {
		double x = -aspect;
		double y = +1;
		
		Vector corner = new Vector(x, y, 1);
		Vector center = new Vector(0, 0, 1);
		
		double lenXY = Vector.subtract(corner, center).length();
		double wantedLength = Math.tan(Math.toRadians(FOV / 2));
		
		double scaling = wantedLength / lenXY;
		
		x *= scaling;
		y *= scaling;
		
		this.upLeft = new Vector(x, y, 1);
		this.upRight = new Vector(-x, y, 1);
		this.downLeft = new Vector(x, -y, 1);
		
		upLeft.add(this.position);
		upRight.add(this.position);
		downLeft.add(this.position);
	}
}
