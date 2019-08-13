package com.demo.sensordata.services;

import java.util.List;
import java.util.Map;

import com.demo.sensordata.entities.Sensor;
import com.demo.sensordata.entities.SensorMeasurement;
import com.fasterxml.jackson.core.JsonProcessingException;


public interface SensorService {

    //For creating Sensor if not exists,if exists to update
    public Sensor createOrUpdateSensor(String uuid,int level,String createdDate) throws JsonProcessingException;

    //for getting sensor for a given uuid
    public Sensor getSensor(String uuid);

    //for creating measurement for given sensor
    public SensorMeasurement createMeasurement(String uuid, int level, String dateStr) throws JsonProcessingException;

    //for getting metrics in last 30 days for given sensor
    public Map<String,String> getMetrics(Sensor sensor);

    //for getting alerts for a given sensor
    public List<String> getAlerts(Sensor sensor);

    //for creating alerts for a given sensor
    public void createAlert(Sensor sensor,List<Object[]> lastThree,Integer level) throws JsonProcessingException;

    //for deleting the data

    public void deleteSensorData(Sensor sensor);



}
