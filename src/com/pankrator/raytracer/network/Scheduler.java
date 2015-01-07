package com.pankrator.raytracer.network;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
	
	private static final int TASK_WIDTH = 120;
	private static final int TASK_HEIGHT = 120;
	private static final long MAXIMUM_TASK_TIME = 1000 * 20;
	
	private int currentX;
	private int currentY;
	private int numberOfWaitingTasks;
	
	private Dimension dimensions;
	
	private List<Task> tasks;

	public Scheduler(Dimension dim) {
		this.currentX = 0;
		this.currentY = 0;
		this.dimensions = dim;
		this.numberOfWaitingTasks = 0;
		this.tasks = new ArrayList<>();
		generateAllTasks();
	}
	
	public synchronized Task getNextTask(int numberOfCores) {
		if (this.tasks.size() < 1) {
			return null;
		}
		this.numberOfWaitingTasks++;
		return this.tasks.remove(0);
	}
	
	private Task getNextTask() {
		int reduceX = (dimensions.width - currentX < TASK_WIDTH ? TASK_WIDTH - (dimensions.width - currentX) : 0);
		int reduceY = (dimensions.height - currentY < TASK_HEIGHT ? TASK_HEIGHT - (dimensions.height - currentY) : 0);
		Task task = new Task(currentX, currentY, TASK_WIDTH - reduceX, TASK_HEIGHT - reduceY);
		
		if (currentY >= dimensions.height) {
			return null;
		}
		
		if (currentX + TASK_WIDTH >= dimensions.width) {
			currentX = 0;
			if (currentY >= dimensions.height) {
				return null;
			}
			currentY += TASK_HEIGHT;
		} else {
			currentX += TASK_WIDTH;
		}
//		numberOfWaitingTasks++;
		
//		this.tasks.add(task);
		
		return task;
	}
	
	private void generateAllTasks() {
		Task task = null;
		while((task = getNextTask()) != null) {
			this.tasks.add(task);
		}
	}
	
	public synchronized Task getAnyFreeTask() {
		for (Task t : tasks) {
			if (System.currentTimeMillis() - t.getStartTime() > MAXIMUM_TASK_TIME) {
				return t;
			}
		}
		
		return null;
	}
	
	public synchronized void removeTask(Task task) {
		this.tasks.remove(task);
		this.numberOfWaitingTasks--;
	}
	
	public void reduceNumberOfWaitingTasks() {
		this.numberOfWaitingTasks--;
	}
	
	public int getNumberOfWaitingTasks() {
		return this.numberOfWaitingTasks;
	}
}
