package com.politechnika;

import lombok.Data;

@Data
public class MeasurementDto {
    private double longitude;
    private double latitude;
    private String time;
    private double altitude;
    private double pressure;
    private double co2;
    private double airDensity;
    private float surfaceTemperature;
}