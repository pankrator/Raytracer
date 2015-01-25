package com.pankrator.raytracer.network.action.impl.server;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.network.TaskResult;
import com.pankrator.raytracer.network.action.Action;

public class ColorTransferAction implements Action {

	private DataInputStream inputStream;
	private TaskResult taskResult;
	
	public ColorTransferAction(DataInputStream is, TaskResult taskResult) {
		this.inputStream = is;
		this.taskResult = taskResult;
	}
	
	@Override
	public boolean doAction() throws Exception {
		int x = inputStream.readInt();
		int y = inputStream.readInt();

		float r = inputStream.readFloat();
		float g = inputStream.readFloat();
		float b = inputStream.readFloat();
		
		Color[][] pixels = taskResult.getPixels();
		pixels[x][y] = new Color(r, g, b);
		
		return true;
	}
}
