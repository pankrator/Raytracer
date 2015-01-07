package com.pankrator.raytracer.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.pankrator.raytracer.network.Scheduler;

public class Server {
	
	private ServerSocket serverSocket;
	private boolean started;
	
	public Server() {
		started = false;
	}
	
	public void startServer() throws IOException {
		serverSocket = new ServerSocket(8095);
		started = true;
	}
	
	public void listen(Scheduler scheduler) throws IOException {
		if (!started) {
			return;
		}
		
		while (true) {
			Socket client = serverSocket.accept();
			ClientThread thread = new ClientThread(client, scheduler);
			thread.start();
		}
	}
	
	public void close() throws IOException {
		this.serverSocket.close();
		this.started = false;
	}
}
