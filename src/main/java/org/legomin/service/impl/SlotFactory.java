package org.legomin.service.impl;

import java.time.Instant;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;

/**
 * In case of changing `slot` domain object you will need to change implementation of this
 * instead of changing main service. That's why it is here))
 */
public interface SlotFactory {

  Slot getSlot(Flat flat, Instant date, Slot.Status status, Tenant reservedBy);

  Long getSlotId(Flat flat, Instant date);

}
