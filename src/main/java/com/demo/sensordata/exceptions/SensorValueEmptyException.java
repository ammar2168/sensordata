package com.demo.sensordata.exceptions;

public class SensorValueEmptyException extends Exception {

	private static final long serialVersionUID = 1L;
	String errorMessage;
	String componentId;

	public SensorValueEmptyException() {
		this.componentId = "AlertService";
	}

	public SensorValueEmptyException(String errorMessage) {
		this.errorMessage = errorMessage;
		this.componentId = "AlertService";
	}

	public String toString() {

		if (errorMessage != null || !errorMessage.isEmpty())
			return this.errorMessage;
		else
			return "cO2 value must be provided";
	}

}
