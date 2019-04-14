package com.politechnika;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.datastax.driver.core.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.ResultSet;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class MeasurementEndpointExecutor {
    private MeasurementRepository repository;

    public MeasurementEndpointExecutor(CassandraClient client) {
        repository = new MeasurementRepository(client);
    }

    public void addMeasurement(RoutingContext routingContext) {
        final MeasurementDto measurementDto = Json.decodeValue(routingContext.getBodyAsString(),
                                                               MeasurementDto.class);

        repository.add(measurementDto, getNoBodyResponse(routingContext, 201));
    }

    public void getMeasurement(RoutingContext routingContext) {
        String timestamp = routingContext.request().getParam("timestamp");

        if (timestamp == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            repository.get(timestamp, result -> {
                if (result.succeeded()) {
                    String responseBody = Json.encodePrettily(convertToDto(result));
                    routingContext.response()
                                  .putHeader("content-type", "application/json; charset=utf-8")
                                  .putHeader("content-length", responseBody.length() + ";")
                                  .end(responseBody);
                } else {
                    routingContext.response().setStatusCode(404).end();
                }
            });
        }
    }

    public void getMeasurementIdByTimestamp(RoutingContext routingContext) {
        String timestamp = routingContext.request().getParam("timestamp");

        if (timestamp == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            repository.getId(timestamp, result -> {
                if (result.succeeded()) {
                    List<UUID> ids = result.result().stream().map(row -> row.getUUID(0)).collect(Collectors.toList());
                    String responseBody = Json.encodePrettily(ids);

                    routingContext.response()
                                  .putHeader("content-type", "text/plain; charset=utf-8")
                                  .putHeader("content-length", responseBody.length() + ";")
                                  .end(responseBody);
                } else {
                    routingContext.response().setStatusCode(404).end();
                }
            });
        }
    }

    public void updateMeasurement(RoutingContext routingContext) {
        final MeasurementDto measurementDto = Json.decodeValue(routingContext.getBodyAsString(),
                                                               MeasurementDto.class);
        repository.update(measurementDto, getNoBodyResponse(routingContext, 201));

    }

    public void removeMeasurement(RoutingContext routingContext) {
        String timestamp = routingContext.request().getParam("timestamp");

        if (timestamp == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            repository.delete(timestamp, getNoBodyResponse(routingContext, 204));
        }
    }

    public void removeMeasurementById(RoutingContext routingContext) {
        String id = routingContext.request().getParam("uuid");

        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            UUID uuid = UUID.fromString(id);
            repository.delete(uuid, getNoBodyResponse(routingContext, 204));
        }
    }

    private Handler<AsyncResult<ResultSet>> getNoBodyResponse(RoutingContext routingContext, int i) {
        return result -> {
            if (result.succeeded()) {
                routingContext.response().setStatusCode(i).end();
            } else {
                routingContext.response().setStatusMessage(result.cause().toString()).setStatusCode(500).end();
            }
        };
    }

    private MeasurementDto convertToDto(AsyncResult<List<Row>> result) {
        Row row = result.result().get(0);
        return new MeasurementDto(row.getUUID("uniqueId"),
                                  row.getDouble("longitude"),
                                  row.getDouble("latitude"),
                                  row.getString("time"),
                                  row.getDouble("altitude"),
                                  row.getDouble("pressure"),
                                  row.getDouble("co2"),
                                  row.getDouble("airDensity"),
                                  row.getFloat("surfaceTemperature"));
    }
}
