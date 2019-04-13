package com.politechnika;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;


public class Main extends AbstractVerticle {
    private MeasurementEndpointExecutor executor;

    @Override
    public void start(Future<Void> future) {
        executor = new MeasurementEndpointExecutor();

        Router router = Router.router(vertx);
        createServer(future, router);

        setupRootUrlHandler(router);
        setupMeasurementUrlHandlers(router);
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
        router.get("/measurement/:id").handler(executor::getMeasurement);
        router.put("/measurement").handler(executor::updateMeasurement);
        router.delete("/measurement/:id").handler(executor::removeMeasurement);
    }
}
