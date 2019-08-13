package com.demo.sensordata.entities;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sensor_alerts")
public class SensorAlerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="alerts")
    private String alerts;

    @Column(name="started_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date started_at;

    @Column(name="ended_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ended_at;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public Date getStarted_at() {
        return started_at;
    }

    public void setStarted_at(Date started_at) {
        this.started_at = started_at;
    }

    public Date getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(Date ended_at) {
        this.ended_at = ended_at;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }



}
