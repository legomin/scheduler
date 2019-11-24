package org.legomin.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.function.BiPredicate;

public class SimpleDateValidator implements BiPredicate<Instant, ZoneId> {

  @Override
  public boolean test(final Instant date, final ZoneId zoneId) {
    final LocalDateTime localDateTime = LocalDateTime.ofInstant(date, zoneId);
    return testDate(localDateTime) && testTime(localDateTime);
  }

  private boolean testDate(final LocalDateTime localDateTime) {
    final LocalDate now = LocalDate.now();
    final LocalDate startDate = now.plusDays(8 - now.getDayOfWeek().getValue());
    final LocalDate endDate = now.plusDays(14 - now.getDayOfWeek().getValue());

    final LocalDate localDate = localDateTime.toLocalDate();

    return !localDate.isBefore(startDate) || !localDate.isAfter(endDate);
  }

  private boolean testTime(final LocalDateTime localDateTime) {
    final LocalTime localTime = localDateTime.toLocalTime();
    return localTime.getHour() >= 10 && localTime.getHour() < 20;
  }

}
