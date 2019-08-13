package com.demo.sensordata.model;

import javax.validation.constraints.NotEmpty;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class SensorInput implements Serializable {

	public SensorInput(Integer co2, String time) {
		this.co2 = co2;
		this.time = time;
	}

	@NotEmpty
	private Integer co2;

	private String time;

	public Integer getCo2() {
		return co2;
	}

	public void setCo2(int co2) {
		this.co2 = co2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
