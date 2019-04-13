package com.politechnika;

import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.ResultSet;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public class MeasurementRepository {
    private CassandraClient client;
    private String insertQuery = "INSERT INTO measurements (longitude, latitude, time, altitude, " +
                                 "pressure, co2, airDensity, surfaceTemperature) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private String updateQuery = "UPDATE measurements SET longitude = ?, latitude = ?, time = ?, altitude = ?, " +
                                 "pressure = ?, co2 = ?, airDensity = ?, surfaceTemperature = ? " +
                                 "WHERE time = (?)";
    private String selectQuery = "SELECT * FROM measurements " +
                                 "WHERE time = (?)";
    private String deleteQuery = "DELETE FROM measurements " +
                                 "WHERE time = (?)";

    private PreparedStatement preparedInsertStatement;
    private PreparedStatement preparedUpdateStatement;
    private PreparedStatement preparedGetStatement;
    private PreparedStatement preparedDeleteStatement;

    public MeasurementRepository(CassandraClient client) {
        this.client = client;

        prepareStatements();
    }

    public void add(MeasurementDto measurementDto, Handler<AsyncResult<ResultSet>> resultHandler) {
        client.execute(bindInsertStatement(measurementDto), resultHandler);
    }

    public void get(String timestamp, Handler<AsyncResult<List<Row>>> resultHandler) {
        client.executeWithFullFetch(preparedGetStatement.bind(timestamp), resultHandler);
    }

    public void update(MeasurementDto measurementDto, Handler<AsyncResult<ResultSet>> resultHandler) {
        client.execute(bindUpdateStatement(measurementDto, measurementDto.getTime()), resultHandler);
    }

    public void delete(String timestamp, Handler<AsyncResult<ResultSet>> resultHandler) {
        client.execute(preparedDeleteStatement.bind(timestamp), resultHandler);
    }

    private void prepareStatements() {
        client.prepare(insertQuery, preparedStatementResult -> {
            if (preparedStatementResult.succeeded()) {
                preparedInsertStatement = preparedStatementResult.result();
            } else {
                preparedStatementResult.cause().printStackTrace();
            }
        });

        client.prepare(updateQuery, preparedStatementResult -> {
            if (preparedStatementResult.succeeded()) {
                preparedUpdateStatement = preparedStatementResult.result();
            } else {
                preparedStatementResult.cause().printStackTrace();
            }
        });

        client.prepare(selectQuery, preparedStatementResult -> {
            if (preparedStatementResult.succeeded()) {
                preparedGetStatement = preparedStatementResult.result();
            } else {
                preparedStatementResult.cause().printStackTrace();
            }
        });

        client.prepare(deleteQuery, preparedStatementResult -> {
            if (preparedStatementResult.succeeded()) {
                preparedDeleteStatement = preparedStatementResult.result();
            } else {
                preparedStatementResult.cause().printStackTrace();
            }
        });
    }

    private BoundStatement bindInsertStatement(MeasurementDto measurementDto) {
        return preparedInsertStatement.bind(measurementDto.getLongitude(),
                                            measurementDto.getLatitude(),
                                            measurementDto.getTime(),
                                            measurementDto.getAltitude(),
                                            measurementDto.getPressure(),
                                            measurementDto.getCo2(),
                                            measurementDto.getAirDensity(),
                                            measurementDto.getSurfaceTemperature());
    }

    private BoundStatement bindUpdateStatement(MeasurementDto measurementDto, String timestamp) {
        return preparedUpdateStatement.bind(measurementDto.getLongitude(),
                                            measurementDto.getLatitude(),
                                            measurementDto.getTime(),
                                            measurementDto.getAltitude(),
                                            measurementDto.getPressure(),
                                            measurementDto.getCo2(),
                                            measurementDto.getAirDensity(),
                                            measurementDto.getSurfaceTemperature(),
                                            timestamp);
    }
}
