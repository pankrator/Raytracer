package com.pankrator.raytracer.shader;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.Ray;
import com.pankrator.raytracer.geometry.IntersectionData;

public interface Shader {
	public Color shade(Ray ray, IntersectionData data);
}
