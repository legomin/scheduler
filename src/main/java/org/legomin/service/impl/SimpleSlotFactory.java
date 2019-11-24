package org.legomin.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;

public class SimpleSlotFactory implements SlotFactory{

  @Override
  public Slot getSlot(final Instant date, final Slot.Status status, final Tenant reservedBy) {

    final LocalDateTime localDate = LocalDateTime.ofInstant(date, ZoneId.systemDefault());
    final int minutes = localDate.getMinute();
    final LocalDateTime slotStartDate;
    if (minutes >= 40) {
      slotStartDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(localDate.getHour(), 40, 0));
    } else if (minutes >= 20) {
      slotStartDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(localDate.getHour(), 20, 0));
    } else {
      slotStartDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(localDate.getHour(), 0, 0));
    }

    final Instant startDate =  slotStartDate.atZone(ZoneId.systemDefault()).toInstant();
    final long id = startDate.toEpochMilli();
    final Instant endDate = slotStartDate.plusSeconds(19 * 60 + 59).atZone(ZoneId.systemDefault()).toInstant();

    return new Slot(id, startDate, endDate, reservedBy, status);
  }
}
