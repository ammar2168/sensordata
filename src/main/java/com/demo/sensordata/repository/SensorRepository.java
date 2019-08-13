package com.demo.sensordata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.sensordata.entities.Sensor;

import javax.transaction.Transactional;

public interface SensorRepository extends JpaRepository<Sensor,Long> {

    /**
     *
     * @param uuid
     * @return
     */
    @Query("Select s from Sensor s where s.uuid= :uuid")
    public Sensor getSensorByUUID(@Param("uuid") String uuid);

    @Modifying
    @Transactional
    @Query(value = "delete from sensor  where sensor_uuid = :uuid",nativeQuery = true)
    public void deleteSensor(@Param("uuid") String id);

}
