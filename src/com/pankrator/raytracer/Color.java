package com.pankrator.raytracer;

public class Color {
	private float r;
	private float g;
	private float b;
	
	public Color() {
		r = 0;
		g = 0;
		b = 0;
	}
	
	public Color(int rgbColor) {
		b = (rgbColor & 0xFF) / 255.0f;
		g = ((rgbColor >> 8) & 0xFF) / 255.0f;
		r = ((rgbColor >> 16) & 0xFF) / 255.0f;
	}
	
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(Color other) {
		this.r = other.r;
		this.g = other.g;
		this.b = other.b;
	}
	
	public Color add(Color a) {
		this.r += a.r;
		this.g += a.g;
		this.b += a.b;
		
		return this;
	}
	public static Color add(Color a, Color b) {
		return new Color(a.r + b.r,
						 a.g + b.g,
						 a.b + b.b);	
	}
	
	public Color subtract(Color other) {
		this.r -= other.r;
		this.g -= other.g;
		this.b -= other.b;
		return this;
	}
	public static Color subtract(Color a, Color b) {
		return new Color(a.r - b.r,
						 a.g - b.g,
						 a.b - b.b);
	}
	
	public Color divide(float divider) {
		r /= divider;
		g /= divider;
		b /= divider;
		return this;
	}
	public static Color divide(Color a, float divider) {
		return new Color(a.r / divider,
						 a.g / divider,
						 a.b / divider);
	}
	
	public Color multiply(float multiplier) {
		r *= multiplier;
		g *= multiplier;
		b *= multiplier;
		return this;
	}
	public Color multiply(Color other) {
		r *= other.r;
		g *= other.g;
		b *= other.b;
		return this;
	}
	public static Color multiply(Color a, Color b) {
		return new Color(a.r * b.r,
						 a.g * b.g,
						 a.b * b.b);
	}
	public static Color multiply(Color a, float m) {
		return new Color(a.r * m,
						 a.g * m,
						 a.b * m);
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		
		Color o = (Color)obj;
		if (o.r == this.r && o.g == this.g && o.b == this.b) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "R: " + this.r + " G: " + this.g + " B: " + this.b;	
	}
}
