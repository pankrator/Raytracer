package com.pankrator.raytracer.geometry;

import com.pankrator.raytracer.Vector;

public class IntersectionData {
	
	private Vector intersectionPoint;
	private Vector normal;
	private double distance;
	private Geometry geometry;
	private double u;
	private double v;
	
	public IntersectionData() {
		
	}

	public double getU() {
		return u;
	}

	public void setU(double u) {
		this.u = u;
	}

	public double getV() {
		return v;
	}

	public void setV(double v) {
		this.v = v;
	}

	public Vector getIntersectionPoint() {
		return intersectionPoint;
	}

	public void setIntersectionPoint(Vector intersectionPoint) {
		this.intersectionPoint = intersectionPoint;
	}

	public Vector getNormal() {
		return normal;
	}

	public void setNormal(Vector normal) {
		this.normal = normal;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}
