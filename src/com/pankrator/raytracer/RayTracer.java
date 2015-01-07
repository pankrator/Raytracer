package com.pankrator.raytracer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import com.pankrator.raytracer.geometry.Geometry;
import com.pankrator.raytracer.geometry.IntersectionData;
import com.pankrator.raytracer.geometry.Plane;
import com.pankrator.raytracer.geometry.PointLight;
import com.pankrator.raytracer.geometry.Sphere;
import com.pankrator.raytracer.shader.Lambert;
import com.pankrator.raytracer.shader.Phong;

public class RayTracer {
	
	public static Color ambientLight = new Color(0.5f, 0.5f, 0.5f);
	
	public static Dimension screenSize = new Dimension(1920, 1080);
	public static Color pixels[][] = new Color[screenSize.width][screenSize.height];
	private static Camera camera = new Camera(screenSize);
	private static BufferedImage image = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_RGB);
	
	public static Scene scene = new Scene();
	
	public RayTracer() {
	}
	
	public static void saveRenderedImage() throws IOException {
		File fImage = new File("rendered.png");
		ImageIO.write(image, "png", fImage);
		System.out.println("Image saved");
	}

	public static Color[][] render(int startX, int startY, int endX, int endY) {
		Color pixels[][] = new Color[endX - startX + 1][endY - startY + 1];
		int indexX = 0;
		int indexY = 0;
		long t1 = System.currentTimeMillis();

		for (int y = startY; y < endY; y++) {
			for (int x = startX; x < endX; x++) {
				Color color = renderSample(x, y);
				pixels[indexX][indexY] = color;
				indexX++;
			}
			indexX = 0;
			indexY++;
		}
		
		long t2 = System.currentTimeMillis();
		long time = t2 - t1;
//		System.out.println("Total time: " + time / 1000);
		
		return pixels;
	}
	
	public static void computeAntialiasing() throws FileNotFoundException {
		long counter = 0; 
		System.out.println("Antialiasing called");
		File res = new File("pixels.txt");
		PrintWriter writer = new PrintWriter(res);
		for (int y = 0; y < screenSize.height; y++) {
			for (int x = 0; x < screenSize.width; x++) {
				Color color = pixels[x][y];
//				writer.write(x + " " + y + " col: " + color);
//				writer.write("\n");
////				System.out.println(x + " " + y + " col: " + color);
//				Color neighs[] = new Color[5];
//				neighs[0] = pixels[x][y];
//				
//				neighs[1] = pixels[x > 0 ? x - 1 : x][y];
//				neighs[2] = pixels[x + 1 < screenSize.width ? x + 1 : x][y];
//				
//				neighs[3] = pixels[x][y > 0 ? y - 1 : y];
//				neighs[4] = pixels[x][y + 1 < screenSize.height ? y + 1 : y];
//				
//				Color average = new Color();
//				for (int i = 0; i < 5; i++) {
//					average.add(neighs[i]);
//				}
//				average.divide(5.0f);
//				
//				for(int i = 0; i < 5; i++) {
//					if (tooDifferent(neighs[i], average)) {
//						counter++;
//						accumulateAntialising(x, y, color);
//						break;
//					}
//				}
//				if (color == null) {
//					System.out.println("Bad error! x: " + x + " y: " + y);
//				}
				clampColor(color);
				image.setRGB(x, y, new java.awt.Color((float) color.getR(), (float) color.getG(), (float) color.getB()).getRGB());
			}
		}
		writer.close();
		System.out.println("called " + counter);
	}
	
	private static Color renderSample(double x, double y) {
		Ray ray = camera.getScreenRay(x, y);
		Color color = new Color();
		IntersectionData data = new IntersectionData();
		data.setDistance(1e99);
		Geometry closestNode = null;
		
		for (Geometry node : scene.getObjects()) {
			if (node.intersect(ray, data)) {
				closestNode = data.getGeometry();
			}
		}

		if (closestNode == null) {
			color = new Color();
		} else {
			color = closestNode.getShader().shade(ray, data);
		}
		
		return color;
	}
	
	private static void accumulateAntialising(int x, int y, Color color) {
		double kernel[][] = new double[][] {
				{0, 0},
				{0.3, 0.3},
				{0.6, 0},
				{0, 0.6},
				{0.6, 0.6},
		};
		for(int samples = 1; samples < 5; samples++) {
			color.add(renderSample(x + kernel[samples][0], y + kernel[samples][1]));
		}
		color.divide(5.0f);
	}
	
	private static boolean tooDifferent(Color a, Color b) {
		final float THRESHOLD = 0.1f;
		
		float theMax = Math.max(a.getR(), b.getR());
		float theMin = Math.min(a.getR(), b.getR());
		if (theMax - theMin > THRESHOLD && theMin < 1.33f) {
			return true;
		}
		
		theMax = Math.max(a.getG(), b.getG());
		theMin = Math.min(a.getG(), b.getG());
		if (theMax - theMin > THRESHOLD && theMin < 1.33f) {
			return true;
		}
		
		theMax = Math.max(a.getB(), b.getB());
		theMin = Math.min(a.getB(), b.getB());
		if (theMax - theMin > THRESHOLD && theMin < 1.33f) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isPointVisible(Vector from, Vector to) {
		Ray ray = new Ray();
		ray.setStart(from);
		Vector dir = Vector.subtract(to, from);
		dir.normalize();
		ray.setDir(dir);
		
		IntersectionData data = new IntersectionData();
		data.setDistance(Vector.subtract(to, from).length());
		
		for (Geometry node: scene.getObjects()) {
			if(node.intersect(ray, data)) {
				return false;
			}
		}
		
		return true;
	}

	public static void generateDefaultScene() {
		scene.addLight(new PointLight(new Vector(-70, 230, 30), new Color(1, 1, 1), 50000));
//		scene.addLight(new PointLight(new Vector(70, 230, 30), new Color(0, 1f, 0), 50000));
//		scene.addLight(new PointLight(new Vector(-50, 40, 100), new Color(1, 0, 0), 30000));
		
//		scene.addObject(new Sphere(
//				new Vector(-120, 140, 200),
//				40,
//				new Phong(new Color(0.5f, 0.2f, 0.009f), 3, 0.9f)
//	));
		scene.addObject(new Sphere(
				new Vector(0, 100, 160),
				40,
				new Phong(new Color(0.5f, 0.2f, 0.009f), 3, 0.9f)
	));
		scene.addObject(new Sphere(
							new Vector(-80, 100, 100),
							20,
							new Phong(new Color(0.23f, 0.67f, 0.21f), 3, 0.9f)
						));
		scene.addObject(new Sphere(
							new Vector(-80, -30, 100),
							60,
							new Phong(new Color(1, 0, 0), 3, 0.9f)
				));
		scene.addObject(new Sphere(
							new Vector(70, 50, 100),
							30,
							new Phong(new Color(0, 0.3f, 0.7f), 2, 0.4f)
//							new Lambert(new Color(0, 0.3f, 0.7f))
				));
		scene.addObject(new Plane(
				-40,
				new Lambert(new Color(0f, 1f, 1f))
				));
	}
	
	private static void clampColor(Color c) {
		if(c.getR() > 1) {
			c.setR(1f);
		}
		if(c.getR() < 0) {
			c.setR(0f);
		}
		if(c.getG() > 1) {
			c.setG(1f);
		}
		if(c.getG() < 0) {
			c.setG(0f);
		}
		if(c.getB() > 1) {
			c.setB(1f);
		}
		if(c.getB() < 0) {
			c.setB(0f);
		}
	}

	public static void fillColors(Color[][] pixels2, int startX, int startY,
			int endX, int endY) {
		int indexX = 0, indexY = 0;
//		System.out.println("received startX " + startX + " startY " + startY + " endX " + endX + " endY " + endY + " from " + addr.getHostAddress());
		for (int y = startY; y < endY; y++) {
			for (int x = startX; x < endX; x++) {
				if (pixels[x][y] != null) {
					System.out.println("Big error");
					System.exit(1);
				}
				pixels[x][y] = new Color(pixels2[indexX][indexY]);
				indexX++;
			}
			indexX = 0;
			indexY++;
		}
	}
}
 