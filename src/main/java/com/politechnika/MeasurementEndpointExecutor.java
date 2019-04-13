package com.politechnika;

import java.util.Optional;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class MeasurementEndpointExecutor {
    private MeasurementRepository repository;

    public MeasurementEndpointExecutor() {
        repository = new MeasurementRepository();
    }

    public void addMeasurement(RoutingContext routingContext) {
        final MeasurementDto measurementDto = Json.decodeValue(routingContext.getBodyAsString(),
                                                       MeasurementDto.class);
        repository.add(measurementDto);
        routingContext.response()
                      .setStatusCode(201)
                      .end();
    }

    public void getMeasurement(RoutingContext routingContext) {
        Integer id = getIdFromRequest(routingContext);

        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            Optional<MeasurementDto> measurementDto = repository.get(id);
            if (measurementDto.isPresent()) {
                routingContext.response()
                              .putHeader("content-type", "application/json; charset=utf-8")
                              .setStatusCode(204)
                              .end(Json.encodePrettily(measurementDto.get()));
            } else {
                routingContext.response().setStatusCode(404).end();
            }
        }
    }

    public void updateMeasurement(RoutingContext routingContext) {
        final MeasurementDto measurementDto = Json.decodeValue(routingContext.getBodyAsString(),
                                                               MeasurementDto.class);
        repository.update(measurementDto);
        routingContext.response()
                      .setStatusCode(201)
                      .end();
    }

    public void removeMeasurement(RoutingContext routingContext) {
        Integer id = getIdFromRequest(routingContext);

        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            repository.delete(id);
        }

        routingContext.response().setStatusCode(204).end();
    }

    private Integer getIdFromRequest(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");

        if (id == null) {
            return null;
        } else {
            return Integer.parseInt(id);
        }
    }
}
