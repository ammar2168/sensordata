package com.demo.sensordata.exceptions;

public class InvalidUUIDException extends Exception {

	private static final long serialVersionUID = 1L;
	String errorMessage;
	String componentId;

	public InvalidUUIDException() {
		this.componentId = "AlertService";
	}

	public InvalidUUIDException(String errorMessage) {
		this.errorMessage = errorMessage;
		this.componentId = "AlertService";
	}

	public String toString() {

		if (errorMessage != null || !errorMessage.isEmpty())
			return this.errorMessage;
		else
			return "Invalid UUID";
	}

	public String getComponentId() {
		return componentId;
	}

}
