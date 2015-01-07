package com.pankrator.raytracer.geometry;

import com.pankrator.raytracer.Ray;
import com.pankrator.raytracer.Vector;
import com.pankrator.raytracer.shader.Shader;

public class Sphere extends Geometry {
	
	private Vector center;
	private double radius;
	
	public Sphere(Vector center, double R, Shader shader) {
		super(shader);
		this.center = center;
		this.radius = R;
	}

	public boolean intersect(Ray ray, IntersectionData result) {
		Vector H = Vector.subtract(ray.getStart(), this.center);
		
		double A = ray.getDir().lengthSqr();
		double B = 2 * Vector.dot(H, ray.getDir());
		double C = H.lengthSqr() - radius * radius;
		double Dscr = B * B - 4 * A * C;
		
		if (Dscr < 0) {
			return false;
		}
		double x1, x2;
		x1 = (-B + Math.sqrt(Dscr)) / (2 * A);
		x2 = (-B - Math.sqrt(Dscr)) / (2 * A);
		double solution = x2;
		if (solution < 0) {
			solution = x1; 
		}
		if (solution < 0) {
			return false; // The whole Sphere is behind us
		}
		
		if (solution > result.getDistance()) {
			return false;
		}
		
		result.setDistance(solution);
		result.setIntersectionPoint(Vector.add(ray.getStart(), Vector.scale(ray.getDir(), solution)));
		Vector normal = new Vector(result.getIntersectionPoint());
		normal.subtract(this.center);
		normal.normalize();
		result.setNormal(normal);		
		result.setU((Math.PI + Math.atan2(result.getIntersectionPoint().z - center.z,
									 	 result.getIntersectionPoint().x - center.x)) / (2 * Math.PI));
		result.setV(1.0 - (Math.PI / 2 + Math.asin((result.getIntersectionPoint().y - center.y) / radius)) / Math.PI);
		result.setGeometry(this);
		
		return true;
	}

}
