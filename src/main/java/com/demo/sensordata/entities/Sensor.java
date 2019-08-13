package com.demo.sensordata.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "sensor")
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sensor_uuid")
    private String uuid;

    @Column(name = "current_status")
    @Enumerated(EnumType.STRING)
    private SensorStatus currentStatus=SensorStatus.OK;


    @Column(name="last_updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedAt;



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SensorStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(SensorStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public enum SensorStatus {
        OK,
        WARN,
        ALERT
    }

}
