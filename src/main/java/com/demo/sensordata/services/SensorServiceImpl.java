package com.demo.sensordata.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.sensordata.entities.Sensor;
import com.demo.sensordata.entities.Sensor.SensorStatus;
import com.demo.sensordata.entities.SensorAlerts;
import com.demo.sensordata.entities.SensorMeasurement;
import com.demo.sensordata.repository.AlertRepository;
import com.demo.sensordata.repository.MeasurementRepository;
import com.demo.sensordata.repository.SensorRepository;
import com.demo.sensordata.utils.DateUtil;
import com.demo.sensordata.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SensorServiceImpl implements SensorService {

	public static int LIMIT = 2000;

	@Autowired
	SensorRepository sensorRepository;

	@Autowired
	MeasurementRepository measurementRepository;

	@Autowired
	AlertRepository alertRepository;

	@Override
	public Sensor createOrUpdateSensor(String uuid, int currentLevel, String createdDate)
			throws JsonProcessingException {
		Sensor sensor = getSensor(uuid);
		if (sensor == null) {
			sensor = new Sensor();
			sensor.setUuid(uuid);
			if (currentLevel > 2000) {
				sensor.setCurrentStatus(Sensor.SensorStatus.WARN);
			}

		} else {
			evaluateSensorStatus(sensor, currentLevel, createdDate);
		}

		sensor.setLastUpdatedAt(DateUtil.convertToDate(createdDate));
		sensorRepository.save(sensor);
		return sensor;
	}

	private Sensor evaluateSensorStatus(Sensor sensor, int currentLevel, String createdDate)
			throws JsonProcessingException {

		List<Object[]> lastTwoLevels = measurementRepository.getLastTwoLevels(sensor.getId());
		int levelAboveLimitCount = 0;
		int levelBelowLimitCount = 0;

		for (int i = lastTwoLevels.size() - 1; i >= 0; i--) {
			if (Integer.parseInt(lastTwoLevels.get(i)[0].toString()) > LIMIT) {
				levelAboveLimitCount++;
			} else {
				levelBelowLimitCount++;
			}
		}

		if (currentLevel > LIMIT)
			levelAboveLimitCount++;
		else if (currentLevel <= LIMIT)
			levelBelowLimitCount++;

		if (levelAboveLimitCount == 3) {
			sensor.setLastUpdatedAt(DateUtil.convertToDate(createdDate));
			sensor.setCurrentStatus(Sensor.SensorStatus.ALERT);
			createAlert(sensor, lastTwoLevels, new Integer(currentLevel));

		} else if (levelBelowLimitCount == 3) {
			sensor.setCurrentStatus(Sensor.SensorStatus.OK);
		}

		else if (sensor.getCurrentStatus() == SensorStatus.OK && currentLevel > LIMIT) {
			sensor.setCurrentStatus(Sensor.SensorStatus.WARN);
		}

		return sensor;
	}

	@Override
	public Sensor getSensor(String uuid) {
		return sensorRepository.getSensorByUUID(uuid);
	}

	@Override
	public SensorMeasurement createMeasurement(String uuid, int level, String dateStr) throws JsonProcessingException {
		Sensor sensor = createOrUpdateSensor(uuid, level, dateStr);
		SensorMeasurement sensorMeasurement = new SensorMeasurement();
		sensorMeasurement.setLevel(level);
		sensorMeasurement.setSensor(sensor);
		sensorMeasurement.setCreatedAt(DateUtil.convertToDate(dateStr));
		measurementRepository.save(sensorMeasurement);
		return sensorMeasurement;
	}

	@Override
	public Map<String, String> getMetrics(Sensor sensor) {
		List<Object[]> results = measurementRepository.getAvgAndMax(sensor.getId());

		HashMap<String, String> map = new HashMap<>();
		map.put("maxLast30Days", results.get(0)[1].toString());
		map.put("avgLast30Days", results.get(0)[0].toString());
		return map;
	}

	@Override
	public List<String> getAlerts(Sensor sensor) {
		List<String> alerts = alertRepository.getAlerts(sensor.getId());
		return alerts;
	}

	@Override
	public void createAlert(Sensor sensor, List<Object[]> lastTwoLevels, Integer level) throws JsonProcessingException {
		SensorAlerts sensorAlerts = new SensorAlerts();
		sensorAlerts.setSensor(sensor);
		sensorAlerts.setStarted_at((Date) lastTwoLevels.get(lastTwoLevels.size() - 1)[1]);
		HashMap<String, String> map = new HashMap<>();
		int counter = 1;
		for (int i = lastTwoLevels.size() - 1; i >= 0; i--) {
			if (Integer.parseInt(lastTwoLevels.get(i)[0].toString()) > 2000) {
				map.put("measurement" + counter, lastTwoLevels.get(i)[0].toString());
			}
			counter++;
		}
		map.put("measurement" + counter, level.toString());
		map.put("startTime", (DateUtil.getDateStr((Date) lastTwoLevels.get(lastTwoLevels.size() - 1)[1])));
		map.put("endTime", DateUtil.getDateStr(sensor.getLastUpdatedAt()));
		sensorAlerts.setAlerts(JsonUtil.convertToString(map));
		sensorAlerts.setEnded_at(sensor.getLastUpdatedAt());
		alertRepository.save(sensorAlerts);
	}

	@Override
	public void deleteSensorData(Sensor sensor) {

		alertRepository.deleteMeasurements(sensor.getId());
		measurementRepository.deleteMeasurements(sensor.getId());
		sensorRepository.deleteSensor(sensor.getUuid());

	}

}
