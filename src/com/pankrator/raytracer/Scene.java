package com.pankrator.raytracer;

import java.util.ArrayList;
import java.util.List;

import com.pankrator.raytracer.geometry.Geometry;
import com.pankrator.raytracer.geometry.PointLight;

public class Scene {

	private List<Geometry> objects;
	private List<PointLight> lights;
	
	public Scene() {
		objects = new ArrayList<>();
		lights = new ArrayList<>();
	}
	
	public void addLight(PointLight light) {
		this.lights.add(light);
	}
	
	public void addObject(Geometry node) {
		this.objects.add(node);
	}
	
	public List<Geometry> getObjects() {
		return objects;
	}
	
	public List<PointLight> getLights() {
		return lights;
	}
}
