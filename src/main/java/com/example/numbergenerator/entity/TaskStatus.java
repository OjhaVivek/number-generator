package com.example.numbergenerator.entity;

public enum TaskStatus {
	
	IN_PROGRESS("IN_PROGRESS"),ERROR("ERROR"),SUCCESS("SUCCESS");
	
	private String value;
	
	private TaskStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
