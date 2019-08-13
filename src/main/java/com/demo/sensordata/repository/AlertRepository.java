package com.demo.sensordata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.sensordata.entities.SensorAlerts;

import javax.transaction.Transactional;

public interface AlertRepository extends JpaRepository<SensorAlerts,Long> {
    /*@Query(value = "Select s.* from sensor_alerts s where s.sensor.id= :sensor_id order by id desc limit 1",nativeQuery = true)
    public SensorAlerts getRecentSensorAlert(@Param("sensor_id") String sensor_id);*/

    /**
     *
     * @param id
     * @return
     */
    @Query("select a.alerts from SensorAlerts a where a.sensor.id = :sensor_id")
    public List<String> getAlerts(@Param("sensor_id") Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from sensor_alerts where sensor_id = :sensor_id",nativeQuery = true)
    public void deleteMeasurements(@Param("sensor_id") Long id);
}
