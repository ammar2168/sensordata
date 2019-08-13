package com.demo.sensordata.exceptions;

public class EmptyOrInvalidTimeFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	String errorMessage;
	String componentId;

	public EmptyOrInvalidTimeFormatException() {
		this.componentId = "AlertService";
	}

	public EmptyOrInvalidTimeFormatException(String errorMessage) {
		this.errorMessage = errorMessage;
		this.componentId = "AlertService";
	}

	public String toString() {

		if (errorMessage != null || !errorMessage.isEmpty())
			return this.errorMessage;
		else
			return "Time cannot be empty or in wrong format";
	}

	public String getComponentId() {
		return componentId;
	}

}
