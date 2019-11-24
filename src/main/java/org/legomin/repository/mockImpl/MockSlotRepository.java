package org.legomin.repository.mockImpl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.legomin.domain.Slot;
import org.legomin.repository.SlotRepository;

public class MockSlotRepository implements SlotRepository {

  private final Map<Long, Slot> db = new ConcurrentHashMap<>();

  @Override
  public Collection<Slot> getSlots() {
    return db.values();
  }

  @Override
  public Slot getSlot(final Long id) {
    return db.get(id);
  }

  @Override
  public void updateSlot(final Slot slot) {
    db.put(slot.getId(), slot);
  }

  @Override
  public void deleteSlot(Slot slot) {
    db.remove(slot.getId());
  }
}
