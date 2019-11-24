package org.legomin.repository;

import java.util.Collection;

import org.legomin.domain.Slot;

public interface SlotRepository {

  Collection<Slot> getSlots();

  Slot getSlot(Long id);

  void updateSlot(Slot slot);

  void deleteSlot(Slot slot);

}
