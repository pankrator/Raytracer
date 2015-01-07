package com.pankrator.raytracer;

import java.io.IOException;

import com.pankrator.raytracer.network.Scheduler;
import com.pankrator.raytracer.network.client.Client;
import com.pankrator.raytracer.network.server.Server;

public class Main {

	private static Server server;
	private static Client client;
	
	public static void main(String[] args) throws IOException { //TODO Catch IOException		
//		RayTracer.generateDefaultScene();
//		Color result[][] = RayTracer.render(0, 0, RayTracer.screenSize.width, RayTracer.screenSize.height);
//		RayTracer.fillColors(result, 0, 0, RayTracer.screenSize.width, RayTracer.screenSize.height);
//		RayTracer.computeAntialiasing();
//		RayTracer.saveRenderedImage();
		
		
		String type = "server";
		String scenePath = "";
		if (args.length > 0) {
			type = args[0];
			scenePath = null;
			if (args.length > 1) {
				scenePath = args[1];
			}			
		}
		
		switch(type) {
		case "server":
			Scheduler s = new Scheduler(RayTracer.screenSize);
			server = new Server();
			server.startServer();
			server.listen(s);
			break;
			
		case "client":
			client = new Client();
			client.connect(scenePath, 8095);
			client.start();
			break;
		}
	}

}
