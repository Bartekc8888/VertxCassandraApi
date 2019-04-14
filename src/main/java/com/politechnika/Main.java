package com.politechnika;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;


public class Main extends AbstractVerticle {
    private MeasurementEndpointExecutor executor;
    private CassandraClient client;

    @Override
    public void start(Future<Void> future) {
        initCassandraDbConnection();
        executor = new MeasurementEndpointExecutor(client);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        createServer(future, router);

        setupRootUrlHandler(router);
        setupMeasurementUrlHandlers(router);
    }

    private void initCassandraDbConnection() {
        CassandraClientOptions options = new CassandraClientOptions()
                .setPort(config().getInteger("cassandra.port", 9042))
                .setKeyspace(config().getString("keyspace", "measurements_db"))
                .addContactPoint(config().getString("node1.address", "192.168.0.101"));

        client = CassandraClient.createShared(vertx, options);
    }

    private void createServer(Future<Void> future, Router router) {
        vertx.createHttpServer()
             .requestHandler(router)
             .listen(config().getInteger("http.port", 8080),
                     result -> {
                         if (result.succeeded()) {
                             future.complete();
                         } else {
                             future.fail(result.cause());
                         }
                     });
    }

    private void setupRootUrlHandler(Router router) {
        router.route("/")
              .handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html")
                    .end("<h1>Hello from Vert.x 3 application</h1>");
        });
    }

    private void setupMeasurementUrlHandlers(Router router) {
        router.post("/measurement").handler(executor::addMeasurement);
        router.get("/measurement/:timestamp").handler(executor::getMeasurement);
        router.get("/measurement/id/byTimestamp/:timestamp").handler(executor::getMeasurementIdByTimestamp);
        router.put("/measurement").handler(executor::updateMeasurement);
        router.delete("/measurement/:timestamp").handler(executor::removeMeasurement);
        router.delete("/measurement/id/:uuid").handler(executor::removeMeasurementById);
    }
}
