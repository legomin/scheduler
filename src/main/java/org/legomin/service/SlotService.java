package org.legomin.service;

import java.time.Instant;
import java.util.Collection;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;

/**
 * That's The main business logic holder
 */
public interface SlotService {

  Collection<Slot> getSlots(Flat flat, Instant startDate, Instant finishDate);

  RequestResult reserveSlot(final Instant date, final Tenant tenant, final Flat flat);

  RequestResult cancelReservation(final Instant date, final Tenant tenant, final Flat flat);

  RequestResult approveSlot(final Instant date, final Tenant tenant, final Flat flat);

  RequestResult rejectSlot(final Instant date, final Tenant tenant, final Flat flat);
}
