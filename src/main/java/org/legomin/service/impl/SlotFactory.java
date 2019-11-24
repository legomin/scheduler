package org.legomin.service.impl;

import java.time.Instant;

import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;

public interface SlotFactory {

  Slot getSlot(final Instant date, final Slot.Status status, final Tenant reservedBy);

}
