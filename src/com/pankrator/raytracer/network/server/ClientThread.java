package com.pankrator.raytracer.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.pankrator.raytracer.Color;
import com.pankrator.raytracer.network.CommandType;
import com.pankrator.raytracer.network.Scheduler;
import com.pankrator.raytracer.network.Task;
import com.pankrator.raytracer.network.TaskResult;
import com.pankrator.raytracer.network.action.Action;
import com.pankrator.raytracer.network.action.impl.server.ColorTransferAction;
import com.pankrator.raytracer.network.action.impl.server.EndTaskAction;
import com.pankrator.raytracer.network.action.impl.server.FileSceneTransfer;
import com.pankrator.raytracer.network.action.impl.server.LastTaskAction;
import com.pankrator.raytracer.network.action.impl.server.NextTask;

public class ClientThread extends Thread {

	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private Scheduler scheduler;
	
	private TaskResult taskResult;

	private long startTaskTime = 0;
	private long endTaskTime = 0;

	public ClientThread(Socket socket, Scheduler scheduler) {
		this.socket = socket;
		this.scheduler = scheduler;
		taskResult = new TaskResult();
	}

	@Override
	public void run() {
		Action nextTask = null,
				colorTransferAction = null,
				sceneTransfer = null,
				endTaskAction = null, 
				lastTaskAction = null;
		try {
			outputStream = new DataOutputStream((socket.getOutputStream()));
			inputStream = new DataInputStream((socket.getInputStream()));

			colorTransferAction = new ColorTransferAction(inputStream, taskResult);
			sceneTransfer = new FileSceneTransfer();
			nextTask = new NextTask(scheduler, outputStream);
			endTaskAction = new EndTaskAction(scheduler, inputStream,
					outputStream, taskResult, socket);
			lastTaskAction = new LastTaskAction(outputStream, inputStream,
					scheduler, taskResult);

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
					// TODO: Send the scene file
					break;

				case COLOR_TRANSFER:
					colorTransferAction.doAction();
					break;

				case NEXT_TASK:
				case START_RENDERING:
					nextTask.doAction();
					break;

				case TASK_END:
					endTaskAction.doAction();
					break;
				case LAST_TASK:
					lastTaskAction.doAction();
					break;

				default:
					break;
				}
			}

		} catch (IOException e) {
			// TODO
			if (nextTask != null) {
				Task notFinishedTask = ((NextTask)endTaskAction).getLastSentTask();
				scheduler.addNotFinishedTask(notFinishedTask);				
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 
		} finally {
			try {
				outputStream.close();
				inputStream.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
