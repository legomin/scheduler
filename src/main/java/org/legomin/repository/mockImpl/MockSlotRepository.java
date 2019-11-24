package org.legomin.repository.mockImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.repository.SlotRepository;

/**
 * some in memory db mock
 */
public class MockSlotRepository implements SlotRepository {

  private final Map<Long, Slot> db = new ConcurrentHashMap<>();

  @Override
  public Collection<Slot> getSlots(Flat flat, Instant startDate, Instant endDate) {
    //yup, not too effective implementation ))
    return db.values().stream()
      .filter(slot -> slot.getFlat().equals(flat))
      .filter(slot -> startDate == null || !slot.getFinishDate().isBefore(startDate))
      .filter(slot -> endDate == null || !slot.getStartDate().isAfter(endDate))
      .collect(Collectors.toList());
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
