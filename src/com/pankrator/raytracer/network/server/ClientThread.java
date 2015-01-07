package com.pankrator.raytracer.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.RayTracer;
import com.pankrator.raytracer.network.CommandType;
import com.pankrator.raytracer.network.Scheduler;
import com.pankrator.raytracer.network.Task;

public class ClientThread extends Thread {

	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private Scheduler scheduler;
	
	private Color[][] pixels;
	
	private long startTaskTime = 0;
	private long endTaskTime = 0;
	
	public ClientThread(Socket socket, Scheduler scheduler) {
		this.socket = socket;
		this.scheduler = scheduler;
		pixels = new Color[400][400]; //TODO: Magic
	}
	
	@Override
	public void run() {
		try {
			outputStream = new DataOutputStream((socket.getOutputStream()));
			inputStream = new DataInputStream((socket.getInputStream()));
			
			socket.setReceiveBufferSize(256);
//			socket.setSendBufferSize(256);
			
			while (true) {
				CommandType type = CommandType.getCommand(inputStream.read());
				if (type == null) {
					break;
				}
				switch (type) {
				case CONNECT:
					System.out.println("A new client has just arrived!");
					System.out.println("Sending scene...");
					
					outputStream.write(CommandType.SCENE_TRANSFER.getCode());
					//TODO: Send the scene file
					break;
					
				case COLOR_TRANSFER:
					int x = inputStream.readInt();
					int y = inputStream.readInt();
					
					float r = inputStream.readFloat();
					float g = inputStream.readFloat();
					float b = inputStream.readFloat();
					
					pixels[x][y] = new Color(r, g, b);
					break;
				
				case NEXT_TASK:
				case START_RENDERING:
					Task task = scheduler.getNextTask(0);
					sendNextTask(task);
					outputStream.flush();
					break;
					
				case TASK_END:
					int startX = inputStream.readInt();
					int startY = inputStream.readInt();
					int endX = inputStream.readInt();
					int endY = inputStream.readInt();
					
					endTaskTime = System.currentTimeMillis() - startTaskTime;
					System.out.println("Receiving time: " + endTaskTime);
					
					scheduler.reduceNumberOfWaitingTasks();
					task = scheduler.getNextTask(0);
					boolean isSent = sendNextTask(task);
					if (!isSent) {
						outputStream.write(CommandType.END_CONNECTION.getCode());
					}
					
					RayTracer.fillColors(pixels, startX, startY, endX, endY);
					InetAddress addr = socket.getInetAddress();
					System.out.println("received startX " + startX + " startY " + startY + " endX " + endX + " endY " + endY + " from " + addr.getHostAddress());
					pixels = new Color[400][400]; //TODO: Magic
					startTaskTime = System.currentTimeMillis();
					break;
				case LAST_TASK:
					startX = inputStream.readInt();
					startY = inputStream.readInt();
					endX = inputStream.readInt();
					endY = inputStream.readInt();
					
					scheduler.reduceNumberOfWaitingTasks();
					RayTracer.fillColors(pixels, startX, startY, endX, endY);
//					System.out.println("received startX " + startX + " startY " + startY + " endX " + endX + " endY " + endY);
					
					pixels = new Color[400][400]; //TODO: Magic
//					System.out.println("No more tasks");
					
					while (this.scheduler.getNumberOfWaitingTasks() > 0) {
						System.out.println(this.scheduler.getNumberOfWaitingTasks());
					}
					RayTracer.computeAntialiasing();
					RayTracer.saveRenderedImage();
					
					outputStream.write(CommandType.END_CONNECTION.getCode());
					break;
					
				default:
					break;
				}
			}
			
		} catch (IOException e) {
			
		} finally {
			try {
//				System.out.println("Strange behaviour!!");
				outputStream.close();
				inputStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean sendNextTask(Task task) throws IOException {
//		Task task = scheduler.getNextTask(0);
		if (task == null) {
			return false;
		}
		outputStream.write(CommandType.NEXT_TASK.getCode());
		outputStream.writeInt(task.getStartX());
		outputStream.writeInt(task.getStartY());
		outputStream.writeInt(task.getWidth());
		outputStream.writeInt(task.getHeight());
		
		return true;
	}
}
