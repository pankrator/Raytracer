package com.pankrator.raytracer.network.client;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.Ray;
import com.pankrator.raytracer.RayTracer;
import com.pankrator.raytracer.network.CommandType;

public class Client {

	private Socket socket;
	private boolean connected;

	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	public Client() {
		connected = false;
	}

	public boolean connect(String address, int port) throws UnknownHostException, IOException {
		socket = new Socket(address, port);
		connected = true;
		return connected;
	}

	public void start() throws IOException {
		RayTracer.generateDefaultScene();
		outputStream = new DataOutputStream((socket.getOutputStream()));
		inputStream = new DataInputStream((socket.getInputStream()));
		
//		socket.setReceiveBufferSize(256);
		socket.setSendBufferSize(256);

		outputStream.write(CommandType.CONNECT.getCode());

		while (true) {
			int code = inputStream.read();
			CommandType command = CommandType.getCommand(code);
			if (command == null) {
				System.out.println("Code is " + code + "Command is null! Terminating program.");
				return;
			}
			switch (command) {
			case SCENE_TRANSFER:
				//TODO: Do something with scene file
				System.out.println("Scene is loaded.");
				
				outputStream.write(CommandType.START_RENDERING.getCode());
				break;
				
			case NEXT_TASK:
				System.out.println("New task arrived");
				
				int startX = inputStream.readInt();
				int startY = inputStream.readInt();
				int width = inputStream.readInt();
				int height = inputStream.readInt();
				
				System.out.println("startX " + startX + " endX " + (startX + width) + " startY " + startY + " endY " + (startY + height));
				
				long startRenderTime = System.currentTimeMillis();
				Color result[][] = RayTracer.render(startX, startY, startX + width, startY + height);
				long endTime = System.currentTimeMillis() - startRenderTime;
				System.out.println("Rendering time: " + endTime);
				
				long startSendingTime = System.currentTimeMillis();
				this.transferColors(result, width, height);
				long endSendingTime = System.currentTimeMillis() - startSendingTime;
				System.out.println("Sending time: " + endSendingTime);
				
				if (startX + width >= RayTracer.screenSize.width && startY + height >= RayTracer.screenSize.height) {
					outputStream.write(CommandType.LAST_TASK.getCode());
				} else {
					outputStream.write(CommandType.TASK_END.getCode());					
				}
				outputStream.writeInt(startX);
				outputStream.writeInt(startY);
				outputStream.writeInt(startX + width);
				outputStream.writeInt(startY + height);
				
				System.out.println("Colors are sent");
				break;
				
			case END_CONNECTION:
				System.out.println("No more tasks for me. Close connection");
				socket.close();
				return;
//				outputStream.write(CommandType.END_CONNECTION.getCode());
//				break;
			default:
				break;
			}
		}
	}

	private void transferColors(Color[][] result, int width, int height) throws IOException {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color c = result[x][y];
				outputStream.write(CommandType.COLOR_TRANSFER.getCode());
				outputStream.writeInt(x);
				outputStream.writeInt(y);
				
				outputStream.writeFloat(c.getR());
				outputStream.writeFloat(c.getG());
				outputStream.writeFloat(c.getB());
			}
		}
	}
}
