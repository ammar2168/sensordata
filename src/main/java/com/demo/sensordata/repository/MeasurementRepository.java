package com.demo.sensordata.repository;

import com.demo.sensordata.entities.SensorMeasurement;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import  com.demo.sensordata.model.SensorMetrics;

import javax.transaction.Transactional;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<SensorMeasurement,Long> {

    /**
     *
     * @param id
     * @return
     */
    @Query(value = "Select sm.level,sm.created_at from sensor_measurement sm where sm.sensor_id = :sensor_id order by id desc limit 2",nativeQuery = true)
    public List<Object[]> getLastTwoLevels(@Param("sensor_id") Long id);

    @Query(value = "Select avg(sm.level) as avgLevel,max(sm.level) as maxLevel " +
            "from sensor_measurement sm where sm.sensor_id = :sensor_id and sm.created_at>curdate()-30",nativeQuery = true)
    public List<Object[]> getAvgAndMax(@Param("sensor_id") Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from sensor_measurement where sensor_id = :sensor_id",nativeQuery = true)
    public void deleteMeasurements(@Param("sensor_id") Long id);



}
