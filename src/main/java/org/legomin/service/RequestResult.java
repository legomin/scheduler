package org.legomin.service;

import java.util.Optional;

import org.legomin.domain.Slot;

/**
 * Class represents Responses from service
 *
 * If response is succeed then additionally to code holds changed scheduler entity,
 * otherwise error code only
 *
 * codes contain their http representations for convenience
 */
public class RequestResult {

  public static enum Status {
    SUCCESS(200),
    INVALID_REQUEST(400),
    INVALID_DATE(406),
    FORBIDDEN(403),
    INTERNAL(500);

    private final int httpCode;
      Status(int httpCode) {
      this.httpCode = httpCode;
    }
  }

  public static RequestResult of(final Status errorStatus) {
    return new RequestResult(errorStatus, null);
  }

  public static RequestResult of(final Status successStatus, final Slot slot) {
    return new RequestResult(successStatus, slot);
  }

  private final Status status;
  private final Slot reservation;

  private RequestResult(Status status, Slot reservation) {
    this.status = status;
    this.reservation = reservation;
  }

  public Status getStatus() {
    return status;
  }

  public int getHttpCode() {
    return status.httpCode;
  }
  public Optional<Slot> getReservation() {
    return Optional.ofNullable(reservation);
  }
}
