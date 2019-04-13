package com.politechnika;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;


public class Main extends AbstractVerticle {
    @Override
    public void start(Future<Void> future) {
        vertx.createHttpServer().requestHandler(r ->
            r.response()
             .end("<h1>Hello from " +
                  "Vert.x 3 application</h1>")
        ).listen(
            config().getInteger("http.port", 8080),
            result -> {
                if (result.succeeded()) {
                    future.complete();
                } else {
                    future.fail(result.cause());
                }
            });
    }
}
