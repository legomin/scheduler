package org.legomin.application.vertx;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;
import org.legomin.repository.SlotRepository;
import org.legomin.repository.mockImpl.MockSlotRepository;
import org.legomin.service.NotificationManager;
import org.legomin.service.SlotService;
import org.legomin.service.TenantNotifier;
import org.legomin.service.impl.DefaultNotificationManager;
import org.legomin.service.impl.MockTenantNotifier;
import org.legomin.service.impl.SimpleDateValidator;
import org.legomin.service.RequestResult;
import org.legomin.service.impl.SimpleSlotFactory;
import org.legomin.service.impl.DefaultSlotService;
import org.legomin.service.impl.SlotFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wow-wow, it's vertex!!
 */
public class Application extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(Application.class);

  private static final Function<Slot, JsonObject> SLOT_TO_JSON_CONVERTER = slot -> new JsonObject()
    .put("Start date", slot.getStartDate())
    .put("End date", slot.getFinishDate())
    .put("Status", slot.getStatus())
    .put("Reserved by", slot.getReservedBy() == null ? "none" : slot.getReservedBy().getId());

  private final Flat mockFlat;
  private final SlotService defaultSlotService;
  private final NotificationManager notificationManager;

  public Application() {
    super();

    this.mockFlat = new Flat(1L, new Tenant(0L, ZoneId.systemDefault()));

    final SlotRepository slotRepository = new MockSlotRepository();
    final BiPredicate<Instant, ZoneId> defaultDateValidator = new SimpleDateValidator();
    final SlotFactory simpleSlotFactory = new SimpleSlotFactory();
    this.defaultSlotService = new DefaultSlotService(slotRepository, defaultDateValidator, simpleSlotFactory);
    final TenantNotifier notifier = new MockTenantNotifier();
    this.notificationManager = new DefaultNotificationManager(notifier);
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.post("/reserve").handler(ctx -> handleAction(ctx, ((date, tenant) ->
      defaultSlotService.reserveSlot(date, tenant, mockFlat))));
    router.put("/approve").handler(ctx -> handleAction(ctx, ((date, tenant) ->
      defaultSlotService.approveSlot(date, tenant, mockFlat))));
    router.put("/reject").handler(ctx -> handleAction(ctx, ((date, tenant) ->
      defaultSlotService.rejectSlot(date, tenant, mockFlat))));
    router.put("/cancel").handler(ctx -> handleAction(ctx, ((date, tenant) ->
      defaultSlotService.cancelReservation(date, tenant, mockFlat))));
    router.get("/slots").handler(this::handleGetSlots);

    vertx.createHttpServer().requestHandler(router).listen(8080, ar -> {
      if (ar.succeeded()) {
        log.debug("Started in {} ms", System.currentTimeMillis());
      } else {
        log.error("Failed to start app", ar);
      }
    });
  }

  private void handleGetSlots(final RoutingContext routingContext) {
    final JsonArray arr = new JsonArray();
    defaultSlotService.getSlots(mockFlat, null, null)
      .forEach(slot -> arr.add(SLOT_TO_JSON_CONVERTER.apply(slot)));
    routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }

  private void handleAction(final RoutingContext routingContext,
    final BiFunction<Instant, Tenant, RequestResult> callback) {
    try {
      final JsonObject body = routingContext.getBodyAsJson();
      final Tenant tenant = new Tenant(body.getLong("id"), null);
      final Instant date = body.getInstant("date");

      final RequestResult result = callback.apply(date, tenant);
      respond(result.getHttpCode(), routingContext.response());

      if (RequestResult.Status.SUCCESS == result.getStatus()) {
        notificationManager.notify(result.getReservation().get());
      }
    } catch (NullPointerException | ClassCastException | DateTimeParseException | NoSuchElementException e) {
      log.error("failed to handle action, cause ", e);
      respond(400, routingContext.response());
    }
  }

  private void respond(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Application());
  }
}
