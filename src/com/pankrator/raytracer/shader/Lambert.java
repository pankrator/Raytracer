package com.pankrator.raytracer.shader;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.Ray;
import com.pankrator.raytracer.RayTracer;
import com.pankrator.raytracer.Vector;
import com.pankrator.raytracer.geometry.IntersectionData;
import com.pankrator.raytracer.geometry.PointLight;

public class Lambert implements Shader {
	
	private Color color;
	
	public Lambert(Color color) {
		this.color = color;
	}
	
	@Override
	public Color shade(Ray ray, IntersectionData data) {
		Vector N = Vector.faceforward(ray.getDir(), data.getNormal());
		
		Color diffuseColor = this.color;
		Color lightContrib = new Color(RayTracer.ambientLight);
		Vector n = Vector.scale(N, 1e-5);
		
		for(PointLight light : RayTracer.scene.getLights()) {
			Vector from = Vector.add(data.getIntersectionPoint(), n);
			if (RayTracer.isPointVisible(from, light.getPosition())) {
				Vector lightDir = Vector.subtract(light.getPosition(), data.getIntersectionPoint());
				lightDir.normalize();
				
				double cosTheta = Vector.dot(lightDir, N);
				double divideBy = light.getPower() / (Vector.subtract(data.getIntersectionPoint(), light.getPosition()).lengthSqr() * cosTheta);
				lightContrib.add(Color.multiply(light.getColor(), (float)divideBy));
			}
		}
		
		return Color.multiply(diffuseColor, lightContrib);
	}
}
