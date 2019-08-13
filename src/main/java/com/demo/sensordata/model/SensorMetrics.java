package com.demo.sensordata.model;

public class SensorMetrics {

    public SensorMetrics(int avgValue,int maxValue){
        this.avgLevel=avgValue;
        this.maxLevel=maxValue;
    }

    private int avgLevel;

    private int maxLevel;

    public int getAvgValue() {
        return avgLevel;
    }

    public void setAvgValue(int avgValue) {
        this.avgLevel = avgValue;
    }

    public int getMaxValue() {
        return maxLevel;
    }

    public void setMaxValue(int maxValue) {
        this.maxLevel = maxValue;
    }
}
