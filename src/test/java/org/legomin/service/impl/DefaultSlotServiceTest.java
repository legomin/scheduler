package org.legomin.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.repository.SlotRepository;
import org.legomin.service.SlotService;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSlotServiceTest {
  @Mock
  private SlotRepository slotRepository;

  @Mock
  private BiPredicate<Instant, ZoneId> dateValidator;

  @Mock
  private SlotFactory slotFactory;

  private SlotService slotService;

  @Before
  public void setUp() throws Exception {
    slotService = new DefaultSlotService(slotRepository, dateValidator, slotFactory);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(slotRepository, dateValidator, slotFactory);
  }

  @Test
  public void getSlots() {
    final Flat flat = new Flat(1, null);
    final List<Slot> slots = new ArrayList<>();
    slots.add(new Slot(1L, flat, Instant.now(), Instant.now(), null, Slot.Status.FREE));
    when(slotRepository.getSlots(any(Flat.class), any(Instant.class), any(Instant.class))).thenReturn(slots);
    Assert.assertEquals("Unexpected slots collection", slots, slotService.getSlots(flat, null, null));

    verify(slotRepository).getSlots(flat, null, null);
  }

  @Test
  public void reserveSlot() {
  }

  @Test
  public void cancelReservation() {
  }

  @Test
  public void approveSlot() {
  }

  @Test
  public void rejectSlot() {
  }
}
