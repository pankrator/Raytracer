package com.pankrator.raytracer.shader;

import java.util.List;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.Ray;
import com.pankrator.raytracer.RayTracer;
import com.pankrator.raytracer.Vector;
import com.pankrator.raytracer.geometry.IntersectionData;
import com.pankrator.raytracer.geometry.PointLight;

public class Phong implements Shader {

	private Color color;
	private double exponent;
	private float strength;

	public Phong(Color color, double exponent, float strength) {
		this.color = color;
		this.exponent = exponent;
		this.strength = strength;
	}

	@Override
	public Color shade(Ray ray, IntersectionData data) {
		Vector N = Vector.faceforward(ray.getDir(), data.getNormal());
		
		Color diffuseColor = this.color;
		
		Color lightContrib = new Color(RayTracer.ambientLight);
		Color specular = new Color();
		
		List<PointLight> lights = RayTracer.scene.getLights();
		for (PointLight light : lights) {
			Vector from = Vector.add(data.getIntersectionPoint(), Vector.scale(N, 1e-6));
			if(RayTracer.isPointVisible(from, light.getPosition())) {
				Vector lightDir = Vector.subtract(light.getPosition(), data.getIntersectionPoint());
				lightDir.normalize();
				
				double cosTheta = Vector.dot(lightDir, N);
				float amount = (float)(light.getPower() /
									   Vector.subtract(data.getIntersectionPoint(), light.getPosition()).lengthSqr());
				Color baseLight = Color.multiply(light.getColor(), amount);
				
				lightContrib.add(Color.multiply(baseLight, (float)cosTheta));
				
				Vector R = Vector.reflect(lightDir.scale(-1), N);
				double cosGamma = Vector.dot(R, Vector.scale(ray.getDir(), -1));
				if (cosGamma > 0) {
					specular.add(Color.multiply(baseLight, (float)(Math.pow(cosGamma, exponent) * strength))); 
				}
			}
		}
		
		return Color.multiply(diffuseColor, lightContrib).add(specular);
	}

}
