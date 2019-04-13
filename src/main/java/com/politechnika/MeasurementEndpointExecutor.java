package com.politechnika;

import java.util.List;

import com.datastax.driver.core.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AsyncResult;
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

        repository.add(measurementDto, result -> {
            if (result.succeeded()) {
                routingContext.response()
                              .setStatusCode(201)
                              .end();
            } else {
                routingContext.response()
                              .setStatusCode(500)
                              .end(result.cause().toString());
            }
        });
    }

    public void getMeasurement(RoutingContext routingContext) {
        String timestamp = routingContext.request().getParam("timestamp");

        if (timestamp == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            repository.get(timestamp, result -> {
                if (result.succeeded()) {

                    routingContext.response()
                                  .putHeader("content-type", "application/json; charset=utf-8")
                                  .setStatusCode(204)
                                  .end(Json.encodePrettily(convertToDto(result)));
                } else {
                    routingContext.response().setStatusCode(404).end();
                }
            });
        }
    }

    public void updateMeasurement(RoutingContext routingContext) {
        final MeasurementDto measurementDto = Json.decodeValue(routingContext.getBodyAsString(),
                                                               MeasurementDto.class);
        repository.update(measurementDto, result -> {
            if (result.succeeded()) {
                routingContext.response()
                              .setStatusCode(201)
                              .end();
            } else {
                routingContext.response()
                              .setStatusCode(500)
                              .end(result.cause().toString());
            }
        });

    }

    public void removeMeasurement(RoutingContext routingContext) {
        String timestamp = routingContext.request().getParam("timestamp");

        if (timestamp == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            repository.delete(timestamp, result -> {
                if (result.succeeded()) {
                    routingContext.response().setStatusCode(204).end();
                } else {
                    routingContext.response()
                                  .setStatusCode(500)
                                  .end(result.cause().toString());
                }
            });
        }
    }

    private MeasurementDto convertToDto(AsyncResult<List<Row>> result) {
        Row row = result.result().get(0);
        return new MeasurementDto(row.getDouble(0),
                                  row.getDouble(1),
                                  row.getString(2),
                                  row.getDouble(3),
                                  row.getDouble(4),
                                  row.getDouble(5),
                                  row.getDouble(6),
                                  row.getFloat(7));
    }
}
