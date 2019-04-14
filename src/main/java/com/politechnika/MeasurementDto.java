package com.politechnika;

import java.util.UUID;

public class MeasurementDto {
    private UUID uniqueId;
    private double longitude;
    private double latitude;
    private String time;
    private double altitude;
    private double pressure;
    private double co2;
    private double airDensity;
    private float surfaceTemperature;

    @java.beans.ConstructorProperties({"uniqueId", "longitude", "latitude", "time", "altitude", "pressure", "co2", "airDensity", "surfaceTemperature"})
    public MeasurementDto(UUID uniqueId, double longitude, double latitude, String time, double altitude,
                          double pressure, double co2, double airDensity, float surfaceTemperature) {
        this.uniqueId = uniqueId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.altitude = altitude;
        this.pressure = pressure;
        this.co2 = co2;
        this.airDensity = airDensity;
        this.surfaceTemperature = surfaceTemperature;
    }

    public MeasurementDto() {
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getTime() {
        return this.time;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getPressure() {
        return this.pressure;
    }

    public double getCo2() {
        return this.co2;
    }

    public double getAirDensity() {
        return this.airDensity;
    }

    public float getSurfaceTemperature() {
        return this.surfaceTemperature;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public void setAirDensity(double airDensity) {
        this.airDensity = airDensity;
    }

    public void setSurfaceTemperature(float surfaceTemperature) {
        this.surfaceTemperature = surfaceTemperature;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MeasurementDto)) {
            return false;
        }
        final MeasurementDto other = (MeasurementDto) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        if (Double.compare(this.getLongitude(), other.getLongitude()) != 0) {
            return false;
        }
        if (Double.compare(this.getLatitude(), other.getLatitude()) != 0) {
            return false;
        }
        final Object this$time = this.getTime();
        final Object other$time = other.getTime();
        if (this$time == null ? other$time != null : !this$time.equals(other$time)) {
            return false;
        }
        if (Double.compare(this.getAltitude(), other.getAltitude()) != 0) {
            return false;
        }
        if (Double.compare(this.getPressure(), other.getPressure()) != 0) {
            return false;
        }
        if (Double.compare(this.getCo2(), other.getCo2()) != 0) {
            return false;
        }
        if (Double.compare(this.getAirDensity(), other.getAirDensity()) != 0) {
            return false;
        }
        if (Float.compare(this.getSurfaceTemperature(), other.getSurfaceTemperature()) != 0) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MeasurementDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $longitude = Double.doubleToLongBits(this.getLongitude());
        result = result * PRIME + (int) ($longitude >>> 32 ^ $longitude);
        final long $latitude = Double.doubleToLongBits(this.getLatitude());
        result = result * PRIME + (int) ($latitude >>> 32 ^ $latitude);
        final Object $time = this.getTime();
        result = result * PRIME + ($time == null ? 43 : $time.hashCode());
        final long $altitude = Double.doubleToLongBits(this.getAltitude());
        result = result * PRIME + (int) ($altitude >>> 32 ^ $altitude);
        final long $pressure = Double.doubleToLongBits(this.getPressure());
        result = result * PRIME + (int) ($pressure >>> 32 ^ $pressure);
        final long $co2 = Double.doubleToLongBits(this.getCo2());
        result = result * PRIME + (int) ($co2 >>> 32 ^ $co2);
        final long $airDensity = Double.doubleToLongBits(this.getAirDensity());
        result = result * PRIME + (int) ($airDensity >>> 32 ^ $airDensity);
        result = result * PRIME + Float.floatToIntBits(this.getSurfaceTemperature());
        return result;
    }

    public String toString() {
        return "MeasurementDto(longitude=" + this.getLongitude() + ", latitude=" + this.getLatitude() + ", time=" + this.getTime() + ", altitude=" + this.getAltitude() +
               ", pressure=" + this.getPressure() + ", co2=" + this.getCo2() + ", airDensity=" + this.getAirDensity() + ", surfaceTemperature=" + this.getSurfaceTemperature() +
               ")";
    }
}
