package com.politechnika;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Test
    void testJsonDecoding() {
        String receivedJson = "{\n" + "\t\"longitude\" : -91.053794860839844,\n" + "  \"latitude\" : -3.8782415390014648,\n" + "  \"time\"  : \"20040822194824\",\n" +
                              "  \"altitude\" : 1012.7100219726562,\n" + "  \"pressure\" : 1012.71,\n" + "  \"co2\" : 0.000365346,\n" + "  \"airDensity\" : 2.45201e+25,\n" +
                              "  \"surfaceTemperature\" : 293.139\n" + "}";

        MeasurementDto measurementDto = Json.decodeValue(receivedJson, MeasurementDto.class);
        assertNotNull(measurementDto);
    }
}