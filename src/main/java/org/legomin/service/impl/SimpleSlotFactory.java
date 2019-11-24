package org.legomin.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;

/**
 * 1. calculates start date & end date from any date
 * 2. sets slot id as millis of start date
 */
public class SimpleSlotFactory implements SlotFactory {

  @Override
  public Slot getSlot(final Flat flat, final Instant date, final Slot.Status status, final Tenant reservedBy) {
    final ZoneId zoneId = ZoneId.systemDefault();
    final Long id = getSlotId(date);
    final Instant startDate = Instant.ofEpochMilli(id);
    final LocalDateTime slotStartDate = LocalDateTime.ofInstant(startDate, zoneId);

    final Instant endDate = slotStartDate.plusSeconds(19 * 60 + 59).atZone(zoneId).toInstant();

    return new Slot(id, flat, startDate, endDate, reservedBy, status);
  }

  @Override
  public Long getSlotId(final Instant date) {
    final ZoneId zoneId = ZoneId.systemDefault();
    final LocalDateTime localDate = LocalDateTime.ofInstant(date, zoneId);
    final int minutes = localDate.getMinute();
    final LocalDateTime slotStartDate;
    if (minutes >= 40) {
      slotStartDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(localDate.getHour(), 40, 0));
    } else if (minutes >= 20) {
      slotStartDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(localDate.getHour(), 20, 0));
    } else {
      slotStartDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(localDate.getHour(), 0, 0));
    }

    return slotStartDate.atZone(zoneId).toInstant().toEpochMilli();
  }
}
