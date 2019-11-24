package org.legomin.service.impl;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.function.BiPredicate;

import org.legomin.domain.Flat;
import org.legomin.domain.Slot;
import org.legomin.domain.Tenant;
import org.legomin.repository.SlotRepository;
import org.legomin.service.RequestResult;
import org.legomin.service.SlotService;

/**
 * Main business logic implementation
 */
public class DefaultSlotService implements SlotService {
  private final SlotRepository slotRepository;
  private final BiPredicate<Instant, ZoneId> dateValidator;
  private final SlotFactory slotFactory;

  public DefaultSlotService(SlotRepository slotRepository, BiPredicate<Instant, ZoneId> dateValidator, SlotFactory slotFactory) {
    this.slotRepository = requireNonNull(slotRepository);
    this.dateValidator = requireNonNull(dateValidator);
    this.slotFactory = requireNonNull(slotFactory);
  }

  @Override
  public Collection<Slot> getSlots(final Flat flat, final Instant startDate, final Instant finishDate) {
    return slotRepository.getSlots(flat, startDate, finishDate);
  }

  @Override
  public RequestResult reserveSlot(final Instant date, final Tenant tenant, final Flat flat) {
    final RequestResult validationResult = validateParams(date, tenant, flat);
    if (validationResult != null) {
      return validationResult;
    }
    final Tenant currentTenant = flat.getCurrentTenant();
    if (currentTenant == null) {
      return RequestResult.of(RequestResult.Status.INTERNAL);
    } else if (currentTenant.getId() == tenant.getId()) {
      return RequestResult.of(RequestResult.Status.FORBIDDEN);
    }

    if (LocalDateTime.now().plusDays(1L).isAfter(LocalDateTime.ofInstant(date, currentTenant.getTimeZone()))) {
      return RequestResult.of(RequestResult.Status.INVALID_DATE);
    }

    final Slot newSlot = slotFactory.getSlot(flat, date, Slot.Status.RESERVED, tenant);
    final Slot currentSlot = slotRepository.getSlot(newSlot.getId());
    if (currentSlot == null || currentSlot.getStatus() == Slot.Status.FREE) {
      slotRepository.updateSlot(newSlot);
      return RequestResult.of(RequestResult.Status.SUCCESS, newSlot);
    } else {
      return RequestResult.of(RequestResult.Status.FORBIDDEN);
    }
  }

  @Override
  public RequestResult cancelReservation(final Instant date, final Tenant tenant, final Flat flat) {
    final RequestResult validationResult = validateParams(date, tenant, flat);
    if (validationResult != null) {
      return validationResult;
    }

    final Slot newSlot = slotFactory.getSlot(flat, date, Slot.Status.FREE, null);
    final Slot currentSlot = slotRepository.getSlot(newSlot.getId());
    if (currentSlot != null && currentSlot.getStatus() == Slot.Status.RESERVED &&
      tenant.equals(currentSlot.getReservedBy())) {
      slotRepository.updateSlot(newSlot);
      return RequestResult.of(RequestResult.Status.SUCCESS, newSlot);
    } else {
      return RequestResult.of(RequestResult.Status.FORBIDDEN);
    }
  }

  @Override
  public RequestResult approveSlot(final Instant date, final Tenant tenant, final Flat flat) {
    return approveRejectInternal(date, tenant, flat, Slot.Status.APPROVED);
  }

  @Override
  public RequestResult rejectSlot(final Instant date, final Tenant tenant, final Flat flat) {
    return approveRejectInternal(date, tenant, flat, Slot.Status.REJECTED);
  }

  private RequestResult approveRejectInternal(final Instant date, final Tenant tenant, final Flat flat,
    final Slot.Status statusToSet) {
    final RequestResult validationResult = validateParams(date, tenant, flat);
    if (validationResult != null) {
      return validationResult;
    }

    final Tenant currentTenant = flat.getCurrentTenant();
    if (currentTenant.getId() != tenant.getId()) {
      return RequestResult.of(RequestResult.Status.FORBIDDEN);
    }

    final Slot newSlot = slotFactory.getSlot(flat, date, statusToSet, currentTenant);
    final Slot currentSlot = slotRepository.getSlot(newSlot.getId());
    if (currentSlot != null && currentSlot.getStatus() == Slot.Status.RESERVED) {
      slotRepository.updateSlot(newSlot);
      return RequestResult.of(RequestResult.Status.SUCCESS, newSlot);
    } else {
      return RequestResult.of(RequestResult.Status.FORBIDDEN);
    }
  }

  private RequestResult validateParams(final Instant date, final Tenant tenant, final Flat flat) {
    if (date == null || tenant == null || flat == null) {
      return RequestResult.of(RequestResult.Status.INVALID_REQUEST);
    } else if (!dateValidator.test(date, flat.getCurrentTenant().getTimeZone())) {
      return RequestResult.of(RequestResult.Status.INVALID_DATE);
    } else {
      return null;
    }
  }

}
