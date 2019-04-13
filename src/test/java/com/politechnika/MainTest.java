package com.politechnika;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(VertxExtension.class)
class MainTest {
    private static int port;

    @BeforeAll
    static void setup(Vertx vertx, VertxTestContext testContext) {
        port = getRandomPort();
        JsonObject jsonConfig = new JsonObject().put("http.port", port);
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(jsonConfig);

        vertx.deployVerticle(Main.class.getName(), options,
                             testContext.completing());
    }

    @AfterAll
    static void tearDown(Vertx vertx, VertxTestContext testContext) {
        vertx.close(testContext.completing());
    }

    private static int getRandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            log.error("Could not get random port.", e);

            return 9096;
        }
    }

    @Test
    void testMyApplication(Vertx vertx, VertxTestContext testContext) throws InterruptedException {
        WebClient webClient = WebClient.create(vertx);

        webClient.get(port, "localhost", "/").send(request -> {
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