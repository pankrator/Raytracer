package com.pankrator.raytracer.network.action.impl.server;

import java.io.DataOutputStream;
import java.io.IOException;

import com.pankrator.raytracer.network.CommandType;
import com.pankrator.raytracer.network.Scheduler;
import com.pankrator.raytracer.network.Task;
import com.pankrator.raytracer.network.action.Action;

public class NextTask implements Action {
	
	protected Scheduler scheduler;
	protected DataOutputStream outputStream;
	private Task lastSentTask;
	
	public NextTask(Scheduler s, DataOutputStream os) {
		this.scheduler = s;
		this.outputStream = os;
	}
	
	@Override
	public boolean doAction() throws IOException {
		Task task = scheduler.getNextTask(0);
		System.out.println("Sending task");		
		if (task == null) {
			return false;
		}
		
		task.setStartTime(System.currentTimeMillis());
		lastSentTask = task;
		
		outputStream.write(CommandType.NEXT_TASK.getCode());
		outputStream.writeInt(task.getStartX());
		outputStream.writeInt(task.getStartY());
		outputStream.writeInt(task.getWidth());
		outputStream.writeInt(task.getHeight());
		
		if (scheduler.getNumberOfTasks() <= 0) {
			outputStream.write(CommandType.LAST_TASK.getCode());		
		} else {
			outputStream.write(0b1111);						
		}
		
		
		return true;
	}
	
	public Task getLastSentTask() {
		return this.lastSentTask;
	}
}
