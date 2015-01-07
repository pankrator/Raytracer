package com.pankrator.raytracer.geometry;

import com.pankrator.raytracer.Ray;
import com.pankrator.raytracer.Vector;
import com.pankrator.raytracer.shader.Shader;

public class Plane extends Geometry {

	private double y;

	public Plane(double y, Shader shader) {
		super(shader);
		this.y = y;
	}

	
	@Override
	public boolean intersect(Ray ray, IntersectionData result) {		
		if ((ray.getStart().y > y && ray.getDir().y > -1e-9) ||
			(ray.getStart().y < y && ray.getDir().y < 1e-9)) {
			return false;
		}
		
		double yDiff = ray.getDir().y;
		double wantYDiff = ray.getStart().y - this.y;
		double mult = wantYDiff / -yDiff;
		
		if (mult > result.getDistance()) {
			return false;
		}
		
		result.setIntersectionPoint(Vector.add(ray.getStart(), Vector.scale(ray.getDir(), mult)));
		result.setDistance(mult);
		result.setNormal(new Vector(0, 1, 0));
		result.setU(result.getIntersectionPoint().x);
		result.setV(result.getIntersectionPoint().z);
		result.setGeometry(this);
		
		return true;
	}
	
}
