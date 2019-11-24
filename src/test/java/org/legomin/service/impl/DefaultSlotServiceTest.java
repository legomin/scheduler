package org.legomin.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;
import org.legomin.repository.SlotRepository;
import org.legomin.service.RequestResult;
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

  private final Tenant currentTenant = new Tenant(0L, ZoneId.systemDefault());
  private final Tenant newTenant = new Tenant(1L, ZoneId.systemDefault());
  private final Flat flat = new Flat(1L,currentTenant);
  private final Instant date = LocalDateTime.now()
    .plusDays(7).withHour(11).atZone(ZoneId.systemDefault())
    .toInstant();

  @Before
  public void setUp() {
    slotService = new DefaultSlotService(slotRepository, dateValidator, slotFactory);
    when(dateValidator.test(any(Instant.class), any(ZoneId.class))).thenReturn(true);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(slotRepository, dateValidator, slotFactory);
  }

  @Test
  public void getSlots() {
    final List<Slot> slots = new ArrayList<>();
    slots.add(new Slot(1L, flat, Instant.now(), Instant.now(), null, Slot.Status.FREE));
    when(slotRepository.getSlots(any(Flat.class), any(Instant.class), any(Instant.class))).thenReturn(slots);
    assertEquals("Unexpected slots collection", slots, slotService.getSlots(flat, null, null));

    verify(slotRepository).getSlots(flat, null, null);
  }

  @Test
  public void reserveSlotSuccess() {
    when(slotRepository.getSlot(anyLong())).thenReturn(null);
    final Slot slot = new Slot(1L, flat, date, date, newTenant, Slot.Status.RESERVED);
    when(slotFactory.getSlot(any(), any(), any(), any())).thenReturn(slot);

    final RequestResult result = slotService.reserveSlot(date, newTenant, flat);
    assertEquals(RequestResult.Status.SUCCESS, result.getStatus());
    assertTrue(result.getReservation().isPresent());
    assertEquals(slot, result.getReservation().get());

    verify(dateValidator).test(date, ZoneId.systemDefault());
    verify(slotFactory).getSlot(flat, date, Slot.Status.RESERVED, newTenant);
    verify(slotRepository).getSlot(1L);
    verify(slotRepository).updateSlot(slot);
  }

  @Test
  public void cancelReservationSuccess() {
    final Slot slot = new Slot(1L, flat, date, date, newTenant, Slot.Status.RESERVED);
    when(slotRepository.getSlot(anyLong())).thenReturn(slot);
    when(slotFactory.getSlot(any(), any(), any(), any())).thenReturn(slot);

    final RequestResult result = slotService.cancelReservation(date, newTenant, flat);
    assertEquals(RequestResult.Status.SUCCESS, result.getStatus());
    assertTrue(result.getReservation().isPresent());
    assertEquals(slot, result.getReservation().get());

    verify(dateValidator).test(date, ZoneId.systemDefault());
    verify(slotFactory).getSlot(flat, date, Slot.Status.FREE, null);
    verify(slotRepository).getSlot(1L);
    verify(slotRepository).updateSlot(slot);
  }

  @Test
  public void approveSlotSuccess() {
    final Slot slot = new Slot(1L, flat, date, date, newTenant, Slot.Status.RESERVED);
    when(slotRepository.getSlot(anyLong())).thenReturn(slot);
    when(slotFactory.getSlot(any(), any(), any(), any())).thenReturn(slot);
    when(slotFactory.getSlotId(any())).thenReturn(1L);

    final RequestResult result = slotService.approveSlot(date, currentTenant, flat);
    assertEquals(RequestResult.Status.SUCCESS, result.getStatus());
    assertTrue(result.getReservation().isPresent());
    assertEquals(slot, result.getReservation().get());

    verify(dateValidator).test(date, ZoneId.systemDefault());
    verify(slotFactory).getSlotId(date);
    verify(slotFactory).getSlot(flat, date, Slot.Status.APPROVED, newTenant);
    verify(slotRepository).getSlot(1L);
    verify(slotRepository).updateSlot(slot);
  }

  @Test
  public void rejectSlotSuccess() {
    final Slot slot = new Slot(1L, flat, date, date, newTenant, Slot.Status.RESERVED);
    when(slotRepository.getSlot(anyLong())).thenReturn(slot);
    when(slotFactory.getSlot(any(), any(), any(), any())).thenReturn(slot);
    when(slotFactory.getSlotId(any())).thenReturn(1L);

    final RequestResult result = slotService.rejectSlot(date, currentTenant, flat);
    assertEquals(RequestResult.Status.SUCCESS, result.getStatus());
    assertTrue(result.getReservation().isPresent());
    assertEquals(slot, result.getReservation().get());

    verify(dateValidator).test(date, ZoneId.systemDefault());
    verify(slotFactory).getSlotId(date);
    verify(slotFactory).getSlot(flat, date, Slot.Status.REJECTED, newTenant);
    verify(slotRepository).getSlot(1L);
    verify(slotRepository).updateSlot(slot);
  }
}
