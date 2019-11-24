package org.legomin.service;

import java.time.Instant;
import java.util.Collection;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;

public interface SlotService {
  Collection<Slot> getSlots();
  RequestResult reserveSlot(final Instant date, final Tenant tenant, final Flat flat);
  RequestResult cancelReservation(final Instant date, final Tenant tenant, final Flat flat);
  RequestResult approveSlot(final Instant date, final Tenant tenant, final Flat flat);
  RequestResult rejectSlot(final Instant date, final Tenant tenant, final Flat flat);
}
