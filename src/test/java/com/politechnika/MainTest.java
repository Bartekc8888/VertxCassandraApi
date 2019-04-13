package com.politechnika;

import java.util.concurrent.TimeUnit;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(VertxExtension.class)
class MainTest {

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(Main.class.getName(),
                             testContext.completing());
    }

    @AfterEach
    void tearDown(Vertx vertx, VertxTestContext testContext) {
        vertx.close(testContext.completing());
    }

    @Test
    void testMyApplication(Vertx vertx, VertxTestContext testContext) throws InterruptedException {
        WebClient webClient = WebClient.create(vertx);

        webClient.get(8080, "localhost", "/").send(request -> {
            if (request.succeeded()) {
                Buffer body = request.result().body();
                assertTrue(body.toString().contains("Hello"));
                testContext.completeNow();
            } else {
                testContext.failNow(request.cause());
            }
        });

        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }
}