package org.legomin.domain;

import java.time.Instant;

public class Slot {

  public static enum Status {
    FREE, RESERVED, REJECTED, APPROVED
  }

  private final long id;
  final Instant startDate;
  final Instant finishDate;
  final Tenant reservedBy;
  final Status status;

  public Slot(long id, Instant startDate, Instant finishDate, Tenant reservedBy, Status status) {
    this.id = id;
    this.startDate = startDate;
    this.finishDate = finishDate;
    this.reservedBy = reservedBy;
    this.status = status;
  }

  public long getId() {
    return id;
  }

  public Instant getStartDate() {
    return startDate;
  }

  public Instant getFinishDate() {
    return finishDate;
  }

  public Tenant getReservedBy() {
    return reservedBy;
  }

  public Status getStatus() {
    return status;
  }
}
