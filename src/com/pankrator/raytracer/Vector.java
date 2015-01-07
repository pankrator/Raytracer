package com.pankrator.raytracer;

public class Vector {
	
	public double x;
	public double y;
	public double z;
	
	public Vector() {
		x = y = z = 0;
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vector o) {
		this(o.x, o.y, o.z);
	}
	
	void makeZero() {
		this.x = y = z = 0;
	}
	
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public double lengthSqr() {
		return x * x + y * y + z * z;
	}
	
	public Vector scale(double multiplayer) {
		x *= multiplayer;
		y *= multiplayer;
		z *= multiplayer;
		return this;
	}
	public static Vector scale(Vector a, double multiplayer) {
		Vector v = new Vector(a.x * multiplayer,
							  a.y * multiplayer, 
							  a.z * multiplayer);
		return v;
	}
	
	public void add(Vector o) {
		this.x += o.x;
		this.y += o.y;
		this.z += o.z;
	}
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	public static Vector add(Vector a, Vector b) {
		return new Vector(
					a.x + b.x,
					a.y + b.y,
					a.z + b.z
				);
	}
	
	public Vector subtract(Vector o) {
		this.x -= o.x;
		this.y -= o.y;
		this.z -= o.z;
		return this;
	}
	public void subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}
	public static Vector subtract(Vector a, Vector b) {
		return new Vector(
					a.x - b.x,
					a.y - b.y,
					a.z - b.z
				);
	}
	
	public void normalize() {
		double multiplayer = 1.0 / this.length();
		this.scale(multiplayer);
	}
	
	public double dot(Vector b) {
		return x * b.x + y * b.y  + z * b.z;
	}
	public static double dot(Vector a, Vector b) {
		return a.x * b.x + a.y * b.y  + a.z * b.z;
	}
	
	public Vector crossProduct(Vector b) {
		return new Vector(
					y * b.z - z * b.y,
					z * b.x - x * b.z,
					x * b.y - y * b.x
				);
	}
	
	public static Vector faceforward(Vector ray, Vector norm) {
		if (dot(ray, norm) < 0) {
			return new Vector(norm);
		}
		return Vector.scale(norm, -1);
	}
	
	public static Vector reflect(Vector ray, Vector norm) {
		Vector t = scale(norm, 2 * dot(ray, norm));
		Vector result = subtract(ray, t);
		result.normalize();
		return result;
	}
}
