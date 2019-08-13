create database sensor;

use sensor;



create table sensor(id int(20) not null AUTO_INCREMENT,sensor_uuid varchar(200) not null ,current_status varchar(50), last_updated_at datetime, UNIQUE(sensor_uuid),PRIMARY KEY (id));







Create table sensor_measurement(id int(20) not null AUTO_INCREMENT,

sensor_id int(20), created_at datetime,level int(10), index sensor(sensor_id), index created(created_at),PRIMARY KEY (id));







create table sensor_alerts

(id int(20) not null AUTO_INCREMENT, alerts varchar(1000) not null , sensor_id int(20), started_at datetime,ended_at datetime,PRIMARY KEY (id), index sensor(sensor_id));

