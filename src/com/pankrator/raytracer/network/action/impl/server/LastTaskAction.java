package com.pankrator.raytracer.network.action.impl.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.RayTracer;
import com.pankrator.raytracer.network.CommandType;
import com.pankrator.raytracer.network.Scheduler;
import com.pankrator.raytracer.network.TaskResult;
import com.pankrator.raytracer.network.action.Action;

public class LastTaskAction implements Action {
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Scheduler scheduler;
	private TaskResult taskResult;
	
	public LastTaskAction(DataOutputStream os, DataInputStream is, Scheduler s, TaskResult taskResult) {
		this.inputStream = is;
		this.outputStream = os;
		this.scheduler = s;
		this.taskResult = taskResult;
	}
	
	@Override
	public boolean doAction() throws Exception {
		int startX = inputStream.readInt();
		int startY = inputStream.readInt();
		int endX = inputStream.readInt();
		int endY = inputStream.readInt();

		scheduler.reduceNumberOfWaitingTasks();
		RayTracer.fillColors(taskResult.getPixels(), startX, startY, endX, endY);

		this.taskResult.setPixels(new Color[Scheduler.TASK_WIDTH][Scheduler.TASK_HEIGHT]);

		while (this.scheduler.getNumberOfWaitingTasks() > 0) {
			System.out.println(this.scheduler
					.getNumberOfWaitingTasks());
		}
		RayTracer.computeAntialiasing();
		RayTracer.saveRenderedImage();

		outputStream.write(CommandType.END_CONNECTION.getCode());
		
		return true;
	}
}
