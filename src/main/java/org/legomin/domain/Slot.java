package org.legomin.domain;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

/**
 * The main domain flat scheduler class
 *
 * Yeah, it is immutable and I like that)
 */
public class Slot {

  public enum Status {
    FREE, RESERVED, REJECTED, APPROVED
  }

  private final long id;
  private final Flat flat;
  private final Instant startDate;
  private final Instant finishDate;
  private final Tenant reservedBy;
  private final Status status;

  public Slot(Long id, Flat flat, Instant startDate, Instant finishDate, Tenant reservedBy, Status status) {
    this.id = requireNonNull(id);
    this.flat = requireNonNull(flat);
    this.startDate = requireNonNull(startDate);
    this.finishDate = requireNonNull(finishDate);
    this.reservedBy = reservedBy;
    this.status = requireNonNull(status);
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

  public Flat getFlat() {
    return flat;
  }
}
