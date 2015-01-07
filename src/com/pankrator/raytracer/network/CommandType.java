package com.pankrator.raytracer.network;

import java.util.HashMap;
import java.util.Map;

public enum CommandType {
	CONNECT(0b00),
	NEXT_TASK(0b01),
	COLOR_TRANSFER(0b10),
	END_CONNECTION(0b11),
	START_RENDERING(0b101),
	SCENE_TRANSFER(0b110),
	TASK_END(0b111),
	LAST_TASK(0b1000);
	
	private int code;
	
	private static Map<Integer, CommandType> mapping;
	
	private CommandType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
	
	private static void initializeMapping() {
		mapping = new HashMap<>();
		CommandType[] types = CommandType.values();
		for(CommandType type : types) {
			mapping.put(type.code, type);
		}
	}
	
	public static CommandType getCommand(int code) {
		if (mapping == null) {
			initializeMapping();
		}
		
		return mapping.get(code);
	}}
