package com.pankrator.raytracer.geometry;

import com.pankrator.raytracer.shader.Shader;

public abstract class Geometry implements Intersectable {
	
	private Shader shader;
	
	public Geometry(Shader shader) {
		this.shader = shader;
	}

	public Shader getShader() {
		return shader;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
}
