package com.pankrator.raytracer.geometry;

import com.pankrator.raytracer.Ray;

public interface Intersectable {
	public boolean intersect(Ray ray, IntersectionData data);
}
