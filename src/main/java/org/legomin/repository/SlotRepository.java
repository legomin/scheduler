package org.legomin.repository;

import java.time.Instant;
import java.util.Collection;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;

public interface SlotRepository {

  Collection<Slot> getSlots(Flat flat, Instant startDate, Instant endDate);

  Slot getSlot(Long id);

  void updateSlot(Slot slot);

  void deleteSlot(Slot slot);

}
