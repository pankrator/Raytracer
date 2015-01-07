package com.pankrator.raytracer.geometry;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.Vector;

public class PointLight {
	
	private Vector position;
	private Color color;
	private int power;
	
	public PointLight(Vector position, Color color, int power) {
		this.position = position;
		this.color = color;
		this.power = power;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
}
