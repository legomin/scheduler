package org.legomin.application.vertx;

import java.time.LocalDateTime;
import java.time.ZoneId;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ApplicationIntegrationTest {

  private final Vertx vertx = Vertx.vertx();
  private final HttpClient client = vertx.createHttpClient();
  private final String date = LocalDateTime.now()
    .plusDays(7).withHour(11).atZone(ZoneId.systemDefault())
    .toInstant().toString();

  @Before
  public void before(TestContext context) {
    final Async async = context.async();
    vertx.deployVerticle(Application.class.getName(), ar -> async.complete());
    async.awaitSuccess();

    final Async async1 = context.async();
    client.post(8080, "localhost", "/reserve")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        async1.complete();
      }).end(new JsonObject().put("date", date).put("id", 1).toString());
    async1.awaitSuccess();

    final Async async2 = context.async();
    client.get(8080, "localhost", "/slots")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        resp.bodyHandler(body -> {
          final JsonArray json = new JsonArray(body.toString());
          context.assertEquals(1, json.size());
          final JsonObject obj = json.getJsonObject(0);
          context.assertEquals("RESERVED", obj.getString("Status"));
        });
        async2.complete();
      }).end();
    async2.awaitSuccess();
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testCancel(TestContext context) {
    final Async async1 = context.async();
    client.put(8080, "localhost", "/cancel")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        async1.complete();
      }).end(new JsonObject().put("date", date).put("id", 1).toString());
    async1.awaitSuccess();

    final Async async2 = context.async();
    client.get(8080, "localhost", "/slots")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        resp.bodyHandler(body -> {
          final JsonArray json = new JsonArray(body.toString());
          context.assertEquals(1, json.size());
          final JsonObject obj = json.getJsonObject(0);
          context.assertEquals("FREE", obj.getString("Status"));
        });
        async2.complete();
      }).end();
    async2.awaitSuccess();
  }

  @Test
  public void testApprove(TestContext context) {
    final Async async1 = context.async();
    client.put(8080, "localhost", "/approve")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        async1.complete();
      }).end(new JsonObject().put("date", date).put("id", 0).toString());
    async1.awaitSuccess();

    final Async async2 = context.async();
    client.get(8080, "localhost", "/slots")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        resp.bodyHandler(body -> {
          final JsonArray json = new JsonArray(body.toString());
          context.assertEquals(1, json.size());
          final JsonObject obj = json.getJsonObject(0);
          context.assertEquals("APPROVED", obj.getString("Status"));
        });
        async2.complete();
      }).end();
    async2.awaitSuccess();
  }

  @Test
  public void testReject(TestContext context) {
    final Async async1 = context.async();
    client.put(8080, "localhost", "/reject")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        async1.complete();
      }).end(new JsonObject().put("date", date).put("id", 0).toString());
    async1.awaitSuccess();

    final Async async2 = context.async();
    client.get(8080, "localhost", "/slots")
      .exceptionHandler(err -> context.fail(err.getMessage()))
      .handler(resp -> {
        context.assertEquals(200, resp.statusCode());
        resp.bodyHandler(body -> {
          final JsonArray json = new JsonArray(body.toString());
          context.assertEquals(1, json.size());
          final JsonObject obj = json.getJsonObject(0);
          context.assertEquals("REJECTED", obj.getString("Status"));
        });
        async2.complete();
      }).end();
    async2.awaitSuccess();
  }

}