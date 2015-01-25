package com.pankrator.raytracer.network.action.impl.server;

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
import com.pankrator.raytracer.network.TaskResult;
import com.pankrator.raytracer.network.action.Action;

public class EndTaskAction extends NextTask implements Action {

	private DataInputStream inputStream;
	private TaskResult taskResult;
	private Socket socket;
	
	public EndTaskAction(Scheduler s, DataInputStream is, DataOutputStream os,
			TaskResult taskResult, Socket socket) {
		super(s, os);
		this.inputStream = is;
		this.taskResult = taskResult;
		this.socket = socket;
	}
	
	@Override
	public boolean doAction() throws IOException {
		int startX = inputStream.readInt();
		int startY = inputStream.readInt();
		int endX = inputStream.readInt();
		int endY = inputStream.readInt();
		
		scheduler.reduceNumberOfWaitingTasks();
		boolean isSent = super.doAction();
		if (!isSent) {
			outputStream.write(CommandType.END_CONNECTION.getCode());
		}
		
		
		RayTracer.fillColors(taskResult.getPixels(), startX, startY, endX, endY);
		InetAddress addr = socket.getInetAddress();
		System.out.println("received startX " + startX + " startY " + startY + " endX " + endX + " endY " + endY + " from " + addr.getHostAddress());
		
		taskResult.setPixels(new Color[Scheduler.TASK_WIDTH][Scheduler.TASK_HEIGHT]);
		
		return true;
	}
}
