 
CREATE KEYSPACE measurements_db WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 2 };
USE measurements_db;
CREATE TABLE measurements (
    uniqueId TimeUUID,
    longitude double,
    latitude double,
    time varchar,
    altitude double,
    pressure double,
    co2 double,
    airDensity double,
    surfaceTemperature float,
    PRIMARY KEY (uniqueId)
);

INSERT INTO measurements (uniqueId, longitude, latitude, time, altitude, pressure, co2, airDensity, surfaceTemperature) 
            VALUES (now(), 23.000, 42.000, '20190410145341', 1012.71, 1012.70, 0.000365346, 2.45201e+25, 293.139);

SELECT * FROM measurements;
